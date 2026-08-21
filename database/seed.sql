-- ============================================================
-- Canteen Management System - Seed Data
-- Run AFTER schema.sql
-- ============================================================
USE canteen_db;

-- Roles
INSERT IGNORE INTO roles (name) VALUES ('CUSTOMER'), ('ADMIN'), ('STAFF');

-- Users (passwords are BCrypt-hashed versions of: admin123, staff123, customer123)
INSERT IGNORE INTO users (id, name, email, phone, password, enabled) VALUES
(1, 'Admin User',    'admin@canteen.com',    '9999999999', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4tbQJMLpkm', TRUE),
(2, 'Staff Member',  'staff@canteen.com',    '9888888888', '$2a$12$NQvjnBfCBkLKIEqxLJZoRO8KJhZKJRNbCBsLbGxYGMKMqGxR5bsQa', TRUE),
(3, 'Rahul Sharma',  'rahul@example.com',    '9876543210', '$2a$12$xzFKpHvh3.IhEjBBsC/AcOjFZpGU8h7O5kL6nEaXqMiX9tnRYfXuO', TRUE),
(4, 'Priya Patel',   'priya@example.com',    '9765432109', '$2a$12$xzFKpHvh3.IhEjBBsC/AcOjFZpGU8h7O5kL6nEaXqMiX9tnRYfXuO', TRUE),
(5, 'Amit Kumar',    'amit@example.com',     '9654321098', '$2a$12$xzFKpHvh3.IhEjBBsC/AcOjFZpGU8h7O5kL6nEaXqMiX9tnRYfXuO', TRUE);

-- Assign roles
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, (SELECT id FROM roles WHERE name='ADMIN')),
(1, (SELECT id FROM roles WHERE name='CUSTOMER')),
(2, (SELECT id FROM roles WHERE name='STAFF')),
(3, (SELECT id FROM roles WHERE name='CUSTOMER')),
(4, (SELECT id FROM roles WHERE name='CUSTOMER')),
(5, (SELECT id FROM roles WHERE name='CUSTOMER'));

-- Categories
INSERT IGNORE INTO categories (id, name, description, active) VALUES
(1, 'Breakfast',  'Start your day right with our nutritious breakfast options',       TRUE),
(2, 'Lunch',      'Hearty meals for your midday break',                               TRUE),
(3, 'Snacks',     'Light bites to keep you energized',                               TRUE),
(4, 'Beverages',  'Refreshing drinks, hot and cold',                                 TRUE),
(5, 'Specials',   'Chef specials and seasonal favorites',                             TRUE);

-- Menu Items (from original project + expanded)
INSERT IGNORE INTO menu_items (id, name, description, price, category_id, available, vegetarian, rating, rating_count, preparation_time, image_url) VALUES
(1,  'Masala Dosa',     'Crispy rice crepe filled with spiced potato masala, served with sambar and chutney',  80.00, 1, TRUE, TRUE,  4.5, 120, 10, 'https://images.unsplash.com/photo-1668236543090-82eba5ee5976?auto=format&fit=crop&w=600&q=80'),
(2,  'Idli Sambar',     'Soft steamed rice cakes served with piping hot sambar and fresh coconut chutney',    40.00, 1, TRUE, TRUE,  4.3, 95,  8,  'https://images.unsplash.com/photo-1589301760014-d929f3979dbc?auto=format&fit=crop&w=600&q=80'),
(3,  'Medu Vada',       'Crispy lentil doughnuts served with sambar and chutney',                             30.00, 1, TRUE, TRUE,  4.2, 80,  8,  'https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?auto=format&fit=crop&w=600&q=80'),
(4,  'Upma',            'Savory semolina porridge with vegetables and spices',                                 45.00, 1, TRUE, TRUE,  4.0, 60,  10, 'https://images.unsplash.com/photo-1645177628172-a94c1f96e6db?auto=format&fit=crop&w=600&q=80'),
(5,  'Kesari Bath',     'Sweet saffron-flavored semolina pudding with ghee',                                  35.00, 1, TRUE, TRUE,  4.4, 75,  8,  '/images/kesari-bath.png'),
(6,  'Pulao',           'Fragrant basmati rice cooked with mixed vegetables and aromatic spices',             90.00, 2, TRUE, TRUE,  4.1, 55,  15, 'https://images.unsplash.com/photo-1633945274405-b6c8069047b0?auto=format&fit=crop&w=600&q=80'),
(7,  'Dal Tadka',       'Yellow lentils tempered with cumin, garlic and spices, served with rice',           75.00, 2, TRUE, TRUE,  4.3, 65,  15, 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=600&q=80'),
(8,  'Paneer Butter Masala', 'Cottage cheese in rich tomato-butter gravy, best with naan',                  130.00, 2, TRUE, TRUE,  4.7, 110, 20, 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=600&q=80'),
(9,  'Chicken Biryani', 'Aromatic basmati rice cooked with tender chicken and whole spices',                160.00, 2, TRUE, FALSE, 4.8, 200, 25, 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&w=600&q=80'),
(10, 'Egg Fried Rice',  'Wok-tossed rice with scrambled eggs, vegetables and soy sauce',                    100.00, 2, TRUE, FALSE, 4.2, 90,  15, 'https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=600&q=80'),
(11, 'Samosa',          'Crispy pastry filled with spiced potatoes and peas, served with mint chutney',      20.00, 3, TRUE, TRUE,  4.5, 180, 5,  'https://images.unsplash.com/photo-1601050690597-df0568f70950?auto=format&fit=crop&w=600&q=80'),
(12, 'Bread Pakoda',    'Bread slices dipped in spiced chickpea batter and deep fried',                      25.00, 3, TRUE, TRUE,  4.1, 70,  5,  'https://images.unsplash.com/photo-1606491956689-2ea866880c84?auto=format&fit=crop&w=600&q=80'),
(13, 'Poha',            'Flattened rice cooked with onions, mustard seeds, and curry leaves',                30.00, 3, TRUE, TRUE,  4.0, 55,  8,  'https://images.unsplash.com/photo-1610057099443-fde8c4d50f91?auto=format&fit=crop&w=600&q=80'),
(14, 'Masala Chai',     'Traditional Indian spiced tea with ginger, cardamom and milk',                      20.00, 4, TRUE, TRUE,  4.6, 250, 3,  'https://images.unsplash.com/photo-1576092768241-dec231879fc3?auto=format&fit=crop&w=600&q=80'),
(15, 'Cold Coffee',     'Blended iced coffee with milk and sugar',                                           60.00, 4, TRUE, TRUE,  4.4, 130, 5,  'https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?auto=format&fit=crop&w=600&q=80'),
(16, 'Fresh Lime Soda', 'Freshly squeezed lime with soda water, sweet or salted',                            30.00, 4, TRUE, TRUE,  4.3, 95,  3,  'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=600&q=80'),
(17, 'Mango Lassi',     'Chilled yogurt drink blended with fresh mango pulp',                               55.00, 4, TRUE, TRUE,  4.7, 160, 5,  '/images/mango-lassi.jpg'),
(18, 'Thali Special',   'Complete meal with dal, sabzi, rice, roti, salad and dessert',                     150.00, 5, TRUE, TRUE,  4.9, 85,  20, 'https://images.unsplash.com/photo-1589302168068-964664d93dc0?auto=format&fit=crop&w=600&q=80'),
(19, 'Chef Special Pasta','Penne pasta in homemade tomato basil sauce with herbs',                           120.00, 5, TRUE, TRUE,  4.2, 45,  20, 'https://images.unsplash.com/photo-1551183053-bf91a1d81141?auto=format&fit=crop&w=600&q=80'),
(20, 'Chaat Platter',   'Assorted street food bites: pani puri, bhel puri and sev puri',                    70.00, 5, TRUE, TRUE,  4.6, 100, 10, 'https://images.unsplash.com/photo-1626132647523-66f5bf380027?auto=format&fit=crop&w=600&q=80');

-- Inventory
INSERT IGNORE INTO inventory (menu_item_id, quantity, unit, min_stock_level) VALUES
(1,  100, 'plates',  20),
(2,  150, 'plates',  30),
(3,  120, 'pieces',  25),
(4,  80,  'plates',  15),
(5,  90,  'plates',  15),
(6,  60,  'plates',  10),
(7,  70,  'plates',  10),
(8,  50,  'plates',  10),
(9,  40,  'plates',  8),
(10, 60,  'plates',  10),
(11, 200, 'pieces',  40),
(12, 150, 'pieces',  30),
(13, 90,  'plates',  15),
(14, 300, 'cups',    50),
(15, 100, 'glasses', 20),
(16, 120, 'glasses', 20),
(17, 80,  'glasses', 15),
(18, 30,  'plates',  8),
(19, 40,  'plates',  8),
(20, 60,  'plates',  12);

-- Coupons
INSERT IGNORE INTO coupons (code, description, discount_percent, max_discount, min_order_amount, expiry_date, usage_limit, active) VALUES
('WELCOME10', 'Welcome discount for new users',             10.00, 50.00,  100.00, '2027-12-31', 1000, TRUE),
('SAVE20',    '20% off on orders above Rs.200',             20.00, 80.00,  200.00, '2027-06-30', 500,  TRUE),
('LUNCH15',   '15% off on lunch orders',                    15.00, 60.00,  150.00, '2027-12-31', NULL, TRUE),
('SPECIAL50', 'Flat 50% off - limited time offer',          50.00, 100.00, 300.00, '2026-12-31', 100,  TRUE);

SELECT 'Seed data inserted successfully!' AS Status;
