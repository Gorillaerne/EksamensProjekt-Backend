-- -----------------------------
-- USERS
-- -----------------------------
INSERT INTO users (id, username, email, password, role)
VALUES
    (1, 'admin', 'admin@example.com',
     '1234', 'ADMIN'),

    (2, 'user', 'user@example.com',
     '1234', 'USER');


-- -----------------------------
-- PRODUCTS
-- -----------------------------
INSERT INTO product (id, name, description, sku, price, picture)
VALUES
    (1, 'Keyboard X200', 'Mechanical keyboard with RGB', 'SKU-001', 799.00, 'pic1.jpg'),
    (2, 'Gaming Mouse G500', 'High precision gaming mouse', 'SKU-002', 499.00, 'pic2.jpg'),
    (3, '4K Monitor 27"', 'Ultra HD IPS display', 'SKU-003', 2499.00, 'pic3.jpg');


-- -----------------------------
-- WAREHOUSE
-- -----------------------------
INSERT INTO warehouse (id, name, address, description)
VALUES
    (1, 'Main Warehouse', '1234 Storage Road', 'Primary storage location'),
    (2, 'Backup Warehouse', '56 Warehouse Blvd', 'Secondary location');


-- -----------------------------
-- WAREHOUSE_PRODUCT (many-to-many)
-- -----------------------------
INSERT INTO warehouse_product (warehouse_id, product_id, quantity)
VALUES
    (1, 1, 100),
    (1, 2, 50),
    (1, 3, 20),
    (2, 2, 70),
    (2, 3, 15);


-- -----------------------------
-- LOGS
-- -----------------------------
INSERT INTO logs (id, action, time_stamp, productid, userid)
VALUES
    (1, 'CREATED_PRODUCT', CURRENT_TIMESTAMP(), 1, 1),
    (2, 'UPDATED_STOCK', CURRENT_TIMESTAMP(), 2, 1),
    (3, 'VIEWED_PRODUCT', CURRENT_TIMESTAMP(), 3, 2);
