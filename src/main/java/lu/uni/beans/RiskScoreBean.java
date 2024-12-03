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

    public void getAlRiskScores() {
        Client client = database.retrieveClientData(clientId);
        if (client != null) {
            alRiskScores = client.getRiskScores();
        }
    }

    // Getters and Setters
    // ...
}