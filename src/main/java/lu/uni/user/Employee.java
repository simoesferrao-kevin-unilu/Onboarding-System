package lu.uni.user;

import lu.uni.client.Address;

import java.sql.Date;

public class Employee extends User {

    private String accessKey;

    public Employee(int id, String name, Date birthDate, Address address, String accessKey) {
        super(id, name, birthDate, address);
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return accessKey;
    }
}