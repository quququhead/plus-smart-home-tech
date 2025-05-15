CREATE TABLE IF NOT EXISTS payment (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    total_payment DECIMAL(10, 2),
    delivery_total DECIMAL(10, 2),
    fee_total DECIMAL(10, 2),
    payment_state TEXT NOT NULL
);