package lu.uni.risk;

public class RiskScore {

    private int clientID;
    private int riskScore;

    public RiskScore(int clientID, int riskScore) {
        this.riskScore = riskScore;
    }

    public int getClientID() {
        return clientID;
    }

    public int getRiskScore() {
        return riskScore;
    }
}