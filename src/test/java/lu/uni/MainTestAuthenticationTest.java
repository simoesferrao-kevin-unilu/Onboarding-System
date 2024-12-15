package lu.uni;

import org.junit.jupiter.api.Test;

import lu.uni.entities.database.DatabaseConnection;
import lu.uni.entities.database.EncryptionUtil;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.Date;

class MainTestAuthenticationTest {

    @Test
    public void testAuthenticateEmployeeValidCredentials() throws Exception {
        Connection connection = DatabaseConnection.getConnection();

        // Insert test employee
        String name = "TestUser";
        String accessKey = "TestKey123";
        String encryptedAccessKey = EncryptionUtil.encrypt(accessKey);

        String insertSQL = "INSERT INTO employees (name, birth_date, access_key) VALUES (?, ?, ?)";
        try (var stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, name);
            stmt.setDate(2, Date.valueOf("2000-01-01"));
            stmt.setString(3, encryptedAccessKey);
            stmt.executeUpdate();
        }

        // Test authentication
        boolean isAuthenticated = MainTest.authenticateEmployee(connection, name, accessKey);
        assertTrue(isAuthenticated, "Employee should be authenticated with correct credentials");
    }

    @Test
    public void testAuthenticateEmployeeInvalidCredentials() throws Exception {
        Connection connection = DatabaseConnection.getConnection();

        // Test with invalid credentials
        boolean isAuthenticated = MainTest.authenticateEmployee(connection, "InvalidUser", "WrongKey");
        assertFalse(isAuthenticated, "Employee should not be authenticated with incorrect credentials");
    }
}