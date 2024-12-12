package lu.uni;

import lu.uni.entities.user.Client;
import lu.uni.entities.client.Address;
import lu.uni.entities.database.DatabaseConnection;
import lu.uni.dao.ClientDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class MainTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return; // Exit if the database connection fails
        }

        ClientDAO clientDAO = new ClientDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=======================\nOnboarding System\n=======================");
            System.out.println("1. Onboard a new client");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            if (choice == 2) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            } 
            else if (choice == 1) {
                try {
                    // Name
                    System.out.print("Enter client's name: ");
                    String name = scanner.nextLine();

                    // Birthdate
                    System.out.print("Enter client's birth date (yyyy-mm-dd): ");
                    String birthDateInput = scanner.nextLine();
                    Date birthDate = Date.valueOf(birthDateInput);

                    // Street number
                    System.out.print("Enter client's address street number: ");
                    int streetNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    // Street name
                    System.out.print("Enter client's address street: ");
                    String street = scanner.nextLine();

                    // Zip code
                    System.out.print("Enter client's address zip code: ");
                    int zip = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    // Country
                    System.out.print("Enter client's address country: ");
                    String country = scanner.nextLine();

                    Address address = new Address(streetNumber, street, zip, country);
                    Client client = new Client(name, birthDate, address);

                    clientDAO.addClient(client);
                    System.out.println("Client onboarded successfully!\n");

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input format: " + e.getMessage());
                } catch (Exception e) { // Catch other unforeseen errors
                    System.out.println("An error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            } 
            else {
                System.out.println("Invalid choice. Please try again.\n");
            }
        }

        scanner.close();
    }
}