CREATE TABLE IF NOT EXISTS product (
    product_id UUID PRIMARY KEY,
    fragile BOOLEAN NOT NULL,
    width DOUBLE PRECISION NOT NULL,
    height DOUBLE PRECISION NOT NULL,
    depth DOUBLE PRECISION NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    quantity BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_booking (
    order_booking_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    delivery_id UUID,
    fragile BOOLEAN,
    delivery_volume  DOUBLE PRECISION NOT NULL,
    delivery_weight  DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS order_booking_product (
    order_booking_id UUID REFERENCES order_booking (order_booking_id),
    product_id UUID REFERENCES product (product_id)
    quantity BIGINT NOT NULL,
    PRIMARY KEY(order_booking_id, product_id)
);