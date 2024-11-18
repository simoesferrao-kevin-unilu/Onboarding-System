package lu.uni.client;

import java.util.ArrayList;

public class BankAccount {

    private float balance;
    private ArrayList<String> transactionsLog;

    public BankAccount(int balance) {
        this.balance = balance;
        this.transactionsLog = new ArrayList<>();
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<String> getTransactionsLog() {
        return transactionsLog;
    }

    public void addTransaction(String transaction) {
        this.transactionsLog.add(transaction);
    }
}