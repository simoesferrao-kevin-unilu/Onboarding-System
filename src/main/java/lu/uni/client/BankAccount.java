package lu.uni.client;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private int balance;
    private List<String> transactionsLog;

    public BankAccount(int balance) {
        this.balance = balance;
        this.transactionsLog = new ArrayList<>();
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<String> getTransactionsLog() {
        return transactionsLog;
    }

    public void addTransaction(String transaction) {
        this.transactionsLog.add(transaction);
    }
}