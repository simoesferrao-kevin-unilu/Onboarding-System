package lu.uni;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lu.uni.entities.database.DatabaseConnection;
import lu.uni.entities.database.EncryptionUtil;

public class AddEmployee {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            }

            String name = "Kevin"; // Employee name
            String accessKey = "123456789"; // Employee access key (plaintext)

            // Encrypt the access key
            String encryptedAccessKey = EncryptionUtil.encrypt(accessKey);

            // Insert employee address
            int addressId = 0;
            String insertAddressSQL = "INSERT INTO addresses (street_number, street, zip, country) VALUES (?, ?, ?, ?)";
            try (PreparedStatement addressStmt = connection.prepareStatement(insertAddressSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                String encryptedStreetNumber = EncryptionUtil.encrypt("123");
                String encryptedStreet = EncryptionUtil.encrypt("Rue de l'Universite");
                String encryptedZip = EncryptionUtil.encrypt("1234");
                String country = "Luxembourg";

                addressStmt.setString(1, encryptedStreetNumber);
                addressStmt.setString(2, encryptedStreet);
                addressStmt.setString(3, encryptedZip);
                addressStmt.setString(4, country);
                addressStmt.executeUpdate();

                try (ResultSet generatedKeys = addressStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        addressId = generatedKeys.getInt(1); // Get generated address ID
                    } else {
                        throw new SQLException("Address insertion failed, no ID obtained.");
                    }
                }
            }

            // Insert employee into the database
            String sql = "INSERT INTO employees (name, birth_date, address_id, access_key) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setDate(2, Date.valueOf("2000-12-21")); // Birthdate
                preparedStatement.setInt(3, addressId); // Assume an address_id exists
                preparedStatement.setString(4, encryptedAccessKey);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Employee added successfully!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}