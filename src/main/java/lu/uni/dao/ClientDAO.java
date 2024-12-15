package lu.uni.dao;

import lu.uni.entities.client.Address;
import lu.uni.entities.client.BankAccount;
import lu.uni.entities.database.DatabaseConnection;
import lu.uni.entities.database.EncryptionUtil;
import lu.uni.entities.user.Client;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientDAO {

    public ClientDAO() {
    }

    private void rejectSQLKeywords(String input) throws IllegalArgumentException {
        // List of common SQL keywords to block
        String[] sqlKeywords = { "SELECT", "INSERT", "DELETE", "DROP", "UPDATE", "--", ";", "OR", "AND", "WHERE", "=", "'" };
    
        for (String keyword : sqlKeywords) {
            if (input != null && input.toUpperCase().contains(keyword)) {
                throw new IllegalArgumentException("Input contains forbidden SQL keyword: " + keyword);
            }
        }
    }

    public void addClient(Client client) throws SQLException {
        String insertAddressSQL = "INSERT INTO addresses (street_number, street, zip, country) VALUES (?, ?, ?, ?)";
        String insertBankAccountSQL = "INSERT INTO bank_accounts (bank_account_balance) VALUES (?)";
        String insertClientSQL = "INSERT INTO clients (name, birth_date, address_id, bank_account_id) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction
    
            int addressId = 0;
            int bankAccountId = 0;
    
            // Reject SQL keywords in the client's name
            rejectSQLKeywords(client.getName());
    
            // Insert address
            try (PreparedStatement addressStmt = connection.prepareStatement(insertAddressSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                validateClient(client);
    
                String encryptedStreetNumber = EncryptionUtil.encrypt(String.valueOf(client.getAddress().getStreetNumber()));
                String encryptedStreet = EncryptionUtil.encrypt(client.getAddress().getStreet());
                String encryptedZip = EncryptionUtil.encrypt(String.valueOf(client.getAddress().getZip()));
    
                addressStmt.setString(1, encryptedStreetNumber);
                addressStmt.setString(2, encryptedStreet);
                addressStmt.setString(3, encryptedZip);
                addressStmt.setString(4, client.getAddress().getCountry()); // Country remains plain
                addressStmt.executeUpdate();
    
                try (ResultSet generatedKeys = addressStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        addressId = generatedKeys.getInt(1); // Get generated address ID
                    } else {
                        throw new SQLException("Address insertion failed, no ID obtained.");
                    }
                }
            }
    
            // Insert bank account
            try (PreparedStatement bankAccountStmt = connection.prepareStatement(insertBankAccountSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                bankAccountStmt.setBigDecimal(1, client.getBankAccount().getBalance());
                bankAccountStmt.executeUpdate();
    
                try (ResultSet generatedKeys = bankAccountStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bankAccountId = generatedKeys.getInt(1); // Get generated bank account ID
                    } else {
                        throw new SQLException("Bank account insertion failed, no ID obtained.");
                    }
                }
            }
    
            // Insert client
            try (PreparedStatement clientStmt = connection.prepareStatement(insertClientSQL)) {
                String encryptedName = EncryptionUtil.encrypt(client.getName().toLowerCase()); // Normalize case before encrypting
    
                clientStmt.setString(1, encryptedName);
                clientStmt.setDate(2, client.getBirthDate());
                clientStmt.setInt(3, addressId); // Use generated address ID
                clientStmt.setInt(4, bankAccountId); // Use generated bank account ID
                clientStmt.executeUpdate();
            }
    
            connection.commit(); // Commit transaction
        } catch (IllegalArgumentException e) {
            throw new SQLException("Input validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error adding client with encrypted data", e);
        }
    }

    private void validateClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be null or empty");
        }
        if (client.getName().length() > 50) {
            throw new IllegalArgumentException("Client name exceeds maximum length");
        }
        validateAddress(client.getAddress());
        if (client.getBankAccount().getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Bank account balance cannot be negative");
        }
    }
    
    private void validateAddress(Address address) throws IllegalArgumentException {
        if (address.getStreetNumber() <= 0) {
            throw new IllegalArgumentException("Street number must be positive");
        }
        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            throw new IllegalArgumentException("Street name cannot be null or empty");
        }
        if (address.getZip() <= 0) {
            throw new IllegalArgumentException("Zip code must be positive");
        }
        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
    }    
    

    /*public Client getClientById(int clientId) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String encryptedName = resultSet.getString("name");
                    String decryptedName = EncryptionUtil.decrypt(encryptedName);

                    Date birthDate = resultSet.getDate("birth_date");
                    // Retrieve and decrypt other fields as necessary

                    return new Client(decryptedName, birthDate, null); // Adjust as needed
                }
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving client with decrypted data", e);
        }
        return null;
    }*/


    public Client getClientByNameAndBirthdate(String name, Date birthDate) throws SQLException {
        String sql = "SELECT c.id AS client_id, c.name, c.birth_date, " +
                     "a.id AS address_id, a.street_number, a.street, a.zip, a.country, " +
                     "b.id AS bank_account_id, b.bank_account_balance " +
                     "FROM clients c " +
                     "JOIN addresses a ON c.address_id = a.id " +
                     "JOIN bank_accounts b ON c.bank_account_id = b.id " +
                     "WHERE c.name = ? AND c.birth_date = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
    
            // Normalize the name and encrypt it before binding
            String encryptedName = EncryptionUtil.encrypt(name.toLowerCase());
            statement.setString(1, encryptedName);
            statement.setDate(2, birthDate);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Decrypt and construct the Client object
                    String decryptedName = EncryptionUtil.decrypt(resultSet.getString("name"));
                    Date retrievedBirthDate = resultSet.getDate("birth_date");
    
                    // Create Address object
                    Address address = new Address(
                        Integer.parseInt(EncryptionUtil.decrypt(resultSet.getString("street_number"))),
                        EncryptionUtil.decrypt(resultSet.getString("street")),
                        Integer.parseInt(EncryptionUtil.decrypt(resultSet.getString("zip"))),
                        resultSet.getString("country")
                    );
                    address.setId(resultSet.getInt("address_id")); // Set Address ID
    
                    // Create BankAccount object
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setId(resultSet.getInt("bank_account_id")); // Set BankAccount ID
                    bankAccount.setBalance(resultSet.getBigDecimal("bank_account_balance"));
    
                    // Create Client object
                    Client client = new Client(decryptedName, retrievedBirthDate, address);
                    client.setId(resultSet.getInt("client_id")); // Set Client ID
                    client.setBankAccount(bankAccount);
    
                    return client;
                }
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving client with encrypted data", e);
        }
        return null; // Return null if no match is found
    }
    


    public ArrayList<Client> getAllClients() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();
        String sql = "SELECT c.id AS client_id, c.name, c.birth_date, " +
                     "a.id AS address_id, a.street_number, a.street, a.zip, a.country, " +
                     "b.id AS bank_account_id, b.bank_account_balance " +
                     "FROM clients c " +
                     "JOIN addresses a ON c.address_id = a.id " +
                     "JOIN bank_accounts b ON c.bank_account_id = b.id";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                // Decrypt client data
                String decryptedName = EncryptionUtil.decrypt(resultSet.getString("name"));
                Date birthDate = resultSet.getDate("birth_date");
    
                // Decrypt address data
                String decryptedStreetNumber = EncryptionUtil.decrypt(resultSet.getString("street_number"));
                String decryptedStreet = EncryptionUtil.decrypt(resultSet.getString("street"));
                String decryptedZip = EncryptionUtil.decrypt(resultSet.getString("zip"));
                String country = resultSet.getString("country");
    
                // Set Address
                Address address = new Address(
                    Integer.parseInt(decryptedStreetNumber),
                    decryptedStreet,
                    Integer.parseInt(decryptedZip),
                    country
                );
                address.setId(resultSet.getInt("address_id")); // Properly set Address ID

                // Set BankAccount
                BankAccount bankAccount = new BankAccount();
                bankAccount.setId(resultSet.getInt("bank_account_id")); // Properly set BankAccount ID
                bankAccount.setBalance(resultSet.getBigDecimal("bank_account_balance"));
    
                // Create Client
                Client client = new Client(decryptedName, birthDate, address);
                client.setId(resultSet.getInt("client_id")); // Set Client ID
                client.setBankAccount(bankAccount);
    
                // Add to list
                clients.add(client);
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving all clients with decrypted data", e);
        }
    
        return clients;
    }    


    public void updateClient(Client client) throws SQLException {
        String updateAddressSQL = "UPDATE addresses SET street_number = ?, street = ?, zip = ?, country = ? WHERE id = ?";
        String updateBankAccountSQL = "UPDATE bank_accounts SET bank_account_balance = ? WHERE id = ?";
        String updateClientSQL = "UPDATE clients SET name = ?, birth_date = ?, address_id = ?, bank_account_id = ? WHERE id = ?";
    
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction
    
            // Validate input
            rejectSQLKeywords(client.getName());
            validateClient(client);
    
            // Update address
            try (PreparedStatement addressStmt = connection.prepareStatement(updateAddressSQL)) {
                Address address = client.getAddress();
                String encryptedStreetNumber = EncryptionUtil.encrypt(String.valueOf(address.getStreetNumber()));
                String encryptedStreet = EncryptionUtil.encrypt(address.getStreet());
                String encryptedZip = EncryptionUtil.encrypt(String.valueOf(address.getZip()));
    
                addressStmt.setString(1, encryptedStreetNumber);
                addressStmt.setString(2, encryptedStreet);
                addressStmt.setString(3, encryptedZip);
                addressStmt.setString(4, address.getCountry());
                addressStmt.setInt(5, address.getId());
                
                int addressUpdated = addressStmt.executeUpdate();
                if (addressUpdated == 0) {
                    throw new SQLException("Address update failed. No record found with the given ID.");
                }
            }
    
            // Update bank account
            try (PreparedStatement bankAccountStmt = connection.prepareStatement(updateBankAccountSQL)) {
                BankAccount bankAccount = client.getBankAccount();
    
                bankAccountStmt.setBigDecimal(1, bankAccount.getBalance());
                bankAccountStmt.setInt(2, bankAccount.getId());

                int bankAccountUpdated = bankAccountStmt.executeUpdate();
                if (bankAccountUpdated == 0) {
                    throw new SQLException("Bank account update failed. No record found with the given ID.");
                }
            }
            
            // Update client
            try (PreparedStatement clientStmt = connection.prepareStatement(updateClientSQL)) {
                String encryptedName = EncryptionUtil.encrypt(client.getName().toLowerCase());
    
                clientStmt.setString(1, encryptedName);
                clientStmt.setDate(2, client.getBirthDate());
                clientStmt.setInt(3, client.getAddress().getId());
                clientStmt.setInt(4, client.getBankAccount().getId());
                clientStmt.setInt(5, client.getId());
    
                int clientUpdated = clientStmt.executeUpdate();
                if (clientUpdated == 0) {
                    throw new SQLException("Client update failed. No record found with the given ID.");
                }
            }
    
            connection.commit(); // Commit transaction
        } catch (IllegalArgumentException e) {
            throw new SQLException("Input validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error updating client information", e);
        }
    }    
}