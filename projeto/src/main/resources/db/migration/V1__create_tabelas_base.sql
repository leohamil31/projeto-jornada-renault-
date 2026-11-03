-- ==========================================================
-- V1__create_tabelas_base.sql
-- Schema transacional (OLTP) - Laboratório Lean Renault
-- ==========================================================

SET NAMES utf8mb4;

-- ----------------------------------------------------------
-- DIMENSÕES
-- ----------------------------------------------------------

CREATE TABLE tb_area (
    id_area      BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo       VARCHAR(20)  NOT NULL UNIQUE,
    nome         VARCHAR(100) NOT NULL,
    descricao    VARCHAR(255),
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_turno (
    id_turno     BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo       VARCHAR(10)  NOT NULL UNIQUE,
    nome         VARCHAR(50)  NOT NULL,
    hora_inicio  TIME         NOT NULL,
    hora_fim     TIME         NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_usuario (
    id_usuario   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(150) NOT NULL,
    matricula    VARCHAR(20)  NOT NULL UNIQUE,
    email        VARCHAR(150) NOT NULL UNIQUE,
    perfil       VARCHAR(30)  NOT NULL DEFAULT 'OPERADOR', -- OPERADOR, ANALISTA_LEAN, SUPERVISOR, ADMIN
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_equipamento (
    id_equipamento             BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_area                    BIGINT NOT NULL,
    codigo                     VARCHAR(20)  NOT NULL UNIQUE,
    nome                       VARCHAR(100) NOT NULL,
    tempo_ciclo_ideal_segundos DECIMAL(10,2) NOT NULL COMMENT 'Tempo de ciclo ideal por peça, usado no cálculo de Performance do OEE',
    ativo                      BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_equipamento_area FOREIGN KEY (id_area) REFERENCES tb_area (id_area) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_motivo_parada (
    id_motivo_parada BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo           VARCHAR(20)  NOT NULL UNIQUE,
    descricao        VARCHAR(150) NOT NULL,
    categoria        VARCHAR(50)  NOT NULL, -- MANUTENCAO, FALTA_MATERIAL, TROCA_FERRAMENTA, QUALIDADE, OUTROS
    ativo            BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE tb_tipo_indicador (
    id_tipo_indicador BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo            VARCHAR(20)  NOT NULL UNIQUE, -- '5S', 'OEE', 'GERAL'
    nome              VARCHAR(100) NOT NULL,
    descricao         VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE dim_calendario (
    data          DATE PRIMARY KEY,
    ano           SMALLINT     NOT NULL,
    mes           TINYINT      NOT NULL,
    nome_mes      VARCHAR(20)  NOT NULL,
    dia           TINYINT      NOT NULL,
    dia_semana    VARCHAR(20)  NOT NULL,
    trimestre     TINYINT      NOT NULL,
    is_dia_util   BOOLEAN      NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='Tabela física de calendário para Time Intelligence no Power BI';

-- ----------------------------------------------------------
-- FATO HEADER
-- ----------------------------------------------------------

CREATE TABLE tb_apontamento (
    id_apontamento    BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_area           BIGINT   NOT NULL,
    id_turno          BIGINT   NOT NULL,
    id_usuario        BIGINT   NOT NULL,
    id_tipo_indicador BIGINT   NOT NULL,
    data_hora         DATETIME NOT NULL,
    status            VARCHAR(30) NOT NULL DEFAULT 'PENDENTE_VALIDACAO', -- PENDENTE_VALIDACAO, VALIDADO, REJEITADO
    observacoes       VARCHAR(500),
    criado_em         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_apontamento_area    FOREIGN KEY (id_area)           REFERENCES tb_area (id_area) ON DELETE RESTRICT,
    CONSTRAINT fk_apontamento_turno   FOREIGN KEY (id_turno)          REFERENCES tb_turno (id_turno) ON DELETE RESTRICT,
    CONSTRAINT fk_apontamento_usuario FOREIGN KEY (id_usuario)        REFERENCES tb_usuario (id_usuario) ON DELETE RESTRICT,
    CONSTRAINT fk_apontamento_tipo    FOREIGN KEY (id_tipo_indicador) REFERENCES tb_tipo_indicador (id_tipo_indicador) ON DELETE RESTRICT,

    INDEX idx_apontamento_data (data_hora),
    INDEX idx_apontamento_area_turno (id_area, id_turno),
    INDEX idx_apontamento_tipo (id_tipo_indicador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------
-- FATO DETALHE: AUDITORIA 5S
-- ----------------------------------------------------------

CREATE TABLE tb_auditoria_5s (
    id_auditoria_5s  BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento   BIGINT NOT NULL UNIQUE,

    nota_seiri       TINYINT NOT NULL COMMENT 'Utilização - escala 0 a 5',
    nota_seiton      TINYINT NOT NULL COMMENT 'Organização - escala 0 a 5',
    nota_seiso       TINYINT NOT NULL COMMENT 'Limpeza - escala 0 a 5',
    nota_seiketsu    TINYINT NOT NULL COMMENT 'Padronização - escala 0 a 5',
    nota_shitsuke    TINYINT NOT NULL COMMENT 'Disciplina - escala 0 a 5',

    nota_total DECIMAL(4,2) GENERATED ALWAYS AS (
        (nota_seiri + nota_seiton + nota_seiso + nota_seiketsu + nota_shitsuke) / 5.0
    ) STORED,

    plano_acao TEXT COMMENT 'Descrição das ações corretivas quando a nota for baixa',

    CONSTRAINT fk_5s_apontamento FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento (id_apontamento) ON DELETE CASCADE,
    CONSTRAINT chk_5s_notas CHECK (
        nota_seiri BETWEEN 0 AND 5 AND nota_seiton BETWEEN 0 AND 5 AND
        nota_seiso BETWEEN 0 AND 5 AND nota_seiketsu BETWEEN 0 AND 5 AND
        nota_shitsuke BETWEEN 0 AND 5
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------
-- FATO DETALHE: APURAÇÃO DE OEE
-- ----------------------------------------------------------

CREATE TABLE tb_oee_apuracao (
    id_oee_apuracao       BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento        BIGINT NOT NULL UNIQUE,
    id_equipamento        BIGINT NOT NULL,

    tempo_planejado_min   INT NOT NULL COMMENT 'Tempo total planejado de produção no turno, em minutos',
    tempo_parada_min      INT NOT NULL DEFAULT 0 COMMENT 'Soma das paradas registradas em tb_parada_producao',
    quantidade_produzida  INT NOT NULL DEFAULT 0,
    quantidade_refugo     INT NOT NULL DEFAULT 0,

    -- Disponibilidade = (Tempo Planejado - Tempo Parado) / Tempo Planejado
    disponibilidade DECIMAL(6,4) GENERATED ALWAYS AS (
        CASE WHEN tempo_planejado_min > 0
             THEN (tempo_planejado_min - tempo_parada_min) / tempo_planejado_min
             ELSE 0 END
    ) STORED,

    CONSTRAINT fk_oee_apontamento  FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento (id_apontamento) ON DELETE CASCADE,
    CONSTRAINT fk_oee_equipamento  FOREIGN KEY (id_equipamento) REFERENCES tb_equipamento (id_equipamento) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------
-- FATO DETALHE (1:N): PARADAS DE PRODUÇÃO
-- ----------------------------------------------------------

CREATE TABLE tb_parada_producao (
    id_parada         BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_oee_apuracao   BIGINT NOT NULL,
    id_motivo_parada  BIGINT NOT NULL,

    hora_inicio       DATETIME NOT NULL,
    hora_fim          DATETIME NOT NULL,

    duracao_min INT GENERATED ALWAYS AS (TIMESTAMPDIFF(MINUTE, hora_inicio, hora_fim)) STORED,

    observacao        VARCHAR(255),

    CONSTRAINT fk_parada_oee    FOREIGN KEY (id_oee_apuracao)  REFERENCES tb_oee_apuracao (id_oee_apuracao) ON DELETE CASCADE,
    CONSTRAINT fk_parada_motivo FOREIGN KEY (id_motivo_parada) REFERENCES tb_motivo_parada (id_motivo_parada) ON DELETE RESTRICT,

    INDEX idx_parada_motivo (id_motivo_parada),
    INDEX idx_parada_periodo (hora_inicio, hora_fim)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
COMMENT='Cada linha é um evento de parada — base para a análise de gargalos (Pareto de motivos)';

-- ----------------------------------------------------------
-- ANEXOS (EVIDÊNCIAS)
-- ----------------------------------------------------------

CREATE TABLE tb_anexo (
    id_anexo        BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento  BIGINT NOT NULL,
    url_arquivo     VARCHAR(500) NOT NULL COMMENT 'Referência ao storage externo (S3, Azure Blob etc.), nunca o binário',
    tipo_arquivo    VARCHAR(20)  NOT NULL, -- IMAGEM, PDF
    criado_em       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_anexo_apontamento FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento (id_apontamento) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;