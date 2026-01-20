CREATE TABLE IF NOT EXISTS payments(
    id UUID PRIMARY KEY,
    order_id UUID,
    total_price double precision,
    delivery_price double precision,
    product_price double precision,
    state VARCHAR(10)
);