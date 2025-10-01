-- USERS
INSERT INTO users (name, email, password, role)
VALUES ('Mahdi salmanizadehgan', 'mahdisalmanizadehgan@gmail.com', '123456', 'ADMIN'),
       ('Reza Hosseini', 'rezahosseini1996@gmail.com', 'password123', 'USER'),
       ('Sajjad Tavakolizadeh', 'sajjadtavakolizadeh@gmail.com', '123456', 'USER');


-- ADDRESSES
INSERT INTO addresses (street, city, state, zip, user_id)
VALUES ('Piroozi', 'Tehran', 'Tehran No', '123456789', 1),
       ('Adab', 'Sanandaj', 'Sanandaj', '32446792', 2),
       ('Resalat', 'Tehran', 'Resalat', '54213421', 3);

-- CATEGORIES
INSERT INTO categories (name)
VALUES ('الکترونیکی'),
       ('کتاب'),
       ('لباس');

-- PRODUCTS
INSERT INTO products (name, price, description, category_id)
VALUES ('لپ تاپ', 1200.00, 'ایسوس ۱۵ اینچ', 1),
       ('هدفون', 199.99, 'هدفون بی سیم', 1),
       ('کتاب', 120.50, 'بوف کور صادق هدایت', 2),
       ('کتونی', 19.99, 'کتونی نایک', 3);

-- PROFILES
INSERT INTO profiles (id, bio, phone_number, dob)
VALUES (1, 'Java Developer', '09104328635', '1996-05-15'),
       (2, 'Bookworm', '09199131673', '1985-11-20'),
       (3, 'An eye for fashion', NULL, '2000-02-10');

