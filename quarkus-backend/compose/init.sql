
-- CREATE EXTENSION --
CREATE EXTENSION pgcrypto;

-- CREATE SCHEMA --
CREATE SCHEMA basapp;



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
	birthday timestamp without time zone NOT NULL,
	country varchar(255) NOT NULL,
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
	date timestamp without time zone NOT NULL,
	label text NOT NULL,
	metadatas jsonb,
	CONSTRAINT pk_transaction PRIMARY KEY (transaction_id)
	);
	
	-- CREATE bank_account_customer TABLE --
	CREATE TABLE basapp.bank_accounts_customers
	(
	account_id bigint NOT NULL,
	customer_id bigint NOT NULL,
	PRIMARY KEY (account_id, customer_id),
	CONSTRAINT fk_account_id FOREIGN KEY(account_id) REFERENCES basapp.bank_accounts(account_id),
	CONSTRAINT fk_customer_id FOREIGN KEY(customer_id) REFERENCES basapp.customers(customer_id)
	);

	-- CREATE bank_account_transactions TABLE --
	CREATE TABLE basapp.bank_accounts_transactions
	(
	account_id bigint NOT NULL,
	transaction_id bigint NOT NULL,
	PRIMARY KEY (account_id, transaction_id),
	CONSTRAINT fk_account_id FOREIGN KEY(account_id) REFERENCES basapp.bank_accounts(account_id),
	CONSTRAINT fk_transaction_id FOREIGN KEY(transaction_id) REFERENCES basapp.transactions(transaction_id)
	);

-- CREATE bank_account_history TABLE --
	CREATE TABLE basapp.bank_accounts_history
	(
	account_id bigint NOT NULL,
	transaction_id bigint NOT NULL,
	PRIMARY KEY (account_id, transaction_id),
	CONSTRAINT fk_account_id FOREIGN KEY(account_id) REFERENCES basapp.bank_accounts(account_id),
	CONSTRAINT fk_transaction_id FOREIGN KEY(transaction_id) REFERENCES basapp.transactions(transaction_id)
	);

-- GRANT basadm -- 
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA basapp TO basadm;	
GRANT SELECT, UPDATE, USAGE ON ALL SEQUENCES IN SCHEMA basapp to basadm;

