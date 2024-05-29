package p2;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import p1.Athlete;
import p1.Result;
import p1.SportEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportManager {

    public static void generatePerformanceReportCSV(List<Athlete> athletes, List<Result> results, List<SportEvent> events, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Athlete Performance Report\n");
            writer.append("ID,Name,Nationality,Event,Score,Rank,Medal\n");
            for (Result result : results) {
                Athlete athlete = findAthleteById(athletes, result.getAthleteId());
                SportEvent event = findEventById(events, result.getCompetitionEventId());
                if (athlete != null && event != null) {
                    writer.append(String.valueOf(athlete.getId())).append(",")
                            .append(athlete.getName()).append(",")
                            .append(athlete.getNationality()).append(",")
                            .append(event.getName()).append(",")
                            .append(String.valueOf(result.getScore())).append(",")
                            .append(String.valueOf(result.getRank())).append(",")
                            .append(result.getMedal()).append("\n");
                }
            }
            System.out.println("CSV Report generated successfully at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generatePerformanceReportPDF(List<Athlete> athletes, List<Result> results, List<SportEvent> events, String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph("Athlete Performance Report"));
            document.add(new Paragraph("ID, Name, Nationality, Event, Score, Rank, Medal"));
            for (Result result : results) {
                Athlete athlete = findAthleteById(athletes, result.getAthleteId());
                SportEvent event = findEventById(events, result.getCompetitionEventId());
                if (athlete != null && event != null) {
                    document.add(new Paragraph(
                            athlete.getId() + ", " + athlete.getName() + ", " + athlete.getNationality() + ", " +
                                    event.getName() + ", " + result.getScore() + ", " + result.getRank() + ", " + result.getMedal()));
                }
            }
            System.out.println("PDF Report generated successfully at: " + filePath);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static Athlete findAthleteById(List<Athlete> athletes, int athleteId) {
        for (Athlete athlete : athletes) {
            if (athlete.getId() == athleteId) {
                return athlete;
            }
        }
        return null;
    }

    private static SportEvent findEventById(List<SportEvent> events, int eventId) {
        for (SportEvent event : events) {
            if (event.getEventId() == eventId) {
                return event;
            }
        }
        return null;
    }
}
