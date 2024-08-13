package codsoft_task1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class NumberGuessingGame {

    // Non-private variables
    int targetNo;
    int attemptsLeft;
    int roundsWon;
    int totalAttempts;

    JFrame frame;
    JTextField guessField;
    JTextArea feedbackArea;
    JButton submitButton;
    JButton playAgainButton;
    JButton setDifficultyButton;
    JLabel attemptsLabel;
    JLabel scoreLabel;
    JLabel difficultyLabel;
    JComboBox<String> difficultyComboBox;

    int minNo = 1;
    int maxNo = 100;
    int maxAttempts = 5;

    Random random = new Random();

    public NumberGuessingGame() {
        roundsWon = 0;
        SwingUtilities.invokeLater(this::showSplashScreen); // Start on EDT
    }

    private void showSplashScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame splashFrame = new JFrame("Loading");
        splashFrame.setSize(screenSize);
        splashFrame.setUndecorated(true); // Remove window decorations for splash screen
        splashFrame.setLayout(null); // Absolute positioning

        // Load and scale the image
        ImageIcon splashImageIcon = new ImageIcon("C:\\Users\\Administrator\\Downloads\\icons\\guess.jpg"); // Update the path
        Image img = splashImageIcon.getImage();
        Image scaledImg = img.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        splashImageIcon = new ImageIcon(scaledImg);

        JLabel imageLabel = new JLabel(splashImageIcon);
        imageLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        splashFrame.add(imageLabel);

        JLabel welcomeLabel = new JLabel("Welcome to the Number Guessing Game!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 60));
        welcomeLabel.setForeground(Color.BLUE);
        welcomeLabel.setBounds(0, screenSize.height / 2 - 100, screenSize.width, 100);
        splashFrame.add(welcomeLabel);

        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 46));
        loadingLabel.setForeground(Color.GREEN);
        loadingLabel.setBounds(0, screenSize.height / 2 + 20, screenSize.width, 50);
        splashFrame.add(loadingLabel);

        splashFrame.setLocationRelativeTo(null);
        splashFrame.setVisible(true);

        // Timer to dispose splash screen and initialize the main frame
        Timer timer = new Timer(3000, e -> {
            splashFrame.dispose();
            SwingUtilities.invokeLater(this::initialize); // Ensure initialize is called on EDT
        });
        timer.setRepeats(false); // Ensure the timer runs only once
        timer.start();
    }

    private void initialize() {
        if (frame != null) {
            frame.dispose(); // Dispose of existing frame if it exists
        }
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Number Guessing Game");
        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setUndecorated(true); // Optional: remove window decorations
        frame.setLayout(null); // Absolute positioning

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setBounds(50, 50, screenSize.width - 100, 150);

        JLabel guessLabel = new JLabel("Enter your guess:");
        guessLabel.setBounds(400,0, 500, 60);
        guessLabel.setFont(new Font("Serif", Font.BOLD, 45));
        inputPanel.add(guessLabel);

        guessField = new JTextField();
        guessField.setBounds(780, 17, 300, 30);
        inputPanel.add(guessField);

        submitButton = new JButton("Submit Guess");
        submitButton.setBounds(400, 80, 200, 30);
        submitButton.setBackground(Color.BLUE);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        inputPanel.add(submitButton);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(900, 80, 150, 30);
        playAgainButton.setEnabled(false);
        inputPanel.add(playAgainButton);

        submitButton.addActionListener(e -> handleGuess());
        playAgainButton.addActionListener(e -> startNewGame());

        frame.add(inputPanel);

        // Feedback Panel
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(null);
        feedbackPanel.setBounds(50, 220, screenSize.width - 100, 200);

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setBounds(0, 0, screenSize.width - 100, 150);
        feedbackPanel.add(scrollPane);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(null);
        statusPanel.setBounds(0, 160, screenSize.width - 100, 40);

        attemptsLabel = new JLabel("Attempts left: " + maxAttempts);
        attemptsLabel.setBounds(10, 10, 200, 30);
       // calculateButton.setBackground(Color.BLUE);
        attemptsLabel.setForeground(Color.BLUE);
        attemptsLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        statusPanel.add(attemptsLabel);

        scoreLabel = new JLabel("Rounds Won: " + roundsWon);
        scoreLabel.setBounds(800, 10, 200, 30);
        scoreLabel.setForeground(Color.RED);
        scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        statusPanel.add(scoreLabel);

        feedbackPanel.add(statusPanel);
        frame.add(feedbackPanel);

        // Difficulty Panel
        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(null);
        difficultyPanel.setBounds(50, 430, screenSize.width - 100, 150);

        difficultyLabel = new JLabel("Select Difficulty:");
        difficultyLabel.setBounds(500, 20, 300, 35);
        difficultyLabel.setForeground(Color.BLACK);
        difficultyLabel.setFont(new Font("SERIF", Font.PLAIN, 20));
        difficultyPanel.add(difficultyLabel);

        difficultyComboBox = new JComboBox<>(new String[]{"Easy (1-50)", "Medium (1-100)", "Hard (1-200)"});
        difficultyComboBox.setBounds(490, 60, 200, 30);
        difficultyPanel.add(difficultyComboBox);

        setDifficultyButton = new JButton("Set Difficulty");
        setDifficultyButton.setBounds(490, 100, 200, 30);
        setDifficultyButton.setBackground(Color.BLUE);
        setDifficultyButton.setForeground(Color.WHITE);
        setDifficultyButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        difficultyPanel.add(setDifficultyButton);

        setDifficultyButton.addActionListener(e -> {
            setDifficulty();
            startNewGame();
        });

        frame.add(difficultyPanel);
        frame.setVisible(true);
        startNewGame();
    }

    private void setDifficulty() {
        String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
        switch (selectedDifficulty) {
            case "Easy (1-50)":
                minNo = 1;
                maxNo = 50;
                maxAttempts = 10;
                break;
            case "Medium (1-100)":
                minNo = 1;
                maxNo = 100;
                maxAttempts = 7;
                break;
            case "Hard (1-200)":
                minNo = 1;
                maxNo = 200;
                maxAttempts = 5;
                break;
        }
    }

    private void startNewGame() {
        targetNo = random.nextInt(maxNo - minNo + 1) + minNo;
        attemptsLeft = maxAttempts;
        feedbackArea.setText("Guess the number between " + minNo + " and " + maxNo + ".\n");
        feedbackArea.setBackground(Color.WHITE);
        feedbackArea.setForeground(Color.GREEN);
        feedbackArea.setFont(new Font("Tahoma", Font.BOLD, 15));
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        playAgainButton.setEnabled(false);
    }

    private void handleGuess() {
        String input = guessField.getText();
        int guess;
        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            feedbackArea.append("Please enter a valid number.\n");
            return;
        }

        if (guess < minNo || guess > maxNo) {
            feedbackArea.append("Guess must be between " + minNo + " and " + maxNo + ".\n");
            return;
        }

        attemptsLeft--;
        if (guess < targetNo) {
            feedbackArea.append("Too low! Try again.\n");
        } else if (guess > targetNo) {
            feedbackArea.append("Too high! Try again.\n");
        } else {
         feedbackArea.append("Congratulations! You guessed the number!\n");
 
            roundsWon++;
            scoreLabel.setText("Rounds Won: " + roundsWon);
            playAgainButton.setEnabled(true);
            return;
        }

        if (attemptsLeft == 0) {
            feedbackArea.append("Game over! The number was: " + targetNo + "\n");
            playAgainButton.setEnabled(true);
        } else {
            attemptsLabel.setText("Attempts left: " + attemptsLeft);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGuessingGame::new); // Ensure main is run on EDT
    }
}
