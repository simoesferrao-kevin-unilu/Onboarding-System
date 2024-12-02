package lu.uni.user;

import lu.uni.client.Address;

import java.sql.Date;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public abstract class User {
    
    @Id
    protected String id; // UUID

    @Column(nullable = false)
    protected String name;

    @Column(name = "birth_date", nullable = false)
    protected Date birthDate;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    protected Address address;


    protected User() {}

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
}