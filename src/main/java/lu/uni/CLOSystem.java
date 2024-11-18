package lu.uni;

import lu.uni.database.Database;
import lu.uni.risk.RiskScore;
import lu.uni.user.Client;
import lu.uni.user.Employee;

import java.math.BigDecimal;
import java.util.*;

public class CLOSystem {
    private Database db;
    private ArrayList<Employee> alAuthorizedEmployees;

    public CLOSystem(Database db) {
        this.db = db;
        this.alAuthorizedEmployees = new ArrayList<>();
    }

    public void onboardClient(Client client) {
        if (validateClientData(client)) {
            db.saveClientData(client);
            System.out.println("Client onboarded successfully!");
        } 
        else {
            System.out.println("Client data validation failed!");
        }
    }

    public Client getClient(String clientId) {
        return db.retrieveClientData(clientId);
    }

    public void calculateRisk(Client client) {
        //BigDecimal risk = client.getBankAccount().getBalance() < 1000 ? 10 : 1;
        //RiskScore riskScore = new RiskScore(client.getId(), risk);
        //client.addRiskScore(riskScore);
        //db.log("Risk calculated for client: " + client.getId());
    }

    public void scheduleReview(Client client, Date date) {
        System.out.println("Scheduled review for client " + client.getId() + " on " + date);
    }

    private boolean validateClientData(Client client) {
        return client.getName() != null && client.getAddress() != null;
    }
}