package lu.uni;

import lu.uni.entities.user.Client;
import lu.uni.entities.client.Address;
import lu.uni.entities.database.DatabaseConnection;
import lu.uni.dao.ClientDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class MainTest {

    private static final Logger logger = LogManager.getLogger(MainTest.class);

    private static boolean authenticateEmployee(Connection connection, String name, String accessKey) {
        String sql = "SELECT access_key FROM employees WHERE name = ?";
        boolean isAuthenticated = false;

        String clientIp = "Unknown";
        String hostname = "Unknown";

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            clientIp = localHost.getHostAddress();
            hostname = localHost.getHostName();
        } catch (UnknownHostException e) {
            logger.error("Unable to determine host information.", e);
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHashedKey = resultSet.getString("access_key");
                    String inputHashedKey = encryptAccessKey(accessKey);

                    if (storedHashedKey.equals(inputHashedKey)) {
                        isAuthenticated = true;
                        logger.info(String.format("Authentication successful for employee: %s, IP: %s, Hostname: %s", name, clientIp, hostname));
                    } else {
                        logger.warn(String.format("Authentication failed for employee: %s, IP: %s, Hostname: %s", name, clientIp, hostname));
                    }
                } else {
                    logger.warn(String.format("Authentication failed for non-existent employee: %s, IP: %s, Hostname: %s", name, clientIp, hostname));
                }
            }
        } catch (SQLException e) {
            logger.error(String.format("Error during authentication for employee: %s, IP: %s, Hostname: %s", name, clientIp, hostname), e);
        }

        return isAuthenticated;
    }

    private static String encryptAccessKey(String accessKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(accessKey.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting access key", e);
        }
    }

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                logger.info("Connected to the database successfully!");
            }

            Scanner scanner = new Scanner(System.in);

            // Employee Authentication
            System.out.println("--------------------------+");
            System.out.println("| Employee Authentication |");
            System.out.println("+-------------------------+");
            System.out.print("Enter your name: ");
            String employeeName = scanner.nextLine();

            System.out.print("Enter your access key: ");
            String accessKey = scanner.nextLine();

            if (!authenticateEmployee(connection, employeeName, accessKey)) {
                System.out.println("Authentication failed. Access denied.");
                scanner.close();
                return; // Exit if authentication fails
            }

            System.out.println("Authentication successful. Welcome, " + employeeName + "!\n");

            ClientDAO clientDAO = new ClientDAO();

            while (true) {
                System.out.println("--------------------+");
                System.out.println("| Onboarding System |");
                System.out.println("+-------------------+");
                System.out.println("1. Onboard a new client");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline

                if (choice == 2) {
                    logger.info("Employee " + employeeName + " exited the system.");
                    break;
                } else if (choice == 1) {
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
                        logger.info("Client onboarded successfully by employee " + employeeName + ".");
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid input format by employee " + employeeName + ": " + e.getMessage());
                    } catch (Exception e) { // Catch other unforeseen errors
                        logger.error("An error occurred while onboarding a client by employee " + employeeName + ".", e);
                    }
                } else {
                    logger.warn("Invalid menu choice by employee " + employeeName + ".");
                }
            }

            scanner.close();
        } catch (Exception e) {
            logger.error("An unexpected error occurred in the system.", e);
        }
    }
}