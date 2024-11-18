package lu.uni.database;

import lu.uni.client.Address;
import lu.uni.user.Client;
import lu.uni.user.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.*;

public class Database {

    private static final Logger logger = LogManager.getLogger(Database.class);

    public void saveClientData(Client client) {
        String insertAddressSQL = "INSERT INTO addresses (street_number, street, zip, country) VALUES (?, ?, ?, ?)";
        String insertClientSQL = "INSERT INTO clients (id, name, birth_date, address_id, bank_account_balance) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            int addressId;

            try (PreparedStatement addressStatement = connection.prepareStatement(insertAddressSQL, Statement.RETURN_GENERATED_KEYS)) {
                Address address = client.getAddress();
                addressStatement.setInt(1, address.getStreetNumber());
                addressStatement.setString(2, address.getStreet());
                addressStatement.setInt(3, address.getZip());
                addressStatement.setString(4, address.getCountry());
                addressStatement.executeUpdate();

                ResultSet rs = addressStatement.getGeneratedKeys();
                if (rs.next()) {
                    addressId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve address ID.");
                }
            }

            try (PreparedStatement clientStatement = connection.prepareStatement(insertClientSQL)) {
                clientStatement.setString(1, client.getId());
                clientStatement.setString(2, client.getName());
                clientStatement.setDate(3, client.getBirthDate());
                clientStatement.setInt(4, addressId);
                clientStatement.setBigDecimal(5, client.getBankAccount().getBalance());
                clientStatement.executeUpdate();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Client retrieveClientData(String clientId) {
        String clientQuery = "SELECT c.id, c.name, c.birth_date, c.bank_account_balance, " +
                             "a.street_number, a.street, a.zip, a.country " +
                             "FROM clients c " + 
                             "JOIN addresses a ON c.address_id = a.id " +
                             "WHERE c.id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(clientQuery)) {

            statement.setString(1, clientId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int streetNum = rs.getInt("street_number");
                String street = rs.getString("street");
                int zip = rs.getInt("zip");
                String country = rs.getString("country");

                Address address = new Address(streetNum, street, zip, country);

                String id = rs.getString("id");
                String name = rs.getString("name");
                Date bdate = rs.getDate("birth_date");

                Client client = new Client(id, name, bdate, address);

                return client;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void log(String activity) {
        logger.info(activity);
    }

    public boolean checkAccess(User user) {
        return true;
    }

    public String encryptData(String data) {
        return new StringBuilder(data).reverse().toString();
    }

    public String decryptData(String data) {
        return new StringBuilder(data).reverse().toString();
    }
}