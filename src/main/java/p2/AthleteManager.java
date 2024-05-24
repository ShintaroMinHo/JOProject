package p2;
import p1.Athlete;

import java.util.ArrayList;
import java.util.List;

public class AthleteManager {
    private List<Athlete> athletes;

    public AthleteManager() {
        this.athletes = new ArrayList<>();
    }

    public void addAthlete(Athlete athlete) {
        athletes.add(athlete);
    }

    public void deleteAthlete(Athlete athlete) {
        athletes.remove(athlete);
    }

    public void updateAthlete(Athlete oldAthlete, Athlete newAthlete) {
        int index = athletes.indexOf(oldAthlete);
        if (index != -1) {
            athletes.set(index, newAthlete);
        }
    }

    public List<Athlete> searchAthletes(String name) {
        List<Athlete> found = new ArrayList<>();
        for (Athlete athlete : athletes) {
            if (athlete.getName().equalsIgnoreCase(name)) {
                found.add(athlete);
            }
        }
        return found;
    }

}
