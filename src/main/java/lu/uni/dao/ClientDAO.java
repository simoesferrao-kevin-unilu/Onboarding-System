package lu.uni.dao;

import lu.uni.entities.database.DatabaseConnection;
import lu.uni.entities.user.Client;

import java.sql.Connection;
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
                addressStmt.setInt(1, client.getAddress().getStreetNumber());
                addressStmt.setString(2, client.getAddress().getStreet());
                addressStmt.setInt(3, client.getAddress().getZip());
                addressStmt.setString(4, client.getAddress().getCountry());
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
                clientStmt.setString(1, client.getName());
                clientStmt.setDate(2, client.getBirthDate());
                clientStmt.setInt(3, addressId); // Use generated address ID
                clientStmt.setInt(4, bankAccountId); // Use generated bank account ID
                clientStmt.executeUpdate();
            }
    
            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }    
    

    public Client getClientById(String clientId) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, clientId);
        }
        return null;
    }

    public ArrayList<Client> getAllClients() throws SQLException {
        ArrayList<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            /*while (resultSet.next()) {
                clients.add(new Client(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("birth_date"),
                    resultSet.getInt("address_id")
                ));
            }*/
        }
        return clients;
    }
}