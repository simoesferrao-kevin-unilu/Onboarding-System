package lu.uni.risk;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RiskScheduler {
    
    private ScheduledExecutorService scheduler;

    public RiskScheduler() {
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void scheduleReview(Runnable task, long delay, TimeUnit unit) {
        scheduler.schedule(task, delay, unit);
    }

    public void startPeriodicReview(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }
}