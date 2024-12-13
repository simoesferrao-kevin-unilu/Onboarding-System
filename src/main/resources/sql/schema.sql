CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street_number VARCHAR(255) NOT NULL, -- Encrypted data
    street VARCHAR(255) NOT NULL,        -- Encrypted data
    zip VARCHAR(255) NOT NULL,           -- Encrypted data
    country VARCHAR(100) NOT NULL        -- Plaintext
);

CREATE TABLE bank_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bank_account_balance DECIMAL(15, 2) NOT NULL
);

CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,          -- Encrypted data
    birth_date DATE NOT NULL,
    address_id INT,
    bank_account_id INT,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL,
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id) ON DELETE SET NULL
);

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,          -- Plaintext for identification
    birth_date DATE NOT NULL,
    address_id INT,
    access_key VARCHAR(255) NOT NULL,    -- Hashed data
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL
);

CREATE TABLE risk_scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    risk_score INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

CREATE TABLE transaction_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bank_account_id INT NOT NULL,
    log VARCHAR(255) NOT NULL,
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id) ON DELETE CASCADE
);