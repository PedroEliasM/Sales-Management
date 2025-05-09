### Script de criação do DB

-- Criação do schema
CREATE DATABASE IF NOT EXISTS sales_management;
USE sales_management;

-- Criação da Tabela Usuario
CREATE TABLE Usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    tipo_usuario ENUM('admin', 'funcionario', 'cliente') NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefone VARCHAR(20),
    senha VARCHAR(255),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criação da Tabela Produto
CREATE TABLE Produto (
    id_produto INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    descricao TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_funcionario_cadastro INT NOT NULL,
    FOREIGN KEY (id_funcionario_cadastro) REFERENCES Usuario(id_usuario)
);

-- Criação da Tabela Estoque
CREATE TABLE Estoque (
    id_estoque INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL UNIQUE,
    quantidade INT NOT NULL DEFAULT 0,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
);

-- Criação da Tabela Venda
CREATE TABLE Venda (
    id_venda INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Usuario(id_usuario)
);

-- Criação da Tabela ItemVenda (para vincular produtos a vendas)
CREATE TABLE ItemVenda (
    id_item_venda INT PRIMARY KEY AUTO_INCREMENT,
    id_venda INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade_vendida INT NOT NULL DEFAULT 1,
    valor_unitario_na_venda DECIMAL(10, 2) NOT NULL,
    valor_total_item DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES Venda(id_venda),
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
);

-- Índices para otimizar consultas
CREATE INDEX idx_funcionario_cadastro ON Produto (id_funcionario_cadastro);
CREATE INDEX idx_produto ON Estoque (id_produto);
CREATE INDEX idx_cliente ON Venda (id_cliente);
CREATE INDEX idx_venda ON ItemVenda (id_venda);
CREATE INDEX idx_produto_item_venda ON ItemVenda (id_produto);

-- Inserir um usuário administrador inicial
INSERT INTO Usuario (nome, sobrenome, idade, cidade, tipo_usuario, email, senha)
VALUES ('Admin', 'Sistema', 30, 'São Paulo', 'admin', 'admin@sistema.com', '$2a$10$xwWMu/U3N4DpYJ57UZx8xO1qtLME00jS8/n1b/dLWjxpbGg6Dkjsu');
-- Senha: admin123 (já no formato bcrypt)

-- Inserir um usuário funcionário inicial
INSERT INTO Usuario (nome, sobrenome, idade, cidade, tipo_usuario, email, senha)
VALUES ('Funcionario', 'Teste', 25, 'Rio de Janeiro', 'funcionario', 'funcionario@sistema.com', '$2a$10$bC9k9wXTJ1dF2HHyM7XQg.RBtN6vvEFMsO5gYwfZYmQPNVezFDxlG');
-- Senha: func123 (já no formato bcrypt)

-- Inserir um cliente inicial
INSERT INTO Usuario (nome, sobrenome, idade, cidade, tipo_usuario, email, senha)
VALUES ('Cliente', 'Teste', 28, 'Belo Horizonte', 'cliente', 'cliente@email.com', '$2a$10$AacK0.WxMF21jPXIJWIxPOZd4gjdvLfnQ6hX2.OE9G9hXjc/d.n6K');
-- Senha: cliente123 (já no formato bcrypt)