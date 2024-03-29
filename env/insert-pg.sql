-- DB SEQ ALTER
ALTER SEQUENCE basapp.customers_customer_id_seq RESTART WITH 10;
ALTER SEQUENCE basapp.bank_accounts_account_id_seq RESTART WITH 10;
ALTER SEQUENCE basapp.transactions_transaction_id_seq RESTART WITH 10;

INSERT INTO basapp.customers(customer_id, first_name, last_name, gender, marital_status, birthday, country, address, city, email, phone_number, metadata)
VALUES (1, 'John', 'Doe', 'M', 'S', '1980-01-01', 'US', '200 Central Park West, NY 10024', 'New York', 'johndoe@bas.com', '+1 212-769-5100', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "false"}'::jsonb),
       (2, 'Anne', 'Jean', 'F', 'M', '1993-07-11', 'FR', '2 rue du chateau', 'Marseille', 'annej@bas.com', '+36 6 50 44 12 05', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "false"}'::jsonb),
       (3, 'Paul', 'Jean', 'M', 'M', '1992-04-11', 'FR', '2 rue du chateau', 'Marseille', 'paulj@bas.com', '+36 6 50 44 12 05', '{"contact_preferences" : "email", "annual_salary" : "52000", "newsletter" : "false"}'::jsonb),
       (4, 'Sophie', 'Dupon', 'F', 'W', '1977-7-14', 'FR', '10 rue du louvre', 'Paris', 'Sodup@bas.com', '+33 6 50 60 12 05', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "true"}'::jsonb),
       (5, 'Andre', 'Martin', 'M', 'D', '1989-07-22', 'FR', '16 boulevard victor hugo', 'Nîmes', 'andre.martin@bas.com', '+33 6 50 44 12 05', '{"contact_preferences" : "phone", "annual_salary" : "52000", "newsletter" : "true"}'::jsonb),
       (6, 'Juan', 'Pedros', 'M', 'S', '1975-12-17', 'ES', 'Place de las Delicias', 'Sevilla', 'juanito@bas.com', '+34 9 20 55 62 05', '{"contact_preferences" : "phone", "annual_salary" : "200000", "newsletter" : "false"}'::jsonb);

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
       (7, 6),
       (8, 6);

INSERT INTO basapp.transactions (transaction_id, emitter_account_id, receiver_account_id, type, amount, currency, status, date, label, metadata)
VALUES (1, 1, 2, 'CREDIT', 1600.00, 'EUR', 'COMPLETED', '2024-06-06T12:00:00+01:00', 'transaction 1', '{"emitter_amount_before" : "2000", "receiver_amount_before" : "0", "emitter_amount_after" : "400", "receiver_amount_after" : "1600"}'::jsonb),
       (2, 6, 3, 'CREDIT', 9200.00, 'EUR', 'ERROR', '2024-07-10T15:00:00+01:00', 'transaction 2', '{"error" : "Transaction 2 deposit error for amount 9200 ..."}'::jsonb),
       (3, 6, 3, 'CREDIT', 9200.00, 'EUR', 'COMPLETED', '2024-07-10T15:00:00+01:00', 'transaction 3', '{"emitter_amount_before" : "9200", "receiver_amount_before" : "10000", "emitter_amount_after" : "0", "receiver_amount_after" : "19200"}'::jsonb),
       (4, 5, 1, 'CREDIT', 100000.00, 'EUR', 'REFUSED', '2024-07-10T15:00:00+01:00', 'transaction 4', '{"error" : "Transaction 4 deposit error for amount 100000 ..."}'::jsonb),
       (5, 2, 1, 'CREDIT', 600.99, 'EUR', 'UNPROCESSED', '2024-11-06 18:00:00+01:00', 'transaction 3', null::jsonb),
       (6, 1, 7, 'DEBIT', 2000.00, 'EUR', 'UNPROCESSED', '2024-11-06 18:30:00+01:00', 'transaction 4', null::jsonb),
       (7, 3, 1, 'CREDIT', 1000.00, 'EUR', 'UNPROCESSED', '2024-12-06 18:00:00+01:00', 'transaction 5', null::jsonb),
       (8, 4, 2, 'CREDIT', 300.80, 'EUR', 'UNPROCESSED', '2024-12-06 19:00:00+01:00', 'transaction 6', null::jsonb),
       (9, 8, 7, 'DEBIT', 5000.00, 'EUR', 'UNPROCESSED', '2024-12-06 19:00:10+01:00', 'transaction 7', null::jsonb);