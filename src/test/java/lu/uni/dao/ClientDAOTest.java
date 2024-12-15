package lu.uni.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lu.uni.entities.client.Address;
import lu.uni.entities.client.BankAccount;
import lu.uni.entities.database.DatabaseConnection;
import lu.uni.entities.user.Client;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class ClientDAOTest {

    private ClientDAO clientDAO;

    @BeforeEach
    public void setUp() {
        clientDAO = new ClientDAO();
    }

    @BeforeEach
    public void resetDatabase() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM clients");
            stmt.executeUpdate("DELETE FROM addresses");
            stmt.executeUpdate("DELETE FROM bank_accounts");
        }
    }

    public Client createTestClient(String name, int streetNumber, BigDecimal balance) {
        Address address = new Address(streetNumber, "Test Street", 12345, "Test Country");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(balance);
        return new Client(name, Date.valueOf("2000-01-01"), address);
    }


    // Add client
    @Test
    public void testAddClient_ValidInput() throws Exception {
        Client testClient = createTestClient("Test Client", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("Test Client", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client should be added successfully");
    }

    @Test
    public void testAddClient_InvalidAddress() throws Exception {
        Client testClient = createTestClient("Test Client", -1, BigDecimal.valueOf(1000.00)); // Negative street number

        assertThrows(SQLException.class, () -> {
            clientDAO.addClient(testClient);
        }, "Invalid address data should throw SQLException");
    }

    @Test
    public void testAddClient_LargeInput() throws Exception {
        Client testClient = createTestClient("A".repeat(50), 123, BigDecimal.valueOf(1000000000.00)); // Large name and balance
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("A".repeat(50), Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client with large data input should be added successfully");
    }

    @Test
    public void testAddClient_MaximumNameLength() throws Exception {
        String maxLengthName = "a".repeat(50); // Assuming 255 is the max length
        Client testClient = createTestClient(maxLengthName, 123, BigDecimal.valueOf(500.00));
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate(maxLengthName, Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client with maximum name length should be added successfully");
        assertEquals(maxLengthName, retrievedClient.getName(), "Client name should match the maximum length name");
    }

    @Test
    public void testAddClient_EmptyName() {
        Client testClient = createTestClient("", 123, BigDecimal.valueOf(500.00));

        assertThrows(SQLException.class, () -> {
            clientDAO.addClient(testClient);
        }, "Client with empty name should throw SQLException");
    }


    // Get client by name and birthdate
    @Test
    public void testGetClientByNameAndBirthdate_ValidInput() throws Exception {
        Client testClient = createTestClient("Test Client", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("Test Client", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client should be retrieved successfully");
        assertEquals("test client", retrievedClient.getName(), "Client name should match");
    }

    @Test
    public void testGetClientByNameAndBirthdate_NonExistent() throws Exception {
        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("Non Existent", Date.valueOf("1999-01-01"));
        assertNull(retrievedClient, "Non-existent client should not be retrieved");
    }

    @Test
    public void testGetClientByNameAndBirthdate_NullInput() throws Exception {
        assertThrows(SQLException.class, () -> {
            clientDAO.getClientByNameAndBirthdate(null, null);
        }, "Null inputs should throw SQLException");
    }

    @Test
    public void testGetClientByNameAndBirthdate_CaseInsensitive() throws Exception {
        Client testClient = createTestClient("Case Test", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("case test", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client retrieval should be case-insensitive");
    }

    @Test
    public void testGetClientByNameAndBirthdate_SimilarNames() throws Exception {
        Client testClient1 = createTestClient("John Smith", 123, BigDecimal.valueOf(1000.00));
        Client testClient2 = createTestClient("John Smyth", 124, BigDecimal.valueOf(2000.00));
        clientDAO.addClient(testClient1);
        clientDAO.addClient(testClient2);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("John Smith", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Should retrieve the correct client by name");
        assertEquals("john smith", retrievedClient.getName(), "Should match the exact client name");
    }


    // Get all clients
    @Test
    public void testGetAllClients_EmptyDatabase() throws Exception {
        var clients = clientDAO.getAllClients();
        assertTrue(clients.isEmpty(), "No clients should be retrieved from an empty database");
    }

    @Test
    public void testGetAllClients_MultipleClients() throws Exception {
        clientDAO.addClient(createTestClient("Client One", 123, BigDecimal.valueOf(1000.00)));
        clientDAO.addClient(createTestClient("Client Two", 456, BigDecimal.valueOf(2000.00)));

        var clients = clientDAO.getAllClients();
        assertTrue(clients.size() >= 2, "Multiple clients should be retrieved");
    }

    @Test
    public void testGetAllClients_NoClients() throws Exception {
        var clients = clientDAO.getAllClients();
        assertTrue(clients.isEmpty(), "No clients should exist in an empty database");
    }

    @Test
    public void testGetAllClients_LargeDatabase() throws Exception {
        for (int i = 1; i <= 1000; i++) {
            clientDAO.addClient(createTestClient("Client " + i, i, BigDecimal.valueOf(i * 10.00)));
        }

        var clients = clientDAO.getAllClients();
        assertEquals(1000, clients.size(), "Should retrieve all clients from a large database");
    }


    // Update client info
    @Test
    public void testUpdateClient_ValidUpdate() throws Exception {
        // Create and add a test client
        Client testClient = createTestClient("Test Client", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        // Retrieve the client and ensure IDs are populated
        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("Test Client", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client should be retrieved successfully");
        assertNotNull(retrievedClient.getAddress().getId(), "Address ID should not be null");
        assertNotNull(retrievedClient.getBankAccount().getId(), "BankAccount ID should not be null");

        // Update client details
        retrievedClient.getAddress().setStreet("Updated Street");
        retrievedClient.getBankAccount().setBalance(BigDecimal.valueOf(2000.00));
        clientDAO.updateClient(retrievedClient);

        // Retrieve the updated client
        Client updatedClient = clientDAO.getClientByNameAndBirthdate("Test Client", Date.valueOf("2000-01-01"));
        assertEquals("Updated Street", updatedClient.getAddress().getStreet(), "Street should be updated");

        // Use compareTo for BigDecimal comparison
        assertTrue(
            updatedClient.getBankAccount().getBalance().compareTo(BigDecimal.valueOf(2000.00)) == 0,
            "Balance should be updated"
        );
    }

    @Test
    public void testUpdateClient_InvalidUpdate() throws Exception {
        Client testClient = createTestClient("Test Client", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("Test Client", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client should be retrieved successfully");

        retrievedClient.getAddress().setStreetNumber(-1); // Invalid data

        assertThrows(SQLException.class, () -> {
            clientDAO.updateClient(retrievedClient);
        }, "Invalid update should throw SQLException");
    }

    @Test
    public void testUpdateClient_NullValues() throws Exception {
        Client testClient = createTestClient("Test Client", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("Test Client", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client should be retrieved successfully");

        retrievedClient.getAddress().setStreet(null); // Setting a field to null

        assertThrows(SQLException.class, () -> {
            clientDAO.updateClient(retrievedClient);
        }, "Updating a client with null values should throw SQLException");
    }



    // SQL Injection testing
    @Test
    public void testSQLInjectionProtection() throws Exception {
        Client testClient = createTestClient("Injection Test", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        String maliciousInput = "Injection Test' OR '1'='1";
        Client retrievedClient = clientDAO.getClientByNameAndBirthdate(maliciousInput, Date.valueOf("2000-01-01"));
        assertNull(retrievedClient, "SQL injection attempt should fail");
    }

    @Test
    public void testAddClient_BoundaryAddressFields() throws Exception {
        Client boundaryClient = createTestClient("Boundary Test", Integer.MAX_VALUE, BigDecimal.valueOf(0.00));
        clientDAO.addClient(boundaryClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("Boundary Test", Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client with boundary values for address fields should be added successfully");
        assertEquals(Integer.MAX_VALUE, retrievedClient.getAddress().getStreetNumber(), "Street number should match the boundary value");
    }

    @Test
    public void testGetClientByNameAndBirthdate_SQLInjectionInName() throws Exception {
        // Add a valid client
        Client testClient = createTestClient("Valid Client", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        // Attempt SQL Injection
        String maliciousInput = "' OR '1'='1";
        Client retrievedClient = clientDAO.getClientByNameAndBirthdate(maliciousInput, Date.valueOf("2000-01-01"));

        // Ensure no client is retrieved
        assertNull(retrievedClient, "SQL injection in name field should fail");
    }

    @Test
    public void testGetClientByNameAndBirthdate_SQLInjectionInDate() throws Exception {
        // Add a valid client
        Client testClient = createTestClient("Valid Client", 123, BigDecimal.valueOf(1000.00));
        clientDAO.addClient(testClient);

        // Attempt SQL Injection
        String maliciousName = "Valid Client";
        String maliciousDate = "2000-01-01' OR '1'='1";

        assertThrows(Exception.class, () -> {
            clientDAO.getClientByNameAndBirthdate(maliciousName, Date.valueOf(maliciousDate));
        }, "SQL injection in date field should throw an exception");
    }

    @Test
    public void testAddClient_SQLInjection() {
        // Create a client with a malicious SQL injection attempt in the name field
        Client maliciousClient = createTestClient(
            "Injected'; DROP TABLE clients; --",
            123,
            BigDecimal.valueOf(1000.00)
        );

        try {
            clientDAO.addClient(maliciousClient);

            fail("SQL injection should have been prevented");
        } catch (IllegalArgumentException e) {
            // Expected behavior: IllegalArgumentException should be thrown
            assertTrue(e.getMessage().contains("forbidden SQL keyword"),
                    "Exception should indicate forbidden SQL keyword");
        } catch (SQLException e) {}

        // Verify that the clients table still exists and is intact
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM clients");
            ResultSet resultSet = stmt.executeQuery()) {

            assertTrue(resultSet.next(), "Clients table should still exist after SQL injection attempt");
        } catch (SQLException e) {
            fail("SQL injection should not affect the database structure: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllClients_SQLInjection() throws Exception {
        // Attempt to inject SQL via an invalid query modification
        Connection connection = DatabaseConnection.getConnection();

        String maliciousSQL = "SELECT * FROM clients WHERE name = 'Injected'; DROP TABLE clients; --";

        try (PreparedStatement stmt = connection.prepareStatement(maliciousSQL)) {
            assertThrows(SQLException.class, stmt::executeQuery, "Malicious query should not execute");
        }
    }

    @Test
    public void testAddClient_AddressValidation() throws Exception {
        // Create a client with invalid address data
        Client maliciousClient = createTestClient(
            "Valid Client",
            -1, // Invalid negative street number
            BigDecimal.valueOf(1000.00)
        );

        // Attempt to add the client
        assertThrows(SQLException.class, () -> {
            clientDAO.addClient(maliciousClient);
        }, "Invalid address input should be prevented");
    }

    @Test
    public void testAddClient_ExtremeInputLengths() throws Exception {
        Client testClient = createTestClient(
            "A".repeat(50), // Maximum length name
            Integer.MAX_VALUE, // Maximum street number
            BigDecimal.valueOf(999999999.99) // Maximum bank balance
        );
        clientDAO.addClient(testClient);

        Client retrievedClient = clientDAO.getClientByNameAndBirthdate("a".repeat(50), Date.valueOf("2000-01-01"));
        assertNotNull(retrievedClient, "Client with maximum input lengths should be added successfully");
    }
}