CREATE SCHEMA IF NOT EXISTS shopping_store;

CREATE TABLE IF NOT EXISTS products (
    product_id VARCHAR(50) PRIMARY KEY,
--    product_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_name VARCHAR(150),
    description VARCHAR(1024),
    image_src VARCHAR(256),
    quantity_state VARCHAR(6),
    product_state VARCHAR(10),
    product_сategory VARCHAR(10),
    price NUMERIC(10, 2)
);