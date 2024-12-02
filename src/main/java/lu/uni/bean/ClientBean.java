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

    private String id;
    private String name;
    private Date birthDate;
    private Address address;

    private Database database = new Database();

    public void saveClient() {
        Client client = new Client(name, birthDate, address);
        database.saveClientData(client);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    
}