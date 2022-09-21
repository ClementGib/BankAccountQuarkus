-- CREATE USERS --
CREATE USER basadm WITH PASSWORD 'postgres';

-- CREATE DATABASE --
CREATE DATABASE basdb;

-- SELECT DATABASE --
\c basdb

-- CREATE EXTENSION --
CREATE EXTENSION pgcrypto;

-- CREATE SCHEMA --
CREATE SCHEMA basapp;

-- GRANT USER --
GRANT ALL ON DATABASE basdb TO basadm;
GRANT ALL ON SCHEMA basapp TO basadm;


-- SET DEFAULT SCHEMA --
SET search_path TO basapp;

-- CREATE TABLES --

-- CREATE customer TABLE --
	CREATE TABLE basapp.customers
	(
	customer_id BIGSERIAL UNIQUE NOT NULL,
	first_name varchar(750) NOT NULL,
	last_name varchar(750) NOT NULL,
	gender varchar(25) NOT NULL,
	marital_status varchar(25) NOT NULL,
	birthday DATE NOT NULL,
	nationality varchar(255) NOT NULL,
	address text NOT NULL,
	city varchar(255) NOT NULL,
	email varchar(255) UNIQUE NOT NULL,
	phone_number varchar(20) NOT NULL,
	metadatas jsonb,
	CONSTRAINT pk_customer PRIMARY KEY (customer_id)
	);

-- CREATE bank_account TABLE --
	CREATE TABLE basapp.bank_accounts
	(
	account_id BIGSERIAL UNIQUE NOT NULL,
	type varchar(25) NOT NULL,
	balance DECIMAL NOT NULL,
	CONSTRAINT pk_bank_account PRIMARY KEY (account_id)
	);

-- CREATE transaction TABLE --
	CREATE TABLE basapp.transactions
	(
	transaction_id BIGSERIAL UNIQUE NOT NULL,
	type varchar(25) NOT NULL,
	amount DECIMAL NOT NULL,
    status varchar(25) NOT NULL,
	date DATE NOT NULL,
	label text NOT NULL,
	CONSTRAINT pk_transaction PRIMARY KEY (transaction_id)
	);
	
	-- CREATE bank_account_customer TABLE --
	CREATE TABLE basapp.bank_accounts_customers
	(
	account_id bigint NOT NULL,
	customer_id bigint NOT NULL,
	CONSTRAINT fk_account FOREIGN KEY(account_id) REFERENCES basapp.bank_accounts(account_id),
	CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES basapp.customers(customer_id)
	);

	-- CREATE bank_account_transactions TABLE --
	CREATE TABLE basapp.bank_accounts_transactions
	(
	account_id bigint NOT NULL,
	transaction_id bigint NOT NULL,
	CONSTRAINT fk_account FOREIGN KEY(account_id) REFERENCES basapp.bank_accounts(account_id),
	CONSTRAINT fk_transaction FOREIGN KEY(transaction_id) REFERENCES basapp.transactions(transaction_id)
	);

-- CREATE bank_account_history TABLE --
	CREATE TABLE basapp.bank_accounts_history
	(
	account_id bigint NOT NULL,
	transaction_id bigint NOT NULL,
	CONSTRAINT fk_account FOREIGN KEY(account_id) REFERENCES basapp.bank_accounts(account_id),
	CONSTRAINT fk_transaction FOREIGN KEY(transaction_id) REFERENCES basapp.transactions(transaction_id)
	);

-- GRANT basadm -- 
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA basapp TO basadm;	
GRANT SELECT, UPDATE, USAGE ON ALL SEQUENCES IN SCHEMA basapp to basadm;