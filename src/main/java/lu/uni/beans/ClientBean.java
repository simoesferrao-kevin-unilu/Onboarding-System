package lu.uni.beans;

import lu.uni.entities.client.Address;
import lu.uni.entities.database.Database;
import lu.uni.entities.user.Client;

import java.sql.Date;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;


@RequestScoped
@Named("clientBean")
public class ClientBean {

    private String id;
    private String name;
    private Date birthDate;
    private Address address;

    private Database database = new Database();

    public void saveClient() {
        Client client = new Client(name, birthDate, address);
        database.saveClientData(client);
    }

    // Getters and Setters
    // ...
}