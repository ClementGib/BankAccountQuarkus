INSERT INTO basapp.customers(customer_id, first_name, last_name, gender, marital_status, birthday, country, address, city, email, phone_number, metadata)
VALUES (1, 'John', 'Doe', 'MALE', 'SINGLE', '1980-01-01', 'US', '200 Central Park West, NY 10024', 'New York', 'johndoe@bas.com', '+1 212-769-5100', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "false"}'),
       (2, 'Anne', 'Jean', 'FEMALE', 'MARRIED', '1993-07-11', 'FR', '2 rue du chateau', 'Marseille', 'annej@bas.com', '+36 6 50 44 12 05', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "false"}'),
       (3, 'Paul', 'Jean', 'MALE', 'MARRIED', '1992-04-11', 'FR', '2 rue du chateau', 'Marseille', 'paulj@bas.com', '+36 6 50 44 12 05', '{"contact_preferences" : "email", "annual_salary" : "52000", "newsletter" : "false"}'),
       (4, 'Sophie', 'Dupon', 'FEMALE', 'WIDOWED', '1977-7-14', 'FR', '10 rue du louvre', 'Paris', 'Sodup@bas.com', '+33 6 50 60 12 05', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "true"}'),
       (5, 'Andre', 'Martin', 'MALE', 'DIVORCED', '1989-07-22', 'FR', '16 boulevard victor hugo', 'Nîmes', 'andre.martin@bas.com', '+33 6 50 44 12 05', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "true"}'),
       (6, 'Juan', 'Pedros', 'MALE', 'SINGLE', '1975-12-17', 'ES', 'Place de las Delicias', 'Sevilla', 'juanito@bas.com', '+34 9 20 55 62 05', '{"contact_preferences" : "phone", "annual_salary" : "200000", "newsletter" : "false"}');

INSERT INTO basapp.bank_accounts(account_id, type, balance)
VALUES (1, 'CHECKING', 400.00),
       (2, 'CHECKING', 1600.00),
       (3, 'SAVING', 19200.00),
       (4, 'CHECKING', 500.00),
       (5, 'MMA', 65000.00),
       (6, 'SAVING', 999.00),
       (7, 'CHECKING', 0.00),
       (8, 'SAVING', 200000.00);

INSERT INTO basapp.bank_accounts_customers(account_id, customer_id)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (3, 4),
       (4, 3),
       (5, 1),
       (6, 5),
       (7, 6);

INSERT INTO basapp.transactions (transaction_id, sender_account_id, receiver_account_id, type, amount, currency, status, date, label, metadata)
VALUES (1, 1, 2, 'CREDIT', 1600.00, 'EUR', 'COMPLETED', '2022-06-06 12:00:00', 'transaction 1', '{"sender_amount_before" : "2000", "receiver_amount_before" : "0", "sender_amount_after" : "400", "receiver_amount_after" : "1600"}'),
       (2, 6, 3, 'CREDIT', 9200.00, 'EUR', 'COMPLETED', '2022-07-10 15:00:00', 'transaction 2', '{"sender_amount_before" : "9200", "receiver_amount_before" : "10000", "sender_amount_after" : "0", "receiver_amount_after" : "19200"}'),
       (3, 2, 1, 'CREDIT', 600.99, 'EUR', 'UNPROCESSED', '2022-11-06 18:00:00', 'transaction 3', null),
       (4, 1, 7, 'DEBIT', 2000.00, 'EUR', 'UNPROCESSED', '2022-11-06 18:30:00', 'transaction 4', null),
       (5, 3, 1, 'CREDIT', 1000.00, 'EUR', 'UNPROCESSED', '2022-12-06 18:00:00', 'transaction 5', null),
       (6, 4, 2, 'CREDIT', 300.80, 'EUR', 'UNPROCESSED', '2022-12-06 19:00:00', 'transaction 6', null),
       (7, 8, 7, 'DEBIT', 5000.00, 'EUR', 'UNPROCESSED', '2022-12-06 19:00:10', 'transaction 7', null);

