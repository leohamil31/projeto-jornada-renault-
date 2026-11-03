CREATE TABLE tb_auditoria_5s (
    id_auditoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_apontamento BIGINT NOT NULL UNIQUE,
    nota_seiri INT NOT NULL,
    nota_seiton INT NOT NULL,
    nota_seiso INT NOT NULL,
    nota_seiketsu INT NOT NULL,
    nota_shitsuke INT NOT NULL,
    plano_acao VARCHAR(500),
    
    -- Garante que o apontamento existe na tabela principal
    CONSTRAINT fk_auditoria_apontamento FOREIGN KEY (id_apontamento) REFERENCES tb_apontamento(id_apontamento)
);