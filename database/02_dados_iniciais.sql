/* Stoki: esses são os inserts iniciais do banco de bados para conseguirmos ter uma base.*/

INSERT INTO usuario (nome, login, senha, perfil) VALUES
                                                     ('Gerente Demonstração', 'gerente', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'GERENTE'),
                                                     ('Responsável Demonstração', 'estoque', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'RESPONSAVEL'),
                                                     ('Repositor Demonstração', 'repositor', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'REPOSITOR');

INSERT INTO fornecedor (razao_social, cnpj, endereco) VALUES
                                                          ('entregadora de Alimentos', '11222333000181', 'Rua Exemplo, 100, Tubarão, SC'),
                                                          ('Bebidas Ajato Exemplo', '44555666000181', 'Avenida Modelo, 200, Tubarão, SC'),
                                                          ('Limpou EXEMPLO', '77888999000181', 'Rua Fictícia, 300, Tubarão, SC');

INSERT INTO setor (numero, nome, cor) VALUES
                                          (1, 'Bebidas', '#1565C0'),
                                          (2, 'Mercearia', '#8D6E63'),
                                          (3, 'Laticínios', '#2E7D32'),
                                          (4, 'Hortifrúti', '#7CB342'),
                                          (5, 'Limpeza', '#6A1B9A'),
                                          (6, 'Higiene', '#C2185B');

INSERT INTO produto (nome, codigo_barras, setor_id, prateleira, unidade_estoque, conteudo, unidade_conteudo, estoque_minimo, estoque_maximo, tem_validade, cadastrado_por) VALUES
                                                                                                                                                                               ('Coca-Cola 2 L', '0000000000001', 1, 1, 'UN', 2, 'L', 10, 20, TRUE, 1),
                                                                                                                                                                               ('Água mineral 500 mL', NULL, 1, 2, 'UN', 500, 'mL', 24, 120, TRUE, 1),
                                                                                                                                                                               ('Arroz 5 kg', '0000000000002', 2, 1, 'UN', 5, 'kg', 10, 60, TRUE, 1),
                                                                                                                                                                               ('Feijão 1 kg', NULL, 2, 1, 'UN', 1, 'kg', 10, 40, TRUE, 1),
                                                                                                                                                                               ('Farinha de trigo 1 kg', NULL, 2, 2, 'UN', 1, 'kg', 8, 30, TRUE, 1),
                                                                                                                                                                               ('Chocolate 90 g', '0000000000003', 2, 3, 'UN', 90, 'g', 12, 50, TRUE, 1),
                                                                                                                                                                               ('Iogurte 170 g', NULL, 3, 1, 'UN', 170, 'g', 12, 40, TRUE, 1),
                                                                                                                                                                               ('Leite integral 1 L', '0000000000004', 3, 2, 'UN', 1, 'L', 24, 80, TRUE, 1),
                                                                                                                                                                               ('Queijo muçarela', NULL, 3, 3, 'KG', NULL, NULL, 5, 20, TRUE, 1),
                                                                                                                                                                               ('Banana', NULL, 4, 1, 'KG', NULL, NULL, 10, 40, TRUE, 1),
                                                                                                                                                                               ('Detergente 500 mL', NULL, 5, 1, 'UN', 500, 'mL', 12, 48, FALSE, 1),
                                                                                                                                                                               ('Sabonete 90 g', NULL, 6, 1, 'UN', 90, 'g', 12, 60, FALSE, 1);

INSERT INTO lote (produto_id, fornecedor_id, numero_lote, validade, custo_unitario, data_entrada) VALUES
                                                                                                      (1, 2, 'BEB-001', CURRENT_DATE + 200, 6.84, NOW() - INTERVAL '10 days'),
                                                                                                      (1, 2, 'BEB-002', CURRENT_DATE + 12, 7.00, NOW() - INTERVAL '30 days'),
                                                                                                      (2, 2, 'BEB-003', CURRENT_DATE + 240, 1.20, NOW() - INTERVAL '8 days'),
                                                                                                      (3, 1, 'MER-001', CURRENT_DATE + 300, 22.50, NOW() - INTERVAL '6 days'),
                                                                                                      (4, 1, 'MER-002', CURRENT_DATE + 200, 6.90, NOW() - INTERVAL '6 days'),
                                                                                                      (5, 1, 'MER-003', CURRENT_DATE + 120, 4.30, NOW() - INTERVAL '6 days'),
                                                                                                      (6, 1, 'MER-004', CURRENT_DATE + 150, 8.67, NOW() - INTERVAL '20 days'),
                                                                                                      (7, 1, 'LAT-001', CURRENT_DATE - 3, 2.50, NOW() - INTERVAL '40 days'),
                                                                                                      (7, 1, 'LAT-002', CURRENT_DATE + 45, 2.60, NOW() - INTERVAL '5 days'),
                                                                                                      (8, 1, 'LAT-003', CURRENT_DATE + 10, 4.10, NOW() - INTERVAL '15 days'),
                                                                                                      (9, 1, 'LAT-004', CURRENT_DATE + 5, 30.50, NOW() - INTERVAL '12 days'),
                                                                                                      (10, 1, 'HOR-001', CURRENT_DATE + 4, 3.20, NOW() - INTERVAL '2 days'),
                                                                                                      (11, 3, 'LIM-001', NULL, 2.10, NOW() - INTERVAL '25 days'),
                                                                                                      (12, 3, 'HIG-001', NULL, 1.90, NOW() - INTERVAL '25 days');

INSERT INTO movimentacao (lote_id, usuario_id, tipo, quantidade, data_hora) VALUES
                                                                                (1, 2, 'ENTRADA', 8, NOW() - INTERVAL '10 days'),
                                                                                (2, 2, 'ENTRADA', 18, NOW() - INTERVAL '30 days'),
                                                                                (3, 2, 'ENTRADA', 60, NOW() - INTERVAL '8 days'),
                                                                                (4, 2, 'ENTRADA', 70, NOW() - INTERVAL '6 days'),
                                                                                (5, 2, 'ENTRADA', 20, NOW() - INTERVAL '6 days'),
                                                                                (6, 2, 'ENTRADA', 6, NOW() - INTERVAL '6 days'),
                                                                                (7, 2, 'ENTRADA', 87, NOW() - INTERVAL '20 days'),
                                                                                (8, 2, 'ENTRADA', 4, NOW() - INTERVAL '40 days'),
                                                                                (9, 2, 'ENTRADA', 6, NOW() - INTERVAL '5 days'),
                                                                                (10, 2, 'ENTRADA', 32, NOW() - INTERVAL '15 days'),
                                                                                (11, 2, 'ENTRADA', 1.5, NOW() - INTERVAL '12 days'),
                                                                                (12, 2, 'ENTRADA', 12.5, NOW() - INTERVAL '2 days'),
                                                                                (13, 2, 'ENTRADA', 30, NOW() - INTERVAL '25 days'),
                                                                                (14, 2, 'ENTRADA', 5, NOW() - INTERVAL '25 days'),
                                                                                (10, 3, 'SAIDA', -2, NOW() - INTERVAL '3 days');

INSERT INTO movimentacao (lote_id, usuario_id, tipo, quantidade, motivo_perda, observacao, data_hora) VALUES
    (1, 3, 'PERDA', -1, 'AVARIA', 'Garrafa furada', NOW() - INTERVAL '2 days');

-- ultima contagem finalizada a 20 dias, para demonstrar aviso de contagem atrasados--

INSERT INTO contagem (usuario_id, aberta_em, finalizada_em, status) VALUES
    (2, NOW() - INTERVAL '20 days', NOW() - INTERVAL '20 days', 'FINALIZADA');