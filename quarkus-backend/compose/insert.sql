INSERT INTO basapp.customers(customer_id, first_name, last_name, gender, marital_status, birthday, country, address, city, email, phone_number, metadatas)
	VALUES (1, 'John', 'Doe', 'MALE', 'SINGLE', '1980-01-01 :12:00:00', 'US', '200 Central Park West, NY 10024', 'New York', 'johndoe@bas.com', '+1 212-769-5100', null),
	(2, 'Anne', 'Jean', 'FEMALE', 'MARRIED', '1993-07-11 :12:00:00', 'FR', '2 rue du chateau', 'Marseille', 'annej@bas.com', '+36 6 50 44 12 05', null),
	(3, 'Paul', 'Jean', 'MALE', 'MARRIED', '1992-04-11 :12:00:00', 'FR', '2 rue du chateau', 'Marseille', 'paulj@bas.com', '+36 6 50 44 12 05', null),
	(4, 'Sophie', 'Dupon', 'FEMALE', 'WIDOWED', '1977-7-14 :12:00:00', 'FR', '10 rue du louvre', 'Paris', 'Sodup@bas.com', '+33 6 50 60 12 05', null),
	(5, 'Andre', 'Martin', 'MALE', 'DIVORCED', '1989-07-22 :12:00:00', 'FR', '16 boulevard victor hugo', 'Nîmes', 'andre.martin@bas.com', '+33 6 50 44 12 05', null);
	
	INSERT INTO basapp.bank_accounts(
	account_id, type, balance)
	VALUES (1, 'CHECKING', 300),
	(2, 'CHECKING', 1600),
	(3, 'SAVING', 19200),
	(4, 'MMA', 55000);
	
	INSERT INTO basapp.bank_accounts_customers(
	account_id, customer_id)
	VALUES (1, 1),
	(2, 2),
	(2, 3),
	(3, 4),
	(4, 3);
	
INSERT INTO basapp.transactions (transaction_id, type, amount, status, date, label)
	VALUES (1, 'CREDIT', 1600, 'COMPLETED', '2022-06-06 :12:00:00', 'transaction 1'),
	(2, 'CREDIT', 350, 'COMPLETED', '2022-06-06 :12:30:00', 'transaction 2'),
	(3, 'DEBIT', 50, 'COMPLETED', '2022-07-06 :13:00:00', 'transaction 3'),
	(4, 'CREDIT', 10000, 'COMPLETED', '2022-07-06 :14:00:00', 'transaction 4'),
	(5, 'CREDIT', 55000, 'COMPLETED', '2022-07-08 :16:00:00', 'transaction 5'),
	(6, 'CREDIT', 9200, 'COMPLETED', '2022-07-10 :15:00:00', 'transaction 6'),
	(7, 'DEBIT', 400, 'REFUSED', '2022-10-06 :18:00:00', 'transaction 7'),
	(8, 'CREDIT', 1000, 'REFUSED', '2022-10-06 :18:10:00', 'transaction 8'),
	(9, 'CREDIT', 600, 'WAITING', '2022-11-06 :18:00:00', 'transaction 9'),
	(10, 'DEBIT', 200, 'WAITING', '2022-11-06 :18:30:00', 'transaction 10'),
	(11, 'CREDIT', 800, 'WAITING', '2022-12-06 :18:00:00', 'transaction 11'),
	(12, 'DEBIT', 300, 'WAITING', '2022-12-06 :19:00:00', 'transaction 12'),
	(13, 'DEBIT', 1200, 'WAITING', '2022-12-06 :19:00:10', 'transaction 13');
	
INSERT INTO basapp.bank_accounts_history(
	account_id, transaction_id)
	VALUES (2, 1),
	(1, 2),
	(1, 3),
	(3, 4),
	(4, 5),
	(3, 6),
	(1, 7),
	(3, 8);
	
	INSERT INTO basapp.bank_accounts_transactions(
	account_id, transaction_id)
	VALUES (2, 9),
	(1, 10),
	(3, 11),
	(1, 12),
	(4, 13);

