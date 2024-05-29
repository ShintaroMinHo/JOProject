package p2;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import UI.MedalsAnalysisUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import p1.*;


public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/olympics_management?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Zst1117.com";

    // Connect to MySQL database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Load all athletes from the database
    public static List<Athlete> loadAthletes() {
        List<Athlete> athletes = new ArrayList<>();
        String sql = "SELECT id, name, nationality, age, gender FROM Athletes";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String nationality = rs.getString("nationality");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                // Assuming no event details are loaded here
                athletes.add(new Athlete(id, name, nationality, age, gender, new ArrayList<>()));
            }
        } catch (SQLException e) {
            System.out.println("Error loading athletes from the database");
            e.printStackTrace();
        }
        return athletes;
    }

    public static void addAthlete(Athlete athlete) {
        String sql = "INSERT INTO Athletes (name, nationality, age, gender) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, athlete.getName());
            pstmt.setString(2, athlete.getNationality());
            pstmt.setInt(3, athlete.getAge());
            pstmt.setString(4, athlete.getGender());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding athlete to the database");
            e.printStackTrace();
        }
    }

    public static List<EventWithDateTime> loadEvents() {
        List<EventWithDateTime> events = new ArrayList<>();
        String sql = "SELECT competitionId, eventId, dateTime, location FROM competitionevents"; // 确保表名正确

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int competitionId = rs.getInt("competitionId");
                int eventId = rs.getInt("eventId");
                LocalDateTime dateTime = rs.getTimestamp("dateTime").toLocalDateTime();
                String location = rs.getString("location");
                SportEvent sportEvent = findSportEventById(eventId);
                events.add(new EventWithDateTime(sportEvent, dateTime, location));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    public static List<CompetitionEvent> loadCompetitionEvents(int sportEventId) {
        List<CompetitionEvent> competitionEvents = new ArrayList<>();
        String sql = "SELECT c.competitionId, c.eventId, c.dateTime, c.location " +
                "FROM competitionevents c WHERE c.eventId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sportEventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int competitionId = rs.getInt("competitionId");
                    int eventId = rs.getInt("eventId");
                    LocalDateTime dateTime = rs.getTimestamp("dateTime").toLocalDateTime();
                    String location = rs.getString("location");

                    // Load participants for this competition event
                    List<Athlete> participants = loadParticipants(competitionId);

                    // Get the SportEvent associated with this competition event
                    SportEvent sportEvent = getSportEventById(eventId);

                    competitionEvents.add(new CompetitionEvent(competitionId, sportEvent, dateTime, location, participants));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competitionEvents;
    }

    private static List<Athlete> loadParticipants(int competitionId) {
        List<Athlete> participants = new ArrayList<>();
        String sql = "SELECT a.id, a.name, a.nationality, a.age, a.gender " +
                "FROM athletes a " +
                "JOIN competition_participants cp ON a.id = cp.athleteId " +
                "WHERE cp.competitionId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, competitionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String nationality = rs.getString("nationality");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    participants.add(new Athlete(id, name, nationality, age, gender,List<SportEvent> events);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    private static SportEvent getSportEventById(int eventId) {
        String sql = "SELECT eventId, name, type FROM sportevents WHERE eventId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    return new SportEvent(eventId, name, type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addParticipantToCompetition(int competitionId, int athleteId) {
        String sql = "INSERT INTO competition_participants (competitionId, athleteId) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, competitionId);
            pstmt.setInt(2, athleteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCompetitionEvent(CompetitionEvent competitionEvent) {
        String sql = "INSERT INTO competitionevents (eventId, dateTime, location) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, competitionEvent.getSportEvent().getEventId());
            pstmt.setTimestamp(2, Timestamp.valueOf(competitionEvent.getDateTime()));
            pstmt.setString(3, competitionEvent.getLocation());
            pstmt.executeUpdate();

            // Get generated competitionId
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    competitionEvent.setEventId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCompetitionEvent(CompetitionEvent competitionEvent) {
        String sql = "UPDATE competitionevents SET eventId = ?, dateTime = ?, location = ? WHERE competitionId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, competitionEvent.getSportEvent().getEventId());
            pstmt.setTimestamp(2, Timestamp.valueOf(competitionEvent.getDateTime()));
            pstmt.setString(3, competitionEvent.getLocation());
            pstmt.setInt(4, competitionEvent.getEventId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCompetitionEvent(int competitionId) {
        String sql = "DELETE FROM competitionevents WHERE competitionId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, competitionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SportEvent findSportEventById(int eventId) {
        List<SportEvent> sportEvents = loadSportEvents();
        for (SportEvent event : sportEvents) {
            if (event.getEventId() == eventId) {
                return event;
            }
        }
        return null;
    }

    public static List<SportEvent> loadSportEvents() {
        List<SportEvent> sportEvents = new ArrayList<>();
        String sql = "SELECT eventId, name, type FROM sportevents";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int eventId = rs.getInt("eventId");
                String name = rs.getString("name");
                String type = rs.getString("type");
                sportEvents.add(new SportEvent(eventId, name, type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sportEvents;
    }

    // Method to delete an athlete from the database
    public static void deleteAthlete(int id) {
        String sql = "DELETE FROM Athletes WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting athlete from the database");
            e.printStackTrace();
        }
    }


    public static void addEvent(SportEvent event) {
        String sql = "INSERT INTO SportEvents (name, type) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getType());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        event.setEventId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding an event to the database");
            e.printStackTrace();
        }
    }

    // Method to update an event in the database
    public static void updateEvent(SportEvent event) {
        String sql = "UPDATE SportEvents SET name = ?, type = ? WHERE eventId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getType());
            pstmt.setInt(3, event.getEventId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating event in the database");
            e.printStackTrace();
        }
    }

    // Method to delete an event from the database
    public static void deleteEvent(int eventId) {
        String sql = "DELETE FROM SportEvents WHERE eventId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting event from the database");
            e.printStackTrace();
        }
    }



    public static ObservableList<XYChart.Data<String, Number>> loadMedalCountsByNationality() {
        ObservableList<XYChart.Data<String, Number>> medalData = FXCollections.observableArrayList();
        String sql = "SELECT a.nationality, COUNT(r.medal) AS total FROM Results r " +
                "JOIN Athletes a ON r.athleteId = a.id " +
                "GROUP BY a.nationality";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String nationality = rs.getString("nationality");
                Number count = rs.getInt("total");
                medalData.add(new XYChart.Data<>(nationality, count));
            }
        } catch (SQLException e) {
            System.out.println("Error loading medal counts from the database: " + e.getMessage());
            e.printStackTrace();
        }
        return medalData;
    }


    public static ObservableList<MedalsAnalysisUI.MedalDetail> loadMedalDetails() {
        ObservableList<MedalsAnalysisUI.MedalDetail> details = FXCollections.observableArrayList();
        // Assume Athletes has 'name' and 'nationality', and they are joined with Results on athleteId
        String sql = "SELECT a.name AS athleteName, a.nationality, r.medal FROM Results r " +
                "JOIN Athletes a ON r.athleteId = a.id";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String nationality = rs.getString("nationality");
                String athleteName = rs.getString("athleteName");
                String medal = rs.getString("medal");
                details.add(new MedalsAnalysisUI.MedalDetail(nationality, athleteName, medal));
            }
        } catch (SQLException e) {
            System.out.println("Error loading medal details from the database");
            e.printStackTrace();
        }
        return details;
    }
    public static List<Result> loadResults() {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT competitionEventId, athleteId, score, `rank`, medal FROM Results";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                results.add(new Result(
                        rs.getInt("competitionEventId"),
                        rs.getInt("athleteId"),
                        rs.getDouble("score"),
                        rs.getInt("rank"),
                        rs.getString("medal")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error loading results from the database");
            e.printStackTrace();
        }
        return results;
    }


    // Add a result to the database
    public static void addResult(Result result) {
        String sql = "INSERT INTO Results (competitionEventId, athleteId, score, `rank`, medal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, result.getCompetitionEventId());
            pstmt.setInt(2, result.getAthleteId());
            pstmt.setDouble(3, result.getScore());
            pstmt.setInt(4, result.getRank());
            pstmt.setString(5, result.getMedal());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding a result to the database");
            e.printStackTrace();
        }
    }

    // Update a result in the database
    public static void updateResult(Result result) {
        String sql = "UPDATE Results SET score = ?, `rank` = ?, medal = ? WHERE competitionEventId = ? AND athleteId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, result.getScore());
            pstmt.setInt(2, result.getRank());
            pstmt.setString(3, result.getMedal());
            pstmt.setInt(4, result.getCompetitionEventId());
            pstmt.setInt(5, result.getAthleteId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating a result in the database");
            e.printStackTrace();
        }
    }

    public static void deleteResult(int competitionEventId, int athleteId) {
        String sql = "DELETE FROM Results WHERE competitionEventId = ? AND athleteId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, competitionEventId);
            pstmt.setInt(2, athleteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting a result from the database");
            e.printStackTrace();
        }
    }

    

    // Main method for testing connectivity
    public static void main(String[] args) {
        try {
            connect();
            System.out.println("Connected to the database successfully.");
            // Optionally test loading athletes
            List<Athlete> athletes = loadAthletes();
            athletes.forEach(a -> System.out.println(a.getName() + " from " + a.getNationality()));
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        }
    }
}

