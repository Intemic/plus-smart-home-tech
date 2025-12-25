CREATE TABLE IF NOT EXISTS carts {
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE
};

CREATE TABLE IN NOT EXISTS products {
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity integer,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT unique_keys_products UNIQUE(cart_id, product_id)
};