package lu.uni.user;

import lu.uni.client.Address;
import lu.uni.client.BankAccount;
import lu.uni.risk.RiskScore;

import java.sql.Date;
import java.util.ArrayList;

public class Client extends User {
    
    private BankAccount bankAccount;
    private ArrayList<RiskScore> alRiskScores;

    public Client(int id, String name, Date birthDate, Address address) {
        super(id, name, birthDate, address);
        this.bankAccount = new BankAccount(0);
        this.alRiskScores = new ArrayList<>();
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public ArrayList<RiskScore> getAlRiskScores() {
        return alRiskScores;
    }

    public void addRiskScore(RiskScore riskScore) {
        this.alRiskScores.add(riskScore);
    }
}