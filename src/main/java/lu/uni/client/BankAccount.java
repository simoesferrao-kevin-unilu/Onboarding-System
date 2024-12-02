package lu.uni.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "bank_account_balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private ArrayList<TransactionLog> transactionLogs = new ArrayList<>();


    public BankAccount(BigDecimal balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ArrayList<TransactionLog> getTransactionLogs() {
        return transactionLogs;
    }

    public void setTransactionLogs(ArrayList<TransactionLog> transactionLogs) {
        this.transactionLogs = transactionLogs;
    }
}