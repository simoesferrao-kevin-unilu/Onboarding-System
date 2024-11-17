package lu.uni.user;

import lu.uni.client.Address;
import lu.uni.client.BankAccount;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private BankAccount bankAccount;
    private List<Integer> riskScores;

    public Client(String id, String name, Date birthDate, Address address, BankAccount bankAccount) {
        super(id, name, birthDate, address);
        this.bankAccount = bankAccount;
        this.riskScores = new ArrayList<>();
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public List<Integer> getRiskScores() {
        return riskScores;
    }

    public void addRiskScore(int riskScore) {
        this.riskScores.add(riskScore);
    }
}