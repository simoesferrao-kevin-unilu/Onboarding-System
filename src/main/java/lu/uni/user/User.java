package lu.uni.user;

import lu.uni.client.Address;

import java.util.Date;

public class User {
    private String id;
    private String name;
    private Date birthDate;
    private Address address;

    public User(String id, String name, Date birthDate, Address address) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
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

    public void updateData(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}