package persistence;

import model.Event;
import model.EventLog;
import model.Exam;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
public class JsonWriter {

    private PrintWriter writer;
    private String destination;

    // FROM: Paul Carter
    // EFFECTS: construct writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: open writer;
    // throws FileNotFound if file cannot be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of exam
    public void write(Exam exam) {
        JSONObject json = exam.toJson();
        saveToFile(json.toString());
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes equations to file
    public void saveToFile(String json) {
        writer.print(json);
        EventLog.getInstance().logEvent(
                new Event("Current exam was saved to file: " + json));
    }
}
