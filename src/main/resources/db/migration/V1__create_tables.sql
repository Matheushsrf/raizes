-- TABELA UNIDADES
CREATE TABLE unidades (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    ativa BOOLEAN NOT NULL DEFAULT true
);

-- TABELA PRODUTOS
CREATE TABLE produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco_base DECIMAL(10,2) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true,
    disponivel_junino BOOLEAN DEFAULT false
);

-- TABELA CLIENTES
CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    idade INTEGER,
    consentimento_lgpd BOOLEAN NOT NULL DEFAULT false,
    data_consentimento TIMESTAMP,
    criado_em TIMESTAMP NOT NULL
);

-- TABELA USUARIOS
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    unidade_id BIGINT REFERENCES unidades(id),
    ativo BOOLEAN NOT NULL DEFAULT true,
    criado_em TIMESTAMP NOT NULL
);

-- TABELA ESTOQUES
CREATE TABLE estoques (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    unidade_id BIGINT NOT NULL REFERENCES unidades(id),
    quantidade INTEGER NOT NULL DEFAULT 0,
    atualizado_em TIMESTAMP NOT NULL
);

-- TABELA PEDIDOS
CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    unidade_id BIGINT NOT NULL REFERENCES unidades(id),
    status VARCHAR(30) NOT NULL,
    canal_pedido VARCHAR(20) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(30),
    status_pagamento VARCHAR(30),
    criado_em TIMESTAMP NOT NULL
);

-- TABELA ITENS_PEDIDO
CREATE TABLE itens_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedidos(id),
    produto_id BIGINT NOT NULL REFERENCES produtos(id),
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL
);