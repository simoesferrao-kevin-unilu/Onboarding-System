package lu.uni;

import lu.uni.user.Client;
import lu.uni.user.Employee;
import lu.uni.client.Address;
import lu.uni.dao.ClientDAO;
import lu.uni.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.util.UUID;
import jakarta.persistence.*;



public class MainTest {
    
    public static void main(String[] args) {
        // Database connection testing
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Client management testing
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankingPU");
        EntityManager em = emf.createEntityManager();
        ClientDAO clientDAO = new ClientDAO(em);

        Address address = new Address();
        address.setStreetNumber(123);
        address.setStreet("Main Street");
        address.setZip(12345);
        address.setCountry("Luxembourg");

        Client client = new Client(
            UUID.randomUUID().toString(),
            "John Doe",
            new Date(1990, 1, 1),
            address
        );

        try {
            clientDAO.addClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Employee employee = new Employee(
            UUID.randomUUID().toString(),
            "Alice Smith",
            new Date(1985, 5, 15),
            address,
            "secure-key-123"
        );

        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}