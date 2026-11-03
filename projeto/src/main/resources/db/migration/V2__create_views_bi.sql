-- ==========================================================
-- V2__create_views_bi.sql
-- Views Analíticas para consumo no Power BI
-- ==========================================================

-- VIEW 1: Auditoria 5S (Pronta para dashboards de conformidade)
CREATE OR REPLACE VIEW vw_bi_auditoria_5s AS
SELECT
    a.id_apontamento,
    DATE(a.data_hora) AS data_referencia,
    a.data_hora,
    ar.codigo AS area_codigo,
    ar.nome AS area_nome,
    t.nome AS turno,
    u.nome AS auditor,
    s.nota_seiri AS utilizacao,
    s.nota_seiton AS organizacao,
    s.nota_seiso AS limpeza,
    s.nota_seiketsu AS padronizacao,
    s.nota_shitsuke AS disciplina,
    s.nota_total,
    s.plano_acao,
    a.status
FROM tb_apontamento a
INNER JOIN tb_auditoria_5s s ON a.id_apontamento = s.id_apontamento
INNER JOIN tb_area ar ON a.id_area = ar.id_area
INNER JOIN tb_turno t ON a.id_turno = t.id_turno
INNER JOIN tb_usuario u ON a.id_usuario = u.id_usuario;

-- VIEW 2: Cálculo Completo de OEE (Disponibilidade, Performance e Qualidade)
CREATE OR REPLACE VIEW vw_bi_oee AS
SELECT
    a.id_apontamento,
    DATE(a.data_hora) AS data_referencia,
    ar.nome AS area,
    e.nome AS equipamento,
    t.nome AS turno,
    u.nome AS operador,
    
    o.tempo_planejado_min,
    o.tempo_parada_min,
    (o.tempo_planejado_min - o.tempo_parada_min) AS tempo_operacional_min,
    o.quantidade_produzida,
    o.quantidade_refugo,
    o.disponibilidade,

    -- Performance = (Quantidade Produzida * Tempo Ciclo Ideal em Minutos) / Tempo Operacional
    CAST(
        CASE 
            WHEN (o.tempo_planejado_min - o.tempo_parada_min) > 0 
            THEN (o.quantidade_produzida * (e.tempo_ciclo_ideal_segundos / 60.0)) / (o.tempo_planejado_min - o.tempo_parada_min)
            ELSE 0 
        END 
    AS DECIMAL(6,4)) AS performance,

    -- Qualidade = (Produzidas Boas) / Total Produzido
    CAST(
        CASE 
            WHEN o.quantidade_produzida > 0 
            THEN (o.quantidade_produzida - o.quantidade_refugo) / o.quantidade_produzida
            ELSE 0 
        END 
    AS DECIMAL(6,4)) AS qualidade,

    -- OEE = Disponibilidade * Performance * Qualidade
    CAST(
        o.disponibilidade * 
        (CASE WHEN (o.tempo_planejado_min - o.tempo_parada_min) > 0 THEN (o.quantidade_produzida * (e.tempo_ciclo_ideal_segundos / 60.0)) / (o.tempo_planejado_min - o.tempo_parada_min) ELSE 0 END) * 
        (CASE WHEN o.quantidade_produzida > 0 THEN (o.quantidade_produzida - o.quantidade_refugo) / o.quantidade_produzida ELSE 0 END)
    AS DECIMAL(6,4)) AS oee_final,
    
    a.status
FROM tb_apontamento a
INNER JOIN tb_oee_apuracao o ON a.id_apontamento = o.id_apontamento
INNER JOIN tb_equipamento e ON o.id_equipamento = e.id_equipamento
INNER JOIN tb_area ar ON a.id_area = ar.id_area
INNER JOIN tb_turno t ON a.id_turno = t.id_turno
INNER JOIN tb_usuario u ON a.id_usuario = u.id_usuario;

-- VIEW 3: Paradas de Produção (Alimenta o Pareto de Gargalos)
CREATE OR REPLACE VIEW vw_bi_paradas_producao AS
SELECT
    p.id_parada,
    DATE(p.hora_inicio) AS data_referencia,
    ar.nome AS area,
    e.nome AS equipamento,
    t.nome AS turno,
    m.codigo AS motivo_codigo,
    m.descricao AS motivo_descricao,
    m.categoria AS motivo_categoria,
    p.hora_inicio,
    p.hora_fim,
    p.duracao_min
FROM tb_parada_producao p
INNER JOIN tb_oee_apuracao o ON p.id_oee_apuracao = o.id_oee_apuracao
INNER JOIN tb_apontamento a ON o.id_apontamento = a.id_apontamento
INNER JOIN tb_equipamento e ON o.id_equipamento = e.id_equipamento
INNER JOIN tb_area ar ON a.id_area = ar.id_area
INNER JOIN tb_turno t ON a.id_turno = t.id_turno
INNER JOIN tb_motivo_parada m ON p.id_motivo_parada = m.id_motivo_parada;