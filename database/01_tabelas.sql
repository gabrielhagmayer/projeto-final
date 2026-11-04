-- STOKI: estrutura do banco. Este arquivo é executado automaticamente na primeira vez que o sistema roda.

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(64) NOT NULL,
    perfil VARCHAR(12) NOT NULL CHECK (perfil IN ('GERENTE', 'RESPONSAVEL', 'REPOSITOR')),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE fornecedor (
    id SERIAL PRIMARY KEY,
    razao_social VARCHAR(120) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    endereco VARCHAR(150),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE setor (
    id SERIAL PRIMARY KEY,
    numero INTEGER NOT NULL UNIQUE CHECK (numero > 0),
    nome VARCHAR(60) NOT NULL UNIQUE,
    cor VARCHAR(7)
);

CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    codigo_barras VARCHAR(14) UNIQUE,
    setor_id INTEGER NOT NULL REFERENCES setor (id) ON DELETE RESTRICT,
    prateleira INTEGER NOT NULL CHECK (prateleira > 0),
    unidade_estoque VARCHAR(2) NOT NULL CHECK (unidade_estoque IN ('UN', 'KG', 'L')),
    conteudo NUMERIC(10, 3) CHECK (conteudo IS NULL OR conteudo > 0),
    unidade_conteudo VARCHAR(2) CHECK (unidade_conteudo IS NULL OR unidade_conteudo IN ('mL', 'L', 'g', 'kg')),
    estoque_minimo NUMERIC(12, 3) NOT NULL CHECK (estoque_minimo >= 0),
    estoque_maximo NUMERIC(12, 3) NOT NULL,
    tem_validade BOOLEAN NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    cadastrado_por INTEGER NOT NULL REFERENCES usuario (id) ON DELETE RESTRICT,
    CHECK (estoque_maximo > estoque_minimo),
    CHECK ((conteudo IS NULL) = (unidade_conteudo IS NULL)),
    CHECK (unidade_estoque = 'UN' OR conteudo IS NULL)
);

CREATE TABLE lote (
    id SERIAL PRIMARY KEY,
    produto_id INTEGER NOT NULL REFERENCES produto (id) ON DELETE RESTRICT,
    fornecedor_id INTEGER NOT NULL REFERENCES fornecedor (id) ON DELETE RESTRICT,
    numero_lote VARCHAR(30) NOT NULL,
    validade DATE,
    custo_unitario NUMERIC(10, 4) NOT NULL CHECK (custo_unitario >= 0),
    data_entrada TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (produto_id, fornecedor_id, numero_lote)
);

CREATE TABLE contagem (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL REFERENCES usuario (id) ON DELETE RESTRICT,
    aberta_em TIMESTAMP NOT NULL DEFAULT NOW(),
    finalizada_em TIMESTAMP,
    status VARCHAR(10) NOT NULL DEFAULT 'ABERTA' CHECK (status IN ('ABERTA', 'FINALIZADA'))
);

-- Permite somente uma contagem aberta por vez.
CREATE UNIQUE INDEX uma_contagem_aberta ON contagem (status) WHERE status = 'ABERTA';

CREATE TABLE item_contagem (
    id SERIAL PRIMARY KEY,
    contagem_id INTEGER NOT NULL REFERENCES contagem (id) ON DELETE CASCADE,
    lote_id INTEGER NOT NULL REFERENCES lote (id) ON DELETE RESTRICT,
    saldo_sistema NUMERIC(12, 3) NOT NULL,
    saldo_contado NUMERIC(12, 3) CHECK (saldo_contado >= 0),
    conferido BOOLEAN NOT NULL DEFAULT FALSE,
    motivo VARCHAR(150),
    UNIQUE (contagem_id, lote_id)
);

CREATE TABLE movimentacao (
    id SERIAL PRIMARY KEY,
    lote_id INTEGER NOT NULL REFERENCES lote (id) ON DELETE RESTRICT,
    usuario_id INTEGER NOT NULL REFERENCES usuario (id) ON DELETE RESTRICT,
    contagem_id INTEGER REFERENCES contagem (id) ON DELETE RESTRICT,
    tipo VARCHAR(8) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA', 'PERDA', 'AJUSTE')),
    quantidade NUMERIC(12, 3) NOT NULL CHECK (quantidade <> 0),
    motivo_perda VARCHAR(12) CHECK (motivo_perda IN ('VENCIMENTO', 'AVARIA', 'OUTRO')),
    observacao VARCHAR(200),
    data_hora TIMESTAMP NOT NULL DEFAULT NOW(),
    CHECK ((tipo = 'PERDA') = (motivo_perda IS NOT NULL)),
    CHECK (tipo <> 'ENTRADA' OR quantidade > 0),
    CHECK (tipo NOT IN ('SAIDA', 'PERDA') OR quantidade < 0)
);

-- O saldo de cada lote é sempre a soma das suas movimentações.
CREATE VIEW vw_saldo_lote AS
SELECT l.id AS lote_id, l.produto_id, l.validade, l.custo_unitario,
       COALESCE(SUM(m.quantidade), 0) AS saldo
FROM lote l
LEFT JOIN movimentacao m ON m.lote_id = l.id
GROUP BY l.id;
