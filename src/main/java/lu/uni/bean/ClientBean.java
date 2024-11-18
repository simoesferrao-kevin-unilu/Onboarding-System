package lu.uni.bean;

import lu.uni.client.Address;
import lu.uni.database.Database;
import lu.uni.user.Client;

import java.sql.Date;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;


@RequestScoped
@Named("clientBean")
public class ClientBean {

    private int id;
    private String name;
    private Date birthDate;
    private Address address;

    private Database database = new Database();

    public void saveClient() {
        Client client = new Client(id, name, birthDate, address);
        database.saveClientData(client);
    }

    // Getters and Setters
    // ...
}