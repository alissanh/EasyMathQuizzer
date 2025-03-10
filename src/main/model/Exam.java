package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Exam implements Writable {
    private List<Equation> equations;
    private List<String> equationsString;
    private int grade;

    // EFFECTS: create an empty list of equations
    public Exam() {
        equations = new ArrayList<>();
        equationsString = new ArrayList<>();
        grade = 0;
    }

    // MODIFIES: this
    // EFFECTS: add equation to equations
    public void addEquation(Equation equation) {
        equations.add(equation);
        EventLog.getInstance().logEvent(
                new Event("Equation: " + equation.getExpression() + " was added."));
    }

    // EFFECTS: return the list of equations
    public ArrayList<Equation> getEquations() {
        return new ArrayList<>(equations); // Return a copy to prevent direct modification
    }


    // EFFECTS: return a sorted list of equations based on the operator
    public List<Equation> getEquationByOperator(String operator) {
        List<Equation> sortedlist = new ArrayList<>();
        for (Equation equation: equations) {
            if (equation.getOperator().equals(operator)) {
                sortedlist.add(equation);
            }
        }
        return sortedlist;
    }

    // EFFECTS: return the total count of equations
    public int getEquationCount() {
        return equations.size();
    }

    // MODIFIES: this
    // EFFECTS: randomize order of equations
    public void shuffleEquations() {
        Collections.shuffle(equations);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Equations", equationsToJson());
        return json;
    }

    // EFFECTS: return JSONArray of equations
    public JSONArray equationsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Equation e : equations) {
            jsonArray.put(e.toJson());

        }

        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: reset exam
    public void clearExam() {
        this.equations = new ArrayList<>();
    }
}