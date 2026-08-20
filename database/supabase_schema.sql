-- ============================================================
-- Canteen Management System - Supabase / PostgreSQL Schema
-- ============================================================

-- Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    phone      VARCHAR(15),
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);

-- User Roles Junction
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Categories Table
CREATE TABLE IF NOT EXISTS categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    image_url   VARCHAR(500),
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Menu Items Table
CREATE TABLE IF NOT EXISTS menu_items (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(150) NOT NULL,
    description      VARCHAR(1000),
    price            NUMERIC(10,2) NOT NULL,
    image_url        VARCHAR(500),
    category_id      BIGINT NOT NULL REFERENCES categories(id),
    available        BOOLEAN NOT NULL DEFAULT TRUE,
    vegetarian       BOOLEAN NOT NULL DEFAULT FALSE,
    rating           NUMERIC(3,2) DEFAULT 0.00,
    rating_count     INT DEFAULT 0,
    preparation_time INT DEFAULT 15,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_mi_category ON menu_items (category_id);
CREATE INDEX IF NOT EXISTS idx_mi_available ON menu_items (available);
CREATE INDEX IF NOT EXISTS idx_mi_price ON menu_items (price);

-- Shopping Cart Table
CREATE TABLE IF NOT EXISTS cart (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Cart Items Table
CREATE TABLE IF NOT EXISTS cart_items (
    id           BIGSERIAL PRIMARY KEY,
    cart_id      BIGINT NOT NULL REFERENCES cart(id) ON DELETE CASCADE,
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE,
    quantity     INT NOT NULL DEFAULT 1,
    added_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cart_item UNIQUE (cart_id, menu_item_id)
);

-- Coupons Table
CREATE TABLE IF NOT EXISTS coupons (
    id               BIGSERIAL PRIMARY KEY,
    code             VARCHAR(50) NOT NULL UNIQUE,
    description      VARCHAR(500),
    discount_percent NUMERIC(5,2) NOT NULL,
    max_discount     NUMERIC(10,2),
    min_order_amount NUMERIC(10,2) DEFAULT 0,
    expiry_date      DATE NOT NULL,
    usage_limit      INT,
    used_count       INT DEFAULT 0,
    active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_coupons_code ON coupons (code);

-- Orders Table
CREATE TABLE IF NOT EXISTS orders (
    id              BIGSERIAL PRIMARY KEY,
    order_number    VARCHAR(50) NOT NULL UNIQUE,
    user_id         BIGINT NOT NULL REFERENCES users(id),
    status          VARCHAR(50) NOT NULL DEFAULT 'PLACED',
    total_amount    NUMERIC(10,2) NOT NULL,
    discount_amount NUMERIC(10,2) DEFAULT 0.00,
    tax_amount      NUMERIC(10,2) DEFAULT 0.00,
    final_amount    NUMERIC(10,2) NOT NULL,
    coupon_code     VARCHAR(50),
    pickup_time     TIMESTAMP WITH TIME ZONE,
    notes           VARCHAR(500),
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders (user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders (status);
CREATE INDEX IF NOT EXISTS idx_orders_date ON orders (created_at);

-- Order Items Table
CREATE TABLE IF NOT EXISTS order_items (
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id),
    item_name    VARCHAR(150) NOT NULL,
    quantity     INT NOT NULL,
    unit_price   NUMERIC(10,2) NOT NULL,
    total_price  NUMERIC(10,2) NOT NULL
);

-- Payments Table
CREATE TABLE IF NOT EXISTS payments (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    amount          NUMERIC(10,2) NOT NULL,
    method          VARCHAR(50) NOT NULL DEFAULT 'CASH_ON_PICKUP',
    status          VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    transaction_ref VARCHAR(100),
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Kitchen Stock & Portion Inventory
CREATE TABLE IF NOT EXISTS inventory (
    id              BIGSERIAL PRIMARY KEY,
    menu_item_id    BIGINT NOT NULL UNIQUE REFERENCES menu_items(id) ON DELETE CASCADE,
    quantity        INT NOT NULL DEFAULT 0,
    unit            VARCHAR(50) DEFAULT 'pieces',
    min_stock_level INT DEFAULT 10,
    last_updated    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Stock Transaction History
CREATE TABLE IF NOT EXISTS inventory_transactions (
    id               BIGSERIAL PRIMARY KEY,
    inventory_id     BIGINT NOT NULL REFERENCES inventory(id),
    change_quantity  INT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    notes            VARCHAR(500),
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by       BIGINT
);

-- Reviews & Ratings Table
CREATE TABLE IF NOT EXISTS reviews (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE,
    rating       INT NOT NULL,
    comment      VARCHAR(1000),
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_item_review UNIQUE (user_id, menu_item_id)
);

-- Favorite Items Table
CREATE TABLE IF NOT EXISTS favorites (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id) ON DELETE CASCADE,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_item_fav UNIQUE (user_id, menu_item_id)
);

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title      VARCHAR(200) NOT NULL,
    message    VARCHAR(1000) NOT NULL,
    is_read    BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_notif_user_read ON notifications (user_id, is_read);

-- System Audit Log
CREATE TABLE IF NOT EXISTS audit_logs (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT,
    action        VARCHAR(200) NOT NULL,
    resource_type VARCHAR(100),
    resource_id   BIGINT,
    details       TEXT,
    ip_address    VARCHAR(50),
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_audit_user ON audit_logs (user_id);
CREATE INDEX IF NOT EXISTS idx_audit_action ON audit_logs (action);
CREATE INDEX IF NOT EXISTS idx_audit_date ON audit_logs (created_at);
