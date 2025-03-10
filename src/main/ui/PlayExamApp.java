package ui;

import model.Event;
import model.EventLog;
import model.Exam;
import ui.tabs.ExamTab;
import ui.tabs.GamePanel;
import ui.tabs.HomeTab;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PlayExamApp extends JFrame {
    public static final int HOME_TAB_INDEX = 0;
    public static final int GAME_TAB_INDEX = 1;
    public static final int EXAM_TAB_INDEX = 2;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Exam myExam;
    private final JTabbedPane sidebar;
    private JPanel homeTab;
    private GamePanel gameTab;
    private ExamTab examTab;

    public static void main(String[] args) {
        new PlayExamApp();
    }

    // Main window where game will be played
    public PlayExamApp() {
        super("Exam Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        myExam = new Exam();
        
        sidebar = new JTabbedPane();
        sidebar.setTabPlacement(JTabbedPane.LEFT);
        
        loadTabs();
        add(sidebar);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
            }
        });
    }

    // EFFECTS: print event log when window is closed
    private void printLog(EventLog instance) {
        for (Event event: instance) {
            System.out.println(event.toString());
        }
    }

    // EFFECTS: return Exam object controlled by this UI
    public Exam getExam() {
        return myExam;
    }

    // MODIFIES: this
    // EFFECTS: sets exam to new exam
    public void setExam(Exam exam) {
        myExam = exam;
    }

    // MODIFIES: this
    // EFFECTS: adds Game tab and Exam tab
    public void loadTabs() {
        homeTab = new HomeTab(this);
        gameTab = new GamePanel(this);
        examTab = new ExamTab(this);

        sidebar.add(homeTab);
        sidebar.setTitleAt(HOME_TAB_INDEX, "Home");
        sidebar.add(gameTab);
        sidebar.setTitleAt(GAME_TAB_INDEX, "Play Game");
        sidebar.add(examTab);
        sidebar.setTitleAt(EXAM_TAB_INDEX, "View my Exam");

    }

    // EFFECTS: return sidebar object
    public JTabbedPane getSidebar() {
        return sidebar;
    }

    // EFFECTS: return gamePanel instance
    public GamePanel getGameTab() {
        return gameTab;
    }


}
