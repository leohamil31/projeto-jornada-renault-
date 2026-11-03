-- ==========================================================
-- V1.1__insert_dados_iniciais.sql
-- Carga inicial de catálogos - Laboratório Lean 
-- ==========================================================

-- 1. Áreas / Células do Laboratório
INSERT INTO tb_area (codigo, nome, descricao) VALUES
('SLD01', 'Solda Ponto', 'Célula robotizada de soldagem de carrocerias'),
('PNT01', 'Pintura', 'Cabine de pintura eletrostática e estufa'),
('MNT01', 'Montagem Final', 'Linha de montagem de acabamento e guarnição');

-- 2. Turnos de Trabalho (Padrão 3 turnos automotivos)
INSERT INTO tb_turno (codigo, nome, hora_inicio, hora_fim) VALUES
('T1', '1º Turno', '06:00:00', '14:00:00'),
('T2', '2º Turno', '14:00:00', '22:00:00'),
('T3', '3º Turno', '22:00:00', '06:00:00');

-- 3. Tipos de Indicador
INSERT INTO tb_tipo_indicador (codigo, nome, descricao) VALUES
('5S', 'Auditoria 5S', 'Avaliação semanal dos 5 sensos da qualidade Lean'),
('OEE', 'Apuração OEE', 'Overall Equipment Effectiveness (Eficácia Global do Equipamento)'),
('GERAL', 'Apontamento Geral', 'Registros avulsos do chão de fábrica');

-- 4. Usuários Iniciais (Admin / Analista / Operador)
INSERT INTO tb_usuario (nome, matricula, email, perfil) VALUES
('Emilio Rodrigues', 'MAT998877', 'emilio.rodrigues@unisenai.edu.br', 'ANALISTA_LEAN'),
('Operador Padrão', 'MAT000001', 'operador1@lab.local', 'OPERADOR');

-- 5. Equipamentos (Necessário para cálculo de Performance do OEE)
-- O tempo de ciclo ideal em segundos é crucial para validar se a máquina produziu o que devia no tempo em que esteve rodando.
INSERT INTO tb_equipamento (id_area, codigo, nome, tempo_ciclo_ideal_segundos) VALUES
((SELECT id_area FROM tb_area WHERE codigo = 'SLD01'), 'RB-SLD-01', 'Robô de Solda KUKA', 45.00),
((SELECT id_area FROM tb_area WHERE codigo = 'MNT01'), 'EA-MNT-01', 'Esteira de Acabamento', 60.00);

-- 6. Motivos de Parada (Catálogo para o Pareto de Gargalos)
INSERT INTO tb_motivo_parada (codigo, descricao, categoria) VALUES
('MAN-01', 'Falha Elétrica no Equipamento', 'MANUTENCAO'),
('MAN-02', 'Falha Mecânica/Pneumática', 'MANUTENCAO'),
('MAT-01', 'Falta de Peças na Linha', 'FALTA_MATERIAL'),
('SET-01', 'Troca de Ferramenta / Setup', 'TROCA_FERRAMENTA'),
('QUA-01', 'Parada para Ajuste de Qualidade', 'QUALIDADE'),
('ORG-01', 'Limpeza / Organização 5S', 'OUTROS');