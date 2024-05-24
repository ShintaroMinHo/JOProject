package p2;

import p1.CompetitionEvent;
import p1.SportEvent;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<SportEvent> sportEvents;
    private List<CompetitionEvent> competitionEvents;

    public EventManager() {
        this.sportEvents = new ArrayList<>();
        this.competitionEvents = new ArrayList<>();
    }

    public void addSportEvent(SportEvent event) {
        sportEvents.add(event);
    }

    public void deleteSportEvent(SportEvent event) {
        sportEvents.remove(event);
    }

    public void addCompetitionEvent(CompetitionEvent event) {
        competitionEvents.add(event);
    }

    public void deleteCompetitionEvent(CompetitionEvent event) {
        competitionEvents.remove(event);
    }

    public void updateCompetitionEvent(CompetitionEvent oldEvent, CompetitionEvent newEvent) {
        int index = competitionEvents.indexOf(oldEvent);
        if (index != -1) {
            competitionEvents.set(index, newEvent);
        }
    }
}