CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY NOT NULL,
    user_name VARCHAR(30),
    cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR(20),
    delivery_weight double precision,
    delivery_volume double precision,
    fragile boolean,
    total_price double precision,
    delivery_price double precision,
    product_price double precision
);

CREATE TABLE IF NOT EXISTS orders_products (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity integer,
    FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT unique_keys_products UNIQUE(order_id, product_id)
);