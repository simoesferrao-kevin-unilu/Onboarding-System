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
        String sql = "INSERT INTO clients (id, name, birth_date, address_id, bank_account_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getId());
            statement.setString(2, client.getName());
            statement.setDate(3, client.getBirthDate());
            statement.setInt(4, client.getAddress().getId());
            statement.setString(5, client.getBankAccount().getId());
            statement.executeUpdate();
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