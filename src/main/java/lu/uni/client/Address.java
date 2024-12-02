package lu.uni.client;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "street_number", nullable = false)
    private int streetNumber;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private int zip;

    @Column(nullable = false)
    private String country;

    
    public Address() {}

    public Address(int streetNumber, String street, int zip, String country) {
        this.streetNumber = streetNumber;
        this.street = street;
        this.zip = zip;
        this.country = country;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}