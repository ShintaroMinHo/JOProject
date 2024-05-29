package p2;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import UI.MedalsAnalysisUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import p1.Athlete;
import p1.Result;
import p1.SportEvent;

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

    public static List<SportEvent> loadEvents() {
        List<SportEvent> events = new ArrayList<>();
        String sql = "SELECT eventId, name, type FROM SportEvents";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int eventId = rs.getInt("eventId");
                String name = rs.getString("name");
                String type = rs.getString("type");
                events.add(new SportEvent(eventId, name, type));
            }
        } catch (SQLException e) {
            System.out.println("Error loading events from the database");
            e.printStackTrace();
        }
        return events;
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

