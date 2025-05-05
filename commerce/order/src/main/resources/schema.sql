CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID NOT NULL,
    username TEXT,
    payment_id UUID,
    delivery_id UUID,
    state TEXT NOT NULL,
    delivery_weight DOUBLE PRECISION,
    delivery_volume DOUBLE PRECISION,
    fragile BOOLEAN,
    total_price DECIMAL(10, 2),
    delivery_price DECIMAL(10, 2),
    product_price DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS orders_product (
    order_id UUID NOT NULL REFERENCES orders (order_id),
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY(order_id, product_id)
);