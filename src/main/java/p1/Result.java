package p1;

public class Result {
    private int competitionEventId;
    private int athleteId;
    private double score;
    private int rank;
    private String medal; // "Gold", "Silver", "Bronze", or "None"

    public Result(int competitionEventId, int athleteId, double score, int rank, String medal) {
        this.competitionEventId = competitionEventId;
        this.athleteId = athleteId;
        this.score = score;
        this.rank = rank;
        this.medal = medal;
    }

    public int getCompetitionEventId() {
        return competitionEventId;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public double getScore() {
        return score;
    }

    public int getRank() {
        return rank;
    }

    public String getMedal() {
        return medal;
    }

    public void setCompetitionEventId(int competitionEventId) {
        this.competitionEventId = competitionEventId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }

}