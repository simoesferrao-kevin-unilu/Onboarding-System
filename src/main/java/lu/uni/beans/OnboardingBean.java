package lu.uni.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lu.uni.dao.ClientDAO;
import lu.uni.entities.client.Address;
import lu.uni.entities.user.Client;

import java.io.Serializable;
import java.sql.Date;

@Named("onboardingBean")
@SessionScoped
public class OnboardingBean implements Serializable {

    private String name;
    private Date birthDate;
    private int streetNumber;
    private String street;
    private int zip;
    private String country;

    @Inject
    private ClientDAO clientDAO;

    public void submit() {
        try {
            Address address = new Address();
            address.setStreetNumber(streetNumber);
            address.setStreet(street);
            address.setZip(zip);
            address.setCountry(country);

            Client client = new Client();
            client.setName(name);
            client.setBirthDate(birthDate);
            client.setAddress(address);

            clientDAO.addClient(client);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Client added successfully!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding client!", e.getMessage()));
        }
    }

    // Getters and setters
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

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}