---

![modelo-coffee-java](https://github.com/Elias-Manica/coffee-java/assets/103606213/20afab06-c7c1-43e5-a390-d173a4908daa)

---

CREATE DATABASE coffee-java;

-- Criação da tabela de usuários para login

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- Criação da tabela de produtos

CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL
);

-- Criação da tabela de estoque

CREATE TABLE estoque (
    id SERIAL PRIMARY KEY,
    produto_id INT REFERENCES produtos(id) ON DELETE CASCADE,
    quantidade INT NOT NULL DEFAULT 0
);

-- Criação da tabela de vendas

CREATE TABLE vendas (
    id SERIAL PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2) NOT NULL
);

-- Criação da tabela de itens de vendas

CREATE TABLE itens_venda (
    id SERIAL PRIMARY KEY,
    venda_id INT REFERENCES vendas(id) ON DELETE CASCADE,
    produto_id INT REFERENCES produtos(id) ON DELETE CASCADE,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL
);

-- Insere um usuário de exemplo (senha deve ser armazenada como um hash)

INSERT INTO usuarios (email, senha) VALUES ('admin@cafeimaginario.com', 'senha123');

-- Inserir produtos de exemplo

INSERT INTO produtos (nome, categoria, preco) VALUES 
('Café Expresso', 'Bebida', 4.50),
('Cappuccino', 'Bebida', 6.00),
('Croissant', 'Comida', 3.50);

-- Inserir estoque inicial de produtos

INSERT INTO estoque (produto_id, quantidade) VALUES 
(1, 100), -- 100 unidades de Café Expresso
(2, 50),  -- 50 unidades de Cappuccino
(3, 30);  -- 30 unidades de Croissant

-- Função para registrar uma venda

```
CREATE OR REPLACE FUNCTION registrar_venda(produtos_vendidos JSON) RETURNS VOID AS $$
DECLARE
    nova_venda_id INT;
    produto RECORD;
BEGIN
    -- Inserir uma nova venda
    INSERT INTO vendas (total) VALUES (0) RETURNING id INTO nova_venda_id;

    -- Iterar sobre os produtos vendidos e inserir na tabela itens_venda
    FOR produto IN SELECT * FROM json_to_recordset(produtos_vendidos) AS (produto_id INT, quantidade INT, preco DECIMAL(10,2)) LOOP
        INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario) 
        VALUES (nova_venda_id, produto.produto_id, produto.quantidade, produto.preco);

        -- Atualizar o estoque
        UPDATE estoque SET quantidade = quantidade - produto.quantidade WHERE produto_id = produto.produto_id;

        -- Atualizar o total da venda
        UPDATE vendas SET total = total + (produto.quantidade * produto.preco) WHERE id = nova_venda_id;
    END LOOP;
END;
$$ LANGUAGE plpgsql;
```

