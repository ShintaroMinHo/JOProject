package p1;
import java.util.List;
import java.time.LocalDateTime;

public class CompetitionEvent {
    private int eventId;
    private SportEvent sportEvent;
    private LocalDateTime dateTime;
    private String location;
    private List<Athlete> participants;

    public CompetitionEvent(int eventId, SportEvent sportEvent, LocalDateTime dateTime, String location, List<Athlete> participants) {
        this.eventId = eventId;
        this.sportEvent = sportEvent;
        this.dateTime = dateTime;
        this.location = location;
        this.participants = participants;
    }

    public int getEventId() {
        return eventId;
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

    public List<Athlete> getParticipants() {
        return participants;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setSportEvent(SportEvent sportEvent) {
        this.sportEvent = sportEvent;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setParticipants(List<Athlete> participants) {
        this.participants = participants;
    }


}
