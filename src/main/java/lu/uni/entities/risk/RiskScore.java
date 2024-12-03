package lu.uni.entities.risk;

import jakarta.persistence.*;
import lu.uni.entities.user.Client;

@Entity
@Table(name = "risk_scores")
public class RiskScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column(name = "risk_score", nullable = false)
    private int riskScore;

    public RiskScore(Client client, int riskScore) {
        this.client = client;
        this.riskScore = riskScore;
    }

    public Client getClient() {
        return client;
    }

    public int getRiskScore() {
        return riskScore;
    }
}