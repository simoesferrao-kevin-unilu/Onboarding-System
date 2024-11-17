package lu.uni.client;

public class Address {
    private int streetNumber;
    private String street;
    private int zip;
    private String country;

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
}