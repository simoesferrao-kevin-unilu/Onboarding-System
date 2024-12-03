package lu.uni.user;

import lu.uni.client.Address;
import lu.uni.client.BankAccount;
import lu.uni.risk.RiskScore;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

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

    @OneToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
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