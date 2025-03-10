package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Scanner;

public class Equation implements Writable {
    private int solution;
    private String expression;

    // EFFECTS: create an equation where the body of the equation is equal to the expression
    //          and the answer is the solution
    public Equation(int solution, String expression) {
        this.solution = solution;
        this.expression = expression;
    }


    // EFFECTS: return the solution part of the equation
    public int getSolution() {
        return solution;
    }


    // EFFECTS: return the expression part of the equation
    public String getExpression() {
        return this.expression;
    }

    // EFFECTS: return operator of the equation
    public String getOperator() {
        String[] parts = expression.split("\\s+");
        if (parts.length != 3) {
            return "";
        }
        return parts[1].trim();
    }

    // EFFECTS: return the correct solution for an equation
    public int evaluate() throws InvalidEquationException {
        String[] parts = expression.split("\\s+");
        int operand1 = Integer.parseInt(parts[0]);
        int operand2 = Integer.parseInt(parts[2]);
        String operator = parts[1];

        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0) {
                    throw new InvalidEquationException();
                }
                return operand1 / operand2;
            default:
                throw new InvalidEquationException();
        }
    }

    // EFFECTS: turn user string input for solution into integer
    public static int parseUserAnswer(String input) throws InvalidInputException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }


    // EFFECTS: check if user input is the correct answer
    public boolean checkAnswer() throws InvalidEquationException {
        Scanner scanner = new Scanner(System.in);

        try {
            int userAnswer = parseUserAnswer(scanner.nextLine());
            return userAnswer == evaluate();
        } catch (InvalidInputException e) {
            System.out.println("Invalid input. ");
            return false;
        }
    }

    // EFFECTS: print out equation
    public void display() {
        System.out.println("Equation: " + expression);
    }

    // EFFECTS: turn user input for expression to an Equation
    public static Equation parseEquation(String input) throws InvalidEquationException, InvalidInputException {
        String[] parts = input.split("\\s+");
        if (parts.length != 3) {
            throw new InvalidEquationException();
        }

        int operand1 = parseNumber(parts[0]);
        int operand2 = parseNumber(parts[2]);
        String operator = parts[1];

        switch (operator) {
            case "+":
                return new Equation(operand1 + operand2, input);
            case "-":
                return new Equation(operand1 - operand2, input);
            case "*":
                return new Equation(operand1 * operand2, input);
            case "/":
                if (operand2 == 0) {
                    throw new InvalidEquationException();
                }
                return new Equation(operand1 / operand2, input);
            default:
                throw new InvalidEquationException();
        }
    }

    // EFFECTS: turn user string input for expression into integer
    public static int parseNumber(String input) throws InvalidInputException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Solution", solution);
        json.put("Expression", expression);
        return json;
    }
}