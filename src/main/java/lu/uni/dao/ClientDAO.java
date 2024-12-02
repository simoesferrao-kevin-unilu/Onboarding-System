package lu.uni.dao;

import lu.uni.database.DatabaseConnection;
import lu.uni.user.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.persistence.EntityManager;

public class ClientDAO {
    
    private EntityManager em;

    public ClientDAO(EntityManager em) {
        this.em = em;
    }

    public void addClient(Client client) throws SQLException {
        String insertAddressSQL = "INSERT INTO addresses (street_number, street, zip, country) VALUES (?, ?, ?, ?)";
        String insertClientSQL = "INSERT INTO clients (id, name, birth_date, address_id, bank_account_id) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Insert address
            int addressId;
            try (PreparedStatement addressStmt = connection.prepareStatement(insertAddressSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                addressStmt.setInt(1, client.getAddress().getStreetNumber());
                addressStmt.setString(2, client.getAddress().getStreet());
                addressStmt.setInt(3, client.getAddress().getZip());
                addressStmt.setString(4, client.getAddress().getCountry());
                addressStmt.executeUpdate();
    
                try (ResultSet generatedKeys = addressStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        addressId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Address insertion failed, no ID obtained.");
                    }
                }
            }
    
            // Insert client
            try (PreparedStatement clientStmt = connection.prepareStatement(insertClientSQL)) {
                clientStmt.setInt(1, client.getId());
                clientStmt.setString(2, client.getName());
                clientStmt.setDate(3, client.getBirthDate());
                clientStmt.setInt(4, addressId);
                clientStmt.setInt(5, client.getBankAccount().getId()); // Ensure BankAccount ID exists
                clientStmt.executeUpdate();
            }
        }
    }
    

    public Client getClientById(String clientId) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, clientId);
            /*try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Client(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getDate("birth_date"),
                        resultSet.getInt("address_id")
                    );
                }
            }*/
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