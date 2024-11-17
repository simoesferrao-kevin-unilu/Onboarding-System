package lu.uni.bean;

import lu.uni.database.Database;
import lu.uni.user.Client;

import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ClientBean {
    private String name;
    private String birthDate;
    private String street;
    private int streetNumber;
    private int zip;
    private String country;

    private Database database = new Database();

    public void saveClient() {
        Client client = new Client(name, birthDate, street, streetNumber, zip, country);
        database.saveClientData(client);
    }

    // Getters and Setters
    // ...
}