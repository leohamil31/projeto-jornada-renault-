CREATE TABLE tb_auditoria_seguranca (
    id_auditoria_seguranca BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento BIGINT NOT NULL UNIQUE,
    epi_conforme BOOLEAN NOT NULL,
    protecoes_maquinas_conformes BOOLEAN NOT NULL,
    rotas_emergencia_desobstruidas BOOLEAN NOT NULL,
    sinalizacao_adequada BOOLEAN NOT NULL,
    area_sem_riscos_evidentes BOOLEAN NOT NULL,
    status VARCHAR(30) NOT NULL,
    plano_acao TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditoria_seguranca_apontamento
        FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento(id_apontamento)
);

CREATE TABLE tb_volume_producao (
    id_volume_producao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento BIGINT NOT NULL UNIQUE,
    quantidade_produzida INT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_volume_producao_apontamento
        FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento(id_apontamento)
);

UPDATE tb_tipo_indicador
SET codigo = 'SEGURANCA',
    nome = 'Auditoria de segurança',
    descricao = 'Verificação de condições básicas de segurança da área'
WHERE codigo = '5S';

UPDATE tb_tipo_indicador
SET codigo = 'PRODUCAO',
    nome = 'Volume de produção',
    descricao = 'Quantidade total de peças produzidas por apontamento'
WHERE codigo = 'OEE';
