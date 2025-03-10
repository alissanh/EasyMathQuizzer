package ui.tabs;

import ui.PlayExamApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class HomeTab extends Tab {

    private static final String GREETING = "Welcome. Make an Exam then test yourself!";
    private JLabel greeting;
    private JPanel buttonBlock;

    // EFFECTS: instantiate HomeTab with an image, welcome message, buttons to make exam and play game
    public HomeTab(PlayExamApp controller) {
        super(controller);

        JLabel emptyBlock = new JLabel("");
        emptyBlock.setPreferredSize(new Dimension(PlayExamApp.WIDTH, PlayExamApp.HEIGHT / 20));

        JLabel emptyBlockB = new JLabel("");
        emptyBlockB.setPreferredSize(new Dimension(PlayExamApp.WIDTH, PlayExamApp.HEIGHT / 20));
        this.add(emptyBlock);

        placeImage();
        buttonBlock = new JPanel(new GridLayout(5,1));
        this.add(emptyBlockB);
        this.add(buttonBlock);
        placeGreeting();
        placeButtons();
    }

    // EFFECTS: place image onto homepage
    private void placeImage() {
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("data/pexels-monstera-production-6238068.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JLabel picLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance(
                PlayExamApp.WIDTH - (PlayExamApp.WIDTH / 5),
                PlayExamApp.HEIGHT / 3, Image.SCALE_FAST)));

        this.add(picLabel);
    }

    // EFFECTS: place welcome message to players
    public void placeGreeting() {
        greeting = new JLabel(GREETING, JLabel.CENTER);
        greeting.setFont(new Font("Calibre", Font.PLAIN, 28));
        buttonBlock.add(greeting);
    }

    // EFFECTS: create Make Exam button that redirects to the Exam tab,
    //          create Play Game button that redirects to Game tab
    public void placeButtons() {
        JButton examButton = new JButton("Make Exam");
        JButton playButton = new JButton("Play Game");

        JPanel buttonRow = formatButtonRow(examButton);
        buttonRow.add(playButton);
        buttonRow.setSize(WIDTH, HEIGHT / 6);
        JLabel emptyBlock = new JLabel("");
        buttonBlock.add(emptyBlock);
        buttonBlock.add(buttonRow);

        getTabListener(examButton, "Make Exam", PlayExamApp.EXAM_TAB_INDEX);
        getTabListener(playButton, "Play Game", PlayExamApp.GAME_TAB_INDEX);
    }

    // EFFECTS: direct buttons to each tab given button, button name, and tab index
    private void getTabListener(JButton button, String buttonName, int tabIndex) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonPressed = e.getActionCommand();
                if (buttonPressed.equals(buttonName)) {
                    getController().getSidebar().setSelectedIndex(tabIndex);
                }
            }
        });
    }

}
