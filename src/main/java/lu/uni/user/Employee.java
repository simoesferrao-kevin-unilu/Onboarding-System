package lu.uni.user;

import lu.uni.client.Address;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee extends User {

    @Column(name = "access_key", nullable = false)
    private String accessKey;


    public Employee() {}

    public Employee(String id, String name, Date birthDate, Address address, String accessKey) {
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