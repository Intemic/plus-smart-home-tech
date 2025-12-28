CREATE TABLE IF NOT EXISTS ware_houses (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    address_country VARCHAR(50) NOT NULL,
    address_city VARCHAR(50) NOT NULL,
    address_street VARCHAR(150) NOT NULL,
    address_flat VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS products (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    fragile boolean,
    dimension_width double precision,
    dimension_height double precision,
    dimension_weight double precision,
    weight double precision
);

CREATE TABLE IF NOT EXISTS ware_house_products (
    ware_house_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity integer,
    FOREIGN KEY(ware_house_id) REFERENCES ware_houses(id) ON DELETE CASCADE,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT unique_key UNIQUE(ware_house_id, product_id)
)