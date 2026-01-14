CREATE TABLE IF NOT EXISTS ware_houses (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    address_country VARCHAR(50) NOT NULL,
    address_city VARCHAR(50) NOT NULL,
    address_street VARCHAR(150) NOT NULL,
    address_house VARCHAR(50) NOT NULL,
    address_flat VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    fragile boolean,
    dimension_width double precision,
    dimension_height double precision,
    dimension_depth double precision,
    weight double precision
);

CREATE TABLE IF NOT EXISTS ware_house_products (
    ware_house_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity integer,
    FOREIGN KEY(ware_house_id) REFERENCES ware_houses(id) ON DELETE CASCADE,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT unique_key_ware_house_products UNIQUE(ware_house_id, product_id)
);

CREATE TABLE IF NOT EXISTS order_bookings (
    order_id UUID NOT NULL,
    ware_house_id UUID NOT NULL,
    delivery_id UUID,
    FOREIGN KEY(ware_house_id) REFERENCES ware_houses(id) ON DELETE CASCADE,
    CONSTRAINT unique_key_order_bookings UNIQUE(order_id, ware_house_id)
);

CREATE TABLE IF NOT EXISTS order_booking_products (
    order_id UUID NOT NULL,
    ware_house_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity integer,
    FOREIGN KEY(ware_house_id) REFERENCES ware_houses(id) ON DELETE CASCADE,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT unique_key_order_booking_products UNIQUE(order_id, ware_house_id, product_id)
);
