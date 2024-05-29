package p1;
import java.time.LocalDateTime;

public class EventWithDateTime {
    private SportEvent sportEvent;
    private LocalDateTime dateTime;
    private String location;

    public EventWithDateTime(SportEvent sportEvent, LocalDateTime dateTime, String location) {
        this.sportEvent = sportEvent;
        this.dateTime = dateTime;
        this.location = location;
    }

    public SportEvent getSportEvent() {
        return sportEvent;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return sportEvent.getName() + " at " + dateTime + " in " + location;
    }
}
