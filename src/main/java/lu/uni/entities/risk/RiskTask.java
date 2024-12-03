package lu.uni.entities.risk;

public class RiskTask implements Runnable {
    
    @Override
    public void run() {
        System.out.println("Running periodic compliance review...");
        // Add compliance review logic here
    }
}

// In Main Class
// RiskScheduler scheduler = new RiskScheduler();
// scheduler.startPeriodicReview(new ComplianceTask(), 0, 1, TimeUnit.DAYS);