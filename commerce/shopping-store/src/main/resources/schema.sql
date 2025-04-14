CREATE SCHEMA IF NOT EXISTS shopping_store;

CREATE TABLE IF NOT EXISTS shopping_store.product (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_name TEXT NOT NULL,
    description TEXT NOT NULL,
    image_src TEXT,
    quantity_state TEXT NOT NULL,
    product_state TEXT NOT NULL,
    product_category TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);