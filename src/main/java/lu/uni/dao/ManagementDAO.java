package lu.uni.dao;

import lu.uni.client.Address;
import lu.uni.client.BankAccount;
import lu.uni.database.DatabaseConnection;
import lu.uni.user.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagementDAO {
    
    public void onboardClient(Client client, Address address, BankAccount bankAccount) throws SQLException {
        String clientSql = "INSERT INTO clients (id, name, birth_date, address_id, bank_account_id) VALUES (?, ?, ?, ?, ?)";
        String addressSql = "INSERT INTO addresses (street_number, street, zip, country) VALUES (?, ?, ?, ?)";
        String accountSql = "INSERT INTO bank_accounts (id, bank_account_balance) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // Insert address
            try (PreparedStatement addressStmt = connection.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS)) {
                addressStmt.setInt(1, address.getStreetNumber());
                addressStmt.setString(2, address.getStreet());
                addressStmt.setInt(3, address.getZip());
                addressStmt.setString(4, address.getCountry());
                addressStmt.executeUpdate();

                try (ResultSet generatedKeys = addressStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        address.setId(generatedKeys.getInt(1));
                    }
                }
            }

            // Insert bank account
            try (PreparedStatement accountStmt = connection.prepareStatement(accountSql)) {
                accountStmt.setInt(1, bankAccount.getId());
                accountStmt.setBigDecimal(2, bankAccount.getBalance());
                accountStmt.executeUpdate();
            }

            // Insert client
            try (PreparedStatement clientStmt = connection.prepareStatement(clientSql)) {
                clientStmt.setInt(1, client.getId());
                clientStmt.setString(2, client.getName());
                clientStmt.setDate(3, client.getBirthDate());
                clientStmt.setInt(4, address.getId());
                clientStmt.setInt(5, bankAccount.getId());
                clientStmt.executeUpdate();
            }

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Or connection.rollback() if needed
        }
    }
}