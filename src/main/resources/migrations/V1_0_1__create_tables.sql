CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE zakaz (
                       zakaz_id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
                       user_name VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(20) NOT NULL,
                       product_name VARCHAR(255) NOT NULL,
                       product_id INT NOT NULL,
                       price DOUBLE PRECISION NOT NULL,
                       quantity INT NOT NULL,
                       office_number INT NOT NULL,
                       utm_source VARCHAR(255),
                       utm_medium VARCHAR(255),
                       utm_term VARCHAR(255),
                       utm_content VARCHAR(255),
                       utm_campaign VARCHAR(255),
                       order_date_time TIMESTAMP NOT NULL
);
