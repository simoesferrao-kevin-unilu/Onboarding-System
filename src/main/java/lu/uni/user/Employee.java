package lu.uni.user;

import lu.uni.client.Address;

public class Employee extends User {
    private String accessKey;

    public Employee(String id, String name, java.util.Date birthDate, Address address, String accessKey) {
        super(id, name, birthDate, address);
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}