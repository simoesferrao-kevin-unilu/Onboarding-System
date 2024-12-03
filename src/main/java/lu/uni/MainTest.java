package lu.uni;

import lu.uni.dao.ClientDAO;
import lu.uni.entities.client.Address;
import lu.uni.entities.client.BankAccount;
import lu.uni.entities.database.DatabaseConnection;
import lu.uni.entities.user.Client;

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


        try {
            ClientDAO clientDAO = new ClientDAO();
    
            // Create an Address object
            Address address = new Address(12, "Main Street", 12345, "Luxembourg");
            Client client = new Client("James B.", Date.valueOf("1980-05-15"), address);
    
            // Add the client to the database
            clientDAO.addClient(client);
            System.out.println("Client added successfully!");
    
            // Retrieve the client by ID
            /*Client retrievedClient = clientDAO.getClientById(1);
            if (retrievedClient != null) {
                System.out.println("Retrieved Client: " + retrievedClient.getName());
            }
    
            // Get all clients
            ArrayList<Client> clients = clientDAO.getAllClients();
            for (Client c : clients) {
                System.out.println("Client: " + c.getName());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        // Client management testing
        /*EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankingPU");
        EntityManager em = emf.createEntityManager();
        ClientDAO clientDAO = new ClientDAO(em);

        Address address = new Address(123, "Main Street", 12345, "Luxembourg");
        em.getTransaction().begin();
        em.persist(address);
        em.getTransaction().commit();

        BankAccount bankAccount = new BankAccount();
        em.getTransaction().begin();
        em.persist(bankAccount);
        em.getTransaction().commit();

        Client client = new Client(
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
        emf.close();*/
    }
}