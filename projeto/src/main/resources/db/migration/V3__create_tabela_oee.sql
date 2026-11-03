-- ==========================================================
-- V3__create_tabela_oee.sql
-- Tabela para os apontamentos do indicador OEE
-- ==========================================================

CREATE TABLE tb_apuracao_oee (
    id_apuracao_oee BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento BIGINT NOT NULL,
    tempo_planejado_minutos INT NOT NULL,
    tempo_parada_minutos INT NOT NULL,
    producao_total INT NOT NULL,
    pecas_refugadas INT NOT NULL,
    tempo_ciclo_padrao DOUBLE NOT NULL,
    indice_disponibilidade DOUBLE,
    indice_performance DOUBLE,
    indice_qualidade DOUBLE,
    indice_oee DOUBLE,
    
    FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento(id_apontamento)
);