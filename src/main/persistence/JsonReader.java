package persistence;

import model.Equation;
import model.Event;
import model.EventLog;
import model.Exam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;


// Represents a reader that reads an exam from a stored JSON file
public class JsonReader {

    private String source;

    // FROM JsonSerializationDemo by Paul Carter
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: read exam from file and return it;
    // throws IOException if file cannot be found
    public Exam read() throws IOException  {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        EventLog.getInstance().logEvent(
                new Event("Exam from file: " + source + " was loaded."));
        return parseExam(jsonObject);
    }

    // EFFECTS: reads source as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }


    // EFFECTS: parse exam from JSON object and returns it
    private Exam parseExam(JSONObject jsonObject) {
        Exam myExam = new Exam();
        addEquations(myExam, jsonObject);

        return myExam;
    }

    // MODIFIES: myExam
    // EFFECTS: parse equations from JSON object and add to exam
    private void addEquations(Exam myExam, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Equations");
        for (Object json : jsonArray) {
            JSONObject nextEqn = (JSONObject) json;
            addEquation(myExam, nextEqn);
        }
    }

    // MODIFIES: myExam
    // EFFECTS: parse equation from JSON object and add to exam
    private void addEquation(Exam myExam, JSONObject jsonObject) {
        String expression = jsonObject.getString("Expression");
        int solution = jsonObject.getInt("Solution");
        Equation equation = new Equation(solution, expression);
        myExam.addEquation(equation);
    }

}
