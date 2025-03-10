package ui;

import model.Equation;
import model.Exam;
import model.InvalidEquationException;
import model.InvalidInputException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ExamApp {
    private static final String JSON_STORE = "./data/my-exam.json";
    private Exam myExam;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private int grade;
    private List<Equation> equations;


    // EFFECTS: create a new exam
    public ExamApp() {
        input = new Scanner(System.in);
        this.myExam = new Exam();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        grade = 0;
    }

    // MODIFIES: this
    // EFFECTS: processes user input and run game
    public void run() {
        input = new Scanner(System.in);
        System.out.println("Enter an equation (e.g., 6 + 4) to add it to the exam, "
                + "\n" + "type the operator to display added equations of the same type (e.g, +) or 'Print' for all"
                + "\n" + "type 'Save' to save exam to file"
                + "\n" + "type 'Load' to load exam from file"
                + "\n" + "type 'Done' to finish");

        while (true) {
            try {
                if (!processInput(input)) {
                    break;
                }
            } catch (InvalidInputException | InvalidEquationException e) {
                System.out.println("Invalid input.");
            }
        }

        if (myExam.getEquationCount() > 0) {
            myExam.shuffleEquations();
            int grade = playGame();
            System.out.println("Your grade: " + grade + " out of " + myExam.getEquationCount());
        } else {
            System.out.println("No equations added. Exiting.");
        }

        input.close();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public boolean processInput(Scanner scanner) throws InvalidInputException, InvalidEquationException {
        System.out.println("Enter an equation, an operator, 'print', or 'done'");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("Done")) {
            return false;
        } else if (input.length() == 1 && input.matches("\\+|\\*|=|/|-")) {
            displayEquationOperator(input);
        } else if (input.equalsIgnoreCase("Print")) {
            displayEquations();
        } else if (input.equalsIgnoreCase("Save")) {
            saveExam();
            return false;
        } else if (input.equalsIgnoreCase("Load")) {
            loadExam();
            return false;
        } else {
            try {
                Equation parsedEquation = Equation.parseEquation(input);
                myExam.addEquation(parsedEquation);
            } catch (InvalidInputException e) {
                System.out.println("Invalid input. ");
            }
        }
        return true;
    }

    // EFFECTS: display equations in a randomized order
    //          parse user answer and check if it is a correct answer
    //          give users 3 incorrect tries before moving to the next equation
    //          return an integer of how many equations the user got correct
    public int playGame() {
        equations = myExam.getEquations();
        for (Equation equation : equations) {
            int tries = 0;

            while (tries < 3) {
                System.out.println("Solve");
                equation.display();
                System.out.print("Your answer: ");

                try {
                    if (equation.checkAnswer()) {
                        System.out.println("Correct!");
                        this.grade++;
                        break;
                    }

                } catch (InvalidEquationException e) {
                    System.out.println("Invalid input. ");
                }

                tries++;
            }

            if (tries == 3) {
                System.out.println("Out of Attempts. Next Question.");
            }
        }

        return this.grade;
    }

    // EFFECTS: display all equations to user
    public void displayEquations() {
        equations = myExam.getEquations();
        if (myExam.getEquationCount() == 0) {
            System.out.println("No equations added yet.");
        } else {
            System.out.println("Added equations:");
            for (Equation equation : equations) {
                equation.display();
            }
        }
    }

    // EFFECTS: display all equations to user
    public void displayEquationOperator(String input) {
        List<Equation> newEquations = myExam.getEquationByOperator(input);
        if (newEquations.size() == 0) {
            System.out.println("No equations with the" + " " + input + " " + "operator added yet.");
        } else {
            System.out.println("Added equations with the" + " " + input + " " + "operator:");
            for (Equation equation : newEquations) {
                equation.display();
            }
        }

    }

    // EFFECTS: saves exam to file
    private void saveExam() {
        try {
            jsonWriter.open();
            jsonWriter.write(myExam);
            jsonWriter.close();
            System.out.println("Saved exam to " + JSON_STORE);

        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: load exam from saved file
    private void loadExam() {
        try {
            myExam = jsonReader.read();
            System.out.println("Loaded exam from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}