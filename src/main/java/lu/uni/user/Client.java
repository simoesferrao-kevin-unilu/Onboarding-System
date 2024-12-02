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
public class Client extends User {

    @OneToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private ArrayList<RiskScore> riskScores = new ArrayList<>();


    public Client() {}

    public Client(String id, String name, Date birthDate, Address address) {
        super(id, name, birthDate, address);
        this.bankAccount = new BankAccount(BigDecimal.valueOf(0));
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