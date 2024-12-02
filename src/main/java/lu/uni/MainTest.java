package lu.uni;

import lu.uni.user.Client;
import lu.uni.user.Employee;
import lu.uni.client.Address;
import lu.uni.client.BankAccount;
import lu.uni.dao.ClientDAO;
import lu.uni.database.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.UUID;
import jakarta.persistence.*;



public class MainTest {
    
    public static void main(String[] args) {
        // Database connection testing
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Client management testing
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankingPU");
        EntityManager em = emf.createEntityManager();
        ClientDAO clientDAO = new ClientDAO(em);

        Address address = new Address(123, "Main Street", 12345, "Luxembourg");
        em.getTransaction().begin();
        em.persist(address);
        em.getTransaction().commit();

        BankAccount bankAccount = new BankAccount(BigDecimal.valueOf(1000));
        em.getTransaction().begin();
        em.persist(bankAccount);
        em.getTransaction().commit();

        Client client = new Client(
            UUID.randomUUID().toString(),
            "John Doe",
            Date.valueOf(LocalDate.of(1990, 1, 1)),
            address
        );
        client.setBankAccount(bankAccount);
        try {
            clientDAO.addClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        em.getTransaction().begin();
        em.persist(client);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}