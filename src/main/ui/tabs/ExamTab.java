package ui.tabs;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.PlayExamApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamTab extends Tab {
    private JLabel examTitle;
    private JScrollPane examPane;
    private Exam exam;
    private ArrayList<Equation> equations;
    private JLabel errorBlock;
    private JTextField txtInput;
    private DefaultListModel<String> model;

    private static final String JSON_STORE = "./data/my-exam.json";
    private static final String SAVE_MESSAGE = "Saved exam to " + JSON_STORE;
    private static final String SAVE_ERROR_MESSAGE = "Unable to write to file: " + JSON_STORE;
    private static final String LOAD_MESSAGE = "Loaded exam from " + JSON_STORE;
    private static final String LOAD_ERROR_MESSAGE = "Unable to read from file: " + JSON_STORE;
    private static final String CLEARED_MESSAGE = "Cleared exam";
    private static final String INVALID_MESSAGE = "Invalid input.";
    private JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    private JsonReader jsonReader = new JsonReader(JSON_STORE);
    private static final String EXAM_TITLE = "My Exam";

    // EFFECTS: make an ExamTab with:
    // title, empty display panel, user input, the following buttons:
    // add equation, save exam, load exam, clear exam, back to home
    public ExamTab(PlayExamApp controller) {
        super(controller);

        this.exam = getController().getExam();
        this.equations = exam.getEquations();

        JPanel examBlock = new JPanel(new GridLayout(3, 4));
        JLabel emptyBlock = new JLabel("");
        emptyBlock.setFont(new Font("Calibre", Font.PLAIN, 30));

        examTitle = new JLabel(EXAM_TITLE);
        examTitle.setFont(new Font("Calibre", Font.PLAIN, 30));

        errorBlock = new JLabel("");

        examBlock.add(emptyBlock);
        examBlock.add(examTitle);
        examBlock.add(errorBlock);

        errorBlock.setHorizontalAlignment(JLabel.CENTER);

        add(examBlock);
        placeUserInput();
        placeButtons();

        this.setVisible(true);
    }

    // EFFECTS: place the buttons
    public void placeButtons() {
        JButton saveButton = new JButton("Save Exam");
        getSaveDataListener(saveButton);

        JButton loadButton = new JButton("Load Saved Exam");
        getLoadButtonListener(loadButton);

        JButton homeButton = new JButton("Back to Home");
        openHomeTab(homeButton);

        JButton clearButton = new JButton("Clear exam");
        getClearButtonListener(clearButton);

        this.add(saveButton);
        this.add(loadButton);
        this.add(clearButton);
        this.add(homeButton);
    }

    // EFFECTS: display message in red text when there is an error
    private void displayErrorMessage(String message) {
        errorBlock.setText(message);
        errorBlock.setFont(new Font("Dialog", Font.BOLD, 15));
        errorBlock.setForeground(Color.red);
        txtInput.setText("");

    }

    // EFFECTS display message in black text when specified actions are finished
    private void displayMessage(String message) {
        errorBlock.setText(message);
        errorBlock.setFont(new Font("Dialog", Font.BOLD, 15));
        errorBlock.setForeground(Color.black);
        txtInput.setText("");

    }

    // MODIFIES: this
    // EFFECTS: clear all equations in the exam
    private void getClearButtonListener(JButton clearButton) {
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(clearButton)) {
                    exam.clearExam();
                    model.removeAllElements();
                    displayMessage(CLEARED_MESSAGE);
                }
            }
        });
    }

    // EFFECTS: move to home tab when clicked
    private void openHomeTab(JButton homeButton) {
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(homeButton)) {
                    getController().getSidebar().setSelectedIndex(PlayExamApp.HOME_TAB_INDEX);
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: load the saved exam onto the game
    private void getLoadButtonListener(JButton loadButton) {
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(loadButton)) {
                    try {
                        Exam loadedExam = jsonReader.read();
                        getController().setExam(loadedExam);

                        for (Equation equation : loadedExam.getEquations()) {
                            model.addElement(equation.getExpression()); // Assuming this is how questions are displayed
                        }

                        getController().getGameTab().updateGameWithNewExam(loadedExam);

                        displayMessage(LOAD_MESSAGE);
                    } catch (IOException exception) {
                        displayErrorMessage(LOAD_ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    // EFFECTS: save the exam on to specified JSON file
    private void getSaveDataListener(JButton saveButton) {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(saveButton)) {
                    try {
                        jsonWriter.open();
                        jsonWriter.write(exam);
                        jsonWriter.close();

                        EventLog.getInstance().logEvent(
                                new Event("Current exam was saved to " + JSON_STORE));
                        displayMessage(SAVE_MESSAGE);
                    } catch (FileNotFoundException exception) {
                        displayErrorMessage(SAVE_ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: add equation to exam
    private void getAddedActionListener(DefaultListModel<String> model, JButton addButton) {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(addButton)) {
                    try {
                        errorBlock.setText("");
                        Equation equation = Equation.parseEquation(txtInput.getText());
                        exam.addEquation(equation);
                        model.addElement(txtInput.getText());
                        txtInput.setText("");
                    } catch (InvalidEquationException | InvalidInputException exception) {
                        displayErrorMessage(INVALID_MESSAGE);
                    }
                }
            }
        }
        );
    }

    // EFFECTS: place input mechanism
    public void placeUserInput() {
        List<Equation> equations = getController().getExam().getEquations();

        model = new DefaultListModel<String>();

        for (Equation e : equations) {
            model.addElement(e.getExpression());
        }


        JList<String> list = new JList<String>(model);
        list.setFont(new Font("Calibre",Font.BOLD,20));
        examPane = new JScrollPane(list);
        examPane.setPreferredSize(new Dimension(PlayExamApp.WIDTH - (PlayExamApp.WIDTH / 5),
                PlayExamApp.HEIGHT - (PlayExamApp.HEIGHT / 2)));


        txtInput = new JTextField("");
        JButton addButton = new JButton("Add Equation");


        this.add(examPane);
        this.add(txtInput);
        this.add(addButton);


        getAddedActionListener(model, addButton);
        errorBlock.setText("");

        txtInput.setColumns(10);

    }

}
