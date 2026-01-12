CREATE TABLE IF NOT EXISTS deliverys(
    id UUID PRIMARY KEY,
    order_id UUID,
    volume double precision,
    weight double precision,
    fragile boolean,
    from_country VARCHAR(50),
    from_city    VARCHAR(50),
    from_street  VARCHAR(100),
    from_house   VARCHAR(10),
    from_flat    VARCHAR(10),
    to_country VARCHAR(50),
    to_city    VARCHAR(50),
    to_street  VARCHAR(100),
    to_house   VARCHAR(10),
    to_flat    VARCHAR(10),
    state VARCHAR(15)
);