package ui.tabs;

import model.*;
import model.Event;
import ui.PlayExamApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GamePanel extends Tab {
    private static JButton enterAnswer;
    private JButton startGame;
    private JLabel finalGrade;
    private static JLabel questionBox;
    private JLabel attemptsCount;
    private static JTextField answerBox;
    private Exam exam;
    private List<Equation> equations;
    private String qcount = "0";
    private final int attempts = 3;
    private int grade = 0;
    private int tries;
    private int currentQuestionIndex = 0;

    public GamePanel(PlayExamApp controller) {
        super(controller);
        setLayout(new BorderLayout());
        this.exam = controller.getExam();

        Box topPanel = placeTopBox();
        Box questionAnswerPanel = placeQuestionBox();

        JPanel containerPanel = placeContainerPanel(questionAnswerPanel);
        JPanel navigationBox = getNavigationBox();

        add(topPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);
        add(navigationBox, BorderLayout.SOUTH);

        getStartGameListener();
        getEnterListener();
    }

    private void getEnterListener() {
        enterAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAnswer();
            }
        });
    }

    private void getStartGameListener() {
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame.setVisible(false);
                answerBox.setText("");
                runGame();
            }
        });
    }

    private static JPanel placeContainerPanel(Box questionAnswerPanel) {
        Box centerPanel = Box.createVerticalBox();
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(questionAnswerPanel);

        JPanel containerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.NONE;
        containerPanel.add(centerPanel, gbc);
        return containerPanel;
    }

    private void runGame() {
        this.exam.shuffleEquations();
        this.equations = exam.getEquations();
        this.qcount = Integer.toString(exam.getEquationCount());
        this.currentQuestionIndex = 0;
        this.tries = 0;
        displayCurrentQuestion();
        resetTopBar();
    }

    private void displayCurrentQuestion() {
        if (currentQuestionIndex < equations.size()) {
            Equation currentEquation = equations.get(currentQuestionIndex);
            questionBox.setText(currentEquation.getExpression() + " =");
        } else {
            questionBox.setText("Game Over.");
        }
    }

    private void submitAnswer() {
        if (currentQuestionIndex < equations.size()) {
            Equation currentEquation = equations.get(currentQuestionIndex);
            String userInput = answerBox.getText();

            if (checkUserAnswer(currentEquation, userInput)) {
                correctAnswer();
            } else {
                tries++;
                attemptsCount.setText("Attempts: " + (attempts - tries) + " left");
                if (tries >= 3) {
                    wrongAnswer();
                }
            }
            answerBox.setText("");
        }
    }


    private void wrongAnswer() {
        currentQuestionIndex++;
        resetTopBar();
        tries = 0;
        if (!equations.isEmpty()) {
            displayCurrentQuestion();
        } else {
            questionBox.setText("Game Over.");
            resetGrade();
        }
    }

    private void correctAnswer() {
        EventLog.getInstance().logEvent(
                new Event("Equation: " + equations.get(
                        currentQuestionIndex).getExpression() + " was removed."));
        equations.remove(currentQuestionIndex);
        grade++;
        tries = 0;
        resetTopBar();

        if (!equations.isEmpty() && currentQuestionIndex < equations.size()) {
            displayCurrentQuestion();
        } else {
            questionBox.setText("Game Over.");
            resetGrade();
        }
    }



    private void resetTopBar() {
        resetAttempts();
        resetGrade();
    }

    private void resetAttempts() {
        attemptsCount.setText("Attempts: " + attempts + " left");
    }

    private void resetGrade() {
        finalGrade.setText("Your grade: " + " " + grade + " / " + qcount);
    }

    private JPanel getNavigationBox() {
        JPanel navigationBox = new JPanel(new GridLayout(1, 2));
        JButton homeButton = new JButton("Back to Home");
        getButtonListener(homeButton, PlayExamApp.HOME_TAB_INDEX);

        JButton examButton = new JButton("View exam");
        getButtonListener(examButton, PlayExamApp.EXAM_TAB_INDEX);

        navigationBox.add(homeButton);
        navigationBox.add(examButton);
        return navigationBox;
    }

    private static Box placeQuestionBox() {
        questionBox = new JLabel("Start");
        questionBox.setFont(new Font("Sans Serif", Font.BOLD, 30));
        answerBox = new JTextField("Type Answer", 10);
        enterAnswer = new JButton("Enter");
        JLabel emptyBig = new JLabel("     ");
        JLabel emptySmall = new JLabel(" ");

        Box questionAnswerPanel = Box.createHorizontalBox();
        questionAnswerPanel.add(questionBox);
        questionAnswerPanel.add(emptyBig);
        questionAnswerPanel.add(answerBox);
        questionAnswerPanel.add(emptySmall);
        questionAnswerPanel.add(enterAnswer);
        return questionAnswerPanel;
    }

    private Box placeTopBox() {
        attemptsCount = new JLabel("Attempts: " + attempts + " left");
        attemptsCount.setFont(new Font("Calibre", Font.BOLD, 15));

        finalGrade = new JLabel("Your grade: " + " " + grade + " / " + qcount);
        finalGrade.setFont(new Font("Calibre", Font.BOLD, 15));

        Box topPanel = Box.createHorizontalBox();
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(attemptsCount);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(finalGrade);
        topPanel.add(Box.createHorizontalGlue());

        startGame = new JButton("Start Game");
        topPanel.add(startGame);
        return topPanel;
    }

    private void getButtonListener(JButton button, int tabIndex) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(button)) {
                    getController().getSidebar().setSelectedIndex(tabIndex);
                }
            }
        });
    }

    private boolean checkUserAnswer(Equation equation, String userInput) {
        try {
            int userAnswer = Equation.parseUserAnswer(userInput); // Assuming this method is accessible
            return userAnswer == equation.evaluate(); // Assuming you can call evaluate() directly
        } catch (InvalidInputException | InvalidEquationException e) {
            JOptionPane.showMessageDialog(null, "Invalid input or equation. Please try again.");
            return false;
        }
    }

    public void updateGameWithNewExam(Exam newExam) {

        this.exam = newExam;
        refreshGameDisplay();
    }

    private void refreshGameDisplay() {
        if (!exam.getEquations().isEmpty()) {
            questionBox.setText("Start");
        }
    }


}
