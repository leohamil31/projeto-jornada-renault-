CREATE TABLE tb_apuracao_oee (
    id_apuracao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento BIGINT NOT NULL UNIQUE,
    tempo_planejado_minutos INT NOT NULL,
    tempo_parada_minutos INT NOT NULL,
    tempo_ciclo_ideal_segundos DOUBLE NOT NULL,
    quantidade_produzida INT NOT NULL,
    quantidade_refugo INT NOT NULL,
    indice_disponibilidade DOUBLE,
    indice_performance DOUBLE,
    indice_qualidade DOUBLE,
    oee_total DOUBLE,
    CONSTRAINT fk_apuracao_apontamento FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento(id_apontamento)
);