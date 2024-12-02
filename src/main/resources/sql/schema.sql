CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street_number INT NOT NULL,
    street VARCHAR(255) NOT NULL,
    zip INT NOT NULL,
    country VARCHAR(100) NOT NULL
);

CREATE TABLE bank_accounts (
    id VARCHAR(50) PRIMARY KEY,
    bank_account_balance DECIMAL(15, 2) NOT NULL
);

CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE clients (
    id VARCHAR(50) PRIMARY KEY,
    address_id INT,
    bank_account_id VARCHAR(50),
    FOREIGN KEY (id) REFERENCES users(id),
    FOREIGN KEY (address_id) REFERENCES addresses(id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);

CREATE TABLE employees (
    id VARCHAR(50) PRIMARY KEY,
    access_key VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE risk_scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(50),
    risk_score INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE transaction_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bank_account_id VARCHAR(50),
    log VARCHAR(255) NOT NULL,
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);