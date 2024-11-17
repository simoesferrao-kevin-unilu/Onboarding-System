package lu.uni.bean;

import lu.uni.database.Database;
import lu.uni.risk.RiskScore;
import lu.uni.user.Client;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@RequestScoped
@Named("loginBean")
public class RiskScoreBean {
    private String clientId;
    private List<RiskScore> riskScores;

    private Database database = new Database();

    public void getRiskScores() {
        Client client = database.retrieveClientData(clientId);
        if (client != null) {
            riskScores = client.getRiskScores();
        }
    }

    // Getters and Setters
    // ...
}