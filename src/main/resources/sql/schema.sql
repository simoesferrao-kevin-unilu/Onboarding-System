CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street_number INT NOT NULL,
    street VARCHAR(255) NOT NULL,
    zip INT NOT NULL,
    country VARCHAR(100) NOT NULL
);

CREATE TABLE bank_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bank_account_balance DECIMAL(15, 2) NOT NULL
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address_id INT,
    bank_account_id INT,
    FOREIGN KEY (id) REFERENCES users(id),
    FOREIGN KEY (address_id) REFERENCES addresses(id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    access_key VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE risk_scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    risk_score INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE transaction_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bank_account_id INT,
    log VARCHAR(255) NOT NULL,
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);