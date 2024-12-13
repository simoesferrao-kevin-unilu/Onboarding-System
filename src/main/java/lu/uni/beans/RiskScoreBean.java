package lu.uni.beans;

import lu.uni.entities.database.Database;
import lu.uni.entities.risk.RiskScore;
import lu.uni.entities.user.Client;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.ArrayList;

@RequestScoped
@Named("riskScoreBean")
public class RiskScoreBean {

    private String clientId;
    private ArrayList<RiskScore> alRiskScores;

    private Database database = new Database();

    public ArrayList<RiskScore> getAlRiskScores() {
        Client client = database.retrieveClientData(clientId);
        if (client != null) {
            alRiskScores = client.getRiskScores();
        }

        return alRiskScores;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAlRiskScores(ArrayList<RiskScore> alRiskScores) {
        this.alRiskScores = alRiskScores;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}