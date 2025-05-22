INSERT INTO users (email, password, first_name, last_name, role) VALUES
('admin@bank.com', '$2a$10$xJwL5v5Jz5UZJQZJQZJQZOJQZJQZJQZJQZJQZJQZJQZJQZJQZJQZ', 'Admin', 'Bankov', 'ADMIN'),
('user1@mail.ru', '$2a$10$xJwL5v5Jz5UZJQZJQZJQZOJQZJQZJQZJQZJQZJQZJQZJQZJQZJQZ', 'Иван', 'Петров', 'USER'),
('user2@mail.ru', '$2a$10$xJwL5v5Jz5UZJQZJQZJQZOJQZJQZJQZJQZJQZJQZJQZJQZJQZJQZ', 'Мария', 'Сидорова', 'USER');

INSERT INTO cards (card_number, card_holder_name, expiration_date, status, balance, user_id) VALUES
('4111111111111111', 'IVAN PETROV', '2025-12-31', 'ACTIVE', 100000.00, 2),
('5555555555554444', 'IVAN PETROV', '2026-06-30', 'ACTIVE', 50000.00, 2),
('378282246310005', 'MARIA SIDOROVA', '2024-09-30', 'ACTIVE', 75000.00, 3),
('6011111111111117', 'MARIA SIDOROVA', '2025-03-31', 'BLOCKED', 0.00, 3);

INSERT INTO transactions (source_card_id, target_card_id, amount, transaction_date, description) VALUES
(1, 3, 5000.00, '2024-05-01 10:30:00', 'Перевод между счетами'),
(3, 2, 10000.00, '2024-05-02 14:15:00', 'Оплата услуг'),
(2, 1, 2000.00, '2024-05-03 09:45:00', 'Возврат долга'),
(1, 2, 1500.00, '2024-05-04 16:20:00', 'Покупка в магазине');