package fr.istic.taa.jaxrs.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ConcertAlertScheduler {

    private static final Logger LOGGER = Logger.getLogger(ConcertAlertScheduler.class.getName());
    private static final ConcertAlertScheduler INSTANCE = new ConcertAlertScheduler();

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final AtomicBoolean started = new AtomicBoolean(false);

    private ConcertAlertScheduler() {
    }

    public static ConcertAlertScheduler getInstance() {
        return INSTANCE;
    }

    public void start() {
        if (!started.compareAndSet(false, true)) {
            return;
        }

        long initialDelay = secondsUntilNextRun();
        long oneDay = TimeUnit.DAYS.toSeconds(1);

        executor.scheduleAtFixedRate(() -> {
            try {
                int sent = new ConcertAlertService().processDailyAlerts();
                LOGGER.info("Daily concert alerts processed. Sent emails: " + sent);
            } catch (Exception e) {
                LOGGER.warning("Daily concert alerts failed: " + e.getMessage());
            }
        }, initialDelay, oneDay, TimeUnit.SECONDS);
    }

    public int runNow() {
        return new ConcertAlertService().processDailyAlerts();
    }

    private long secondsUntilNextRun() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = now.withHour(8).withMinute(0).withSecond(0).withNano(0);
        if (!next.isAfter(now)) {
            next = next.plusDays(1);
        }
        return Math.max(Duration.between(now, next).getSeconds(), 1L);
    }
}

