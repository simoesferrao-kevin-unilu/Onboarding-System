package lu.uni.entities.user;

import java.sql.Date;
import jakarta.persistence.*;
import lu.uni.entities.client.Address;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(nullable = false)
    protected String name;

    @Column(name = "birth_date", nullable = false)
    protected Date birthDate;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    protected Address address;

    @Column(name = "access_key", nullable = false)
    private String accessKey;


    public Employee() {}

    public Employee(String name, Date birthDate, Address address, String accessKey) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.accessKey = accessKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}