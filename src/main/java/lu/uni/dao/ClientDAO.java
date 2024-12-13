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


    public void addClient(Client client) throws SQLException {
        String insertAddressSQL = "INSERT INTO addresses (street_number, street, zip, country) VALUES (?, ?, ?, ?)";
        String insertBankAccountSQL = "INSERT INTO bank_accounts (bank_account_balance) VALUES (?)";
        String insertClientSQL = "INSERT INTO clients (name, birth_date, address_id, bank_account_id) VALUES (?, ?, ?, ?)";
    
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction
    
            int addressId = 0;
            int bankAccountId = 0;
    
            // Insert address
            try (PreparedStatement addressStmt = connection.prepareStatement(insertAddressSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
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
                String encryptedName = EncryptionUtil.encrypt(client.getName());
    
                clientStmt.setString(1, encryptedName);
                clientStmt.setDate(2, client.getBirthDate());
                clientStmt.setInt(3, addressId); // Use generated address ID
                clientStmt.setInt(4, bankAccountId); // Use generated bank account ID
                clientStmt.executeUpdate();
            }
    
            connection.commit(); // Commit transaction
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error adding client with encrypted data", e);
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
        String sql = "SELECT c.name, c.birth_date, a.street_number, a.street, a.zip, a.country, b.bank_account_balance " +
                    "FROM clients c " +
                    "JOIN addresses a ON c.address_id = a.id " +
                    "JOIN bank_accounts b ON c.bank_account_id = b.id " +
                    "WHERE c.name = ? AND c.birth_date = ?";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            String encryptedName = EncryptionUtil.encrypt(name);
            statement.setString(1, encryptedName);
            statement.setDate(2, birthDate);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String decryptedName = EncryptionUtil.decrypt(resultSet.getString("name"));
                    Date retrievedBirthDate = resultSet.getDate("birth_date");

                    String decryptedStreetNumber = EncryptionUtil.decrypt(resultSet.getString("street_number"));
                    String decryptedStreet = EncryptionUtil.decrypt(resultSet.getString("street"));
                    String decryptedZip = EncryptionUtil.decrypt(resultSet.getString("zip"));
                    String country = resultSet.getString("country");
                    BigDecimal balance = resultSet.getBigDecimal("bank_account_balance");

                    Address address = new Address(Integer.parseInt(decryptedStreetNumber), decryptedStreet, Integer.parseInt(decryptedZip), country);
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setBalance(balance);

                    Client newClient = new Client(decryptedName, retrievedBirthDate, address);
                    newClient.setBankAccount(bankAccount);

                    return newClient;
                }
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving client with encrypted data", e);
        }
        return null;
    }


    public ArrayList<Client> getAllClients() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();
        String sql = "SELECT c.name, c.birth_date, a.street_number, a.street, a.zip, a.country, b.bank_account_balance " +
                     "FROM clients c " +
                     "JOIN addresses a ON c.address_id = a.id " +
                     "JOIN bank_accounts b ON c.bank_account_id = b.id";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                String decryptedName = EncryptionUtil.decrypt(resultSet.getString("name"));
                Date birthDate = resultSet.getDate("birth_date");
    
                String decryptedStreetNumber = EncryptionUtil.decrypt(resultSet.getString("street_number"));
                String decryptedStreet = EncryptionUtil.decrypt(resultSet.getString("street"));
                String decryptedZip = EncryptionUtil.decrypt(resultSet.getString("zip"));
                String country = resultSet.getString("country");
                BigDecimal balance = resultSet.getBigDecimal("bank_account_balance");
    
                Address address = new Address(Integer.parseInt(decryptedStreetNumber), decryptedStreet, Integer.parseInt(decryptedZip), country);
                BankAccount bankAccount = new BankAccount();
                bankAccount.setBalance(balance);

                Client newClient = new Client(decryptedName, birthDate, address);
                newClient.setBankAccount(bankAccount);
    
                clients.add(newClient);
            }
        } catch (Exception e) {
            throw new SQLException("Error retrieving all clients with decrypted data", e);
        }
    
        return clients;
    }


    public void updateClient(Client client) throws SQLException {
        String updateAddressSQL = "UPDATE addresses SET street_number = ?, street = ?, zip = ?, country = ? WHERE id = ?";
        String updateClientSQL = "UPDATE clients SET name = ?, birth_date = ? WHERE id = ?";
    
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
    
            // Update address
            try (PreparedStatement addressStmt = connection.prepareStatement(updateAddressSQL)) {
                String encryptedStreetNumber = EncryptionUtil.encrypt(String.valueOf(client.getAddress().getStreetNumber()));
                String encryptedStreet = EncryptionUtil.encrypt(client.getAddress().getStreet());
                String encryptedZip = EncryptionUtil.encrypt(String.valueOf(client.getAddress().getZip()));
    
                addressStmt.setString(1, encryptedStreetNumber);
                addressStmt.setString(2, encryptedStreet);
                addressStmt.setString(3, encryptedZip);
                addressStmt.setString(4, client.getAddress().getCountry());
                addressStmt.setInt(5, client.getAddress().getId());
                addressStmt.executeUpdate();
            }
    
            // Update client
            try (PreparedStatement clientStmt = connection.prepareStatement(updateClientSQL)) {
                String encryptedName = EncryptionUtil.encrypt(client.getName());
    
                clientStmt.setString(1, encryptedName);
                clientStmt.setDate(2, client.getBirthDate());
                clientStmt.setInt(3, client.getId());
                clientStmt.executeUpdate();
            }
    
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Error updating client with encrypted data", e);
        }
    }
}