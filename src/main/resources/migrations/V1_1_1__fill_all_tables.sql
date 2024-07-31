create extension if not exists pgcrypto;
--create function for getting data from bounded tables
CREATE OR REPLACE FUNCTION get_id_from_table(element_number integer, table_name regclass,column_name varchar(20))
    RETURNS uuid AS $$
DECLARE result_id uuid;
BEGIN
    EXECUTE format('SELECT (SELECT %s FROM %s LIMIT 1 OFFSET $1)::uuid',  column_name ,table_name)
        INTO result_id
        USING element_number;

    RETURN result_id;
END;
$$ LANGUAGE plpgsql IMMUTABLE;
INSERT INTO zakaz (user_name, phone_number, product_name, product_id, price, quantity, office_number, utm_source, utm_medium, utm_term, utm_content, utm_campaign, order_date_time)

VALUES
    ('John Doe', '1234567890', 'Product 1', 1, 100.0, 1, 1, 'google', 'cpc', 'term1', 'content1', 'campaign1', NOW()),
    ('Jane Smith', '0987654321', 'Product 2', 2, 200.0, 2, 2, 'facebook', 'cpc', 'term2', 'content2', 'campaign2', NOW()),
    ('Alice Johnson', '1111111111', 'Product 3', 3, 150.0, 3, 3, 'twitter', 'cpc', 'term3', 'content3', 'campaign3', NOW()),
    ('Bob Brown', '2222222222', 'Product 4', 4, 250.0, 4, 4, 'instagram', 'cpc', 'term4', 'content4', 'campaign4', NOW()),
    ('Charlie Black', '3333333333', 'Product 5', 5, 300.0, 5, 5, 'linkedin', 'cpc', 'term5', 'content5', 'campaign5', NOW()),
    ('David White', '4444444444', 'Product 6', 6, 350.0, 6, 6, 'bing', 'cpc', 'term6', 'content6', 'campaign6', NOW()),
    ('Eva Green', '5555555555', 'Product 7', 7, 400.0, 7, 7, 'yahoo', 'cpc', 'term7', 'content7', 'campaign7', NOW()),
    ('Frank Blue', '6666666666', 'Product 8', 8, 450.0, 8, 8, 'duckduckgo', 'cpc', 'term8', 'content8', 'campaign8', NOW()),
    ('Grace Red', '7777777777', 'Product 9', 9, 500.0, 9, 9, 'baidu', 'cpc', 'term9', 'content9', 'campaign9', NOW()),
    ('Henry Purple', '8888888888', 'Product 10', 10, 550.0, 10, 10, 'yandex', 'cpc', 'term10', 'content10', 'campaign10', NOW());
