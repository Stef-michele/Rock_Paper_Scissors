import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

public class RockPaperScissorsFrame extends JFrame {
    // Constants & Variables
    private final String[] choices = {"Rock", "Paper", "Scissors"};
    private final Random random = new Random();
    private int playerWins = 0, computerWins = 0, ties = 0, gamesPlayed = 0;
    private String lastPlayerMove = "Rock";
    private Map<String, Integer> moveCounts = new HashMap<>();

    // Enum for Computer Strategy
    private enum Strategy { RANDOM, LAST_USED, MOST_USED, LEAST_USED, CHEAT }
    private Strategy computerStrategy = Strategy.RANDOM; // Default

    // UI Components
    private JLabel playerWinsLabel = new JLabel("Player Wins: 0");
    private JLabel computerWinsLabel = new JLabel("Computer Wins: 0");
    private JLabel tiesLabel = new JLabel("Ties: 0");
    private JLabel gamesPlayedLabel = new JLabel("Games Played: 0");
    private JTextArea resultTextArea;

    // Constructor - Sets up the Game Window
    public RockPaperScissorsFrame() {
        super("Rock Paper Scissors Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Setup UI components
        setupButtonPanel();
        setupStatsPanel();
        setupHistoryPanel();

        setVisible(true);
    }


    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());

        JButton rockButton = new JButton(resizeIcon("/rock.jpg", 100, 100));
        JButton paperButton = new JButton(resizeIcon("/paper.jpg", 100, 100));
        JButton scissorsButton = new JButton(resizeIcon("/scissors.jpg", 100, 100));
        JButton quitButton = new JButton("Quit");

        // Button Actions
        rockButton.addActionListener(e -> playGame("Rock"));
        paperButton.addActionListener(e -> playGame("Paper"));
        scissorsButton.addActionListener(e -> playGame("Scissors"));
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.NORTH);
    }


    private void setupStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Game Stats"));



        statsPanel.add(playerWinsLabel);

        statsPanel.add(computerWinsLabel);

        statsPanel.add(tiesLabel);
        statsPanel.add(gamesPlayedLabel);
        add(statsPanel, BorderLayout.CENTER);
    }


    private void setupHistoryPanel() {
        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Game History"));

        add(scrollPane, BorderLayout.SOUTH);
    }


    private void playGame(String playerChoice) {
        gamesPlayed++;
        String computerChoice = getComputerMove(playerChoice);
        String result;

        if (playerChoice.equals(computerChoice)) {
            result = "It's a tie! Both chose " + playerChoice;
            ties++;
        } else if ((playerChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                (playerChoice.equals("Paper") && computerChoice.equals("Rock")) ||
                (playerChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
            result = playerChoice + " beats " + computerChoice + ". Player Wins!";
            playerWins++;
        } else {
            result = computerChoice + " beats " + playerChoice + ". Computer Wins!";
            computerWins++;
        }

        lastPlayerMove = playerChoice;
        moveCounts.put(playerChoice, moveCounts.getOrDefault(playerChoice, 0) + 1);

        resultTextArea.append(result + " (" + computerStrategy + ")\n");
        playerWinsLabel.setText("Player Wins: " + playerWins);
        computerWinsLabel.setText("Computer Wins: " + computerWins);
        tiesLabel.setText("Ties: " + ties);
        gamesPlayedLabel.setText("Games Played: " + gamesPlayed);
    }


    private String getComputerMove(String playerChoice) {
        if (random.nextDouble() < 0.1) {
            computerStrategy = Strategy.CHEAT;
        } else {
            Strategy[] strategies = Strategy.values();
            computerStrategy = strategies[random.nextInt(strategies.length)];
        }
        switch (computerStrategy) {
            case RANDOM:
                return choices[random.nextInt(3)];
            case LAST_USED:
                return lastPlayerMove;
            case MOST_USED:
                return moveCounts.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(choices[random.nextInt(3)]);
            case LEAST_USED:
                return moveCounts.entrySet().stream()
                        .min(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(choices[random.nextInt(3)]);
            case CHEAT:
                return  getWinningMove(playerChoice);
            default:
                return choices[random.nextInt(3)];
        }
    }


    private String getWinningMove(String playerMove) {
        switch (playerMove) {
            case "Rock": return "Paper";
            case "Paper": return "Scissors";
            case "Scissors": return "Rock";
            default: return choices[random.nextInt(3)];
        }
    }

    //   Resize Icons for Buttons
    private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

}


