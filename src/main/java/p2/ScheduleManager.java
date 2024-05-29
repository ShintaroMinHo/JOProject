package p2;

import p1.EventWithDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleManager {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void displaySchedule(List<EventWithDateTime> events) {
        for (EventWithDateTime event : events) {
            System.out.println(event);
        }
    }

    public static void scheduleNotifications(List<EventWithDateTime> events) {
        for (EventWithDateTime event : events) {
            LocalDateTime eventTime = event.getDateTime();
            LocalDateTime notifyTime = eventTime.minusMinutes(30); // Notify 30 minutes before event

            long delay = java.time.Duration.between(LocalDateTime.now(), notifyTime).toMillis();
            if (delay > 0) {
                scheduler.schedule(() -> {
                    System.out.println("Reminder: " + event.getSportEvent().getName() + " starts in 30 minutes.");
                }, delay, TimeUnit.MILLISECONDS);
            }
        }
    }

    public static void shutdownScheduler() {
        scheduler.shutdown();
    }
}
