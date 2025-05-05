CREATE TABLE IF NOT EXISTS delivery (
    delivery_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    from_address UUID NOT NULL REFERENCES address (address_id),
    to_address UUID NOT NULL REFERENCES address (address_id),
    order_id UUID NOT NULL,
    delivery_state TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS address (
    address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country TEXT,
    city TEXT,
    street TEXT,
    house TEXT,
    flat TEXT,
);