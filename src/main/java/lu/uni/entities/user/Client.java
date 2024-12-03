package lu.uni.entities.user;

import lu.uni.entities.client.Address;
import lu.uni.entities.client.BankAccount;
import lu.uni.entities.risk.RiskScore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToOne
    private BankAccount bankAccount;

    @OneToMany(cascade = CascadeType.ALL)
    private ArrayList<RiskScore> riskScores = new ArrayList<>();


    public Client() {}

    public Client(String name, Date birthDate, Address address) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.bankAccount = new BankAccount();
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

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public ArrayList<RiskScore> getRiskScores() {
        return riskScores;
    }

    public void setRiskScores(ArrayList<RiskScore> riskScores) {
        this.riskScores = riskScores;
    }
}