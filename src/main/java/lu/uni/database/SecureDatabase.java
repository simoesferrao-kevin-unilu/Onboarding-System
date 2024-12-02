package lu.uni.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SecureDatabase {

    private Connection connection;

    public SecureDatabase(String url, String username, String password) throws Exception {
        connection = DriverManager.getConnection(url, username, password);
    }

    public void saveClientSecurely(String id, String name, String address) {
        try {
            String query = "INSERT INTO clients (id, name, address) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, address);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}