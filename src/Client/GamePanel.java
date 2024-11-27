package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JFrame implements ActionListener {

    private final int INITIAL = 0;
    private final int ENTER_USERNAME = 1;
    private final int CHOOSE_CATEGORY = 2;
    private final int QUIZZING = 3;
    private final int WAITING = 4;
    private final int SHOW_SCORE_THIS_ROUND = 5;
    private final int FINAL = 6;
    private final int FINAL_WAIT = 7;
    private int currentState = 0;


    private JLabel title = new JLabel();
    private JLabel question = new JLabel();
    private JTextField userNameField = new JTextField();
    private JButton playButton = new JButton();
    private JButton quitButton = new JButton();
    private JButton enterNameButton = new JButton();
    private JButton category1Button = new JButton();
    private JButton category2Button = new JButton();
    private JButton buttonA = new JButton();
    private JButton buttonB = new JButton();
    private JButton buttonC = new JButton();
    private JButton buttonD = new JButton();
    private JButton readyButton=new JButton();
    private Timer questionTimer;
    private JProgressBar timerBar;
    private double timeLeft;
    int timeFromServer;


    private String currentCategory;
    private int currentQuestionIndex;
    private int score;
    private int opponentScoreThisRound;
    private int opponentTotalScore;
    private int totalScore;
    private int questionsPerRound;
    private int totalQuestions;
    private ArrayList[][] questionArray;
    private Object fromServer;
    private Object toServer;
    private String correctAnswer;
    private String opponentUsername;


    Client  client = new Client();


    public GamePanel() {
        setUpFrame();
        setupActionlisteners();
        try {
         timeFromServer=(int)client.receive();
         questionsPerRound=(int)client.receive();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                fromServer = client.receive();
                if (fromServer != null) {
                    currentState = (int) fromServer;
                    handleState();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitButton) {
            client.send("QUIT");
            System.exit(0);
        }
        if (e.getSource() == playButton) {
            client.send("STEP_FINISHED");
            return;
        }
        if (e.getSource() == enterNameButton || e.getSource() == userNameField) {
            if (!userNameField.getText().trim().isEmpty()) {
                toServer = userNameField.getText().trim();
                client.send(toServer);
                return;
            }
        }
        if (e.getSource() == category1Button) {
            toServer = category1Button.getText().toUpperCase();
            currentCategory = (String) toServer;
            client.send(toServer.toString());
            return;
        }
        if (e.getSource() == category2Button) {
            toServer = category2Button.getText().toUpperCase();
            currentCategory = (String) toServer;
            client.send(toServer.toString());
            return;
        }
        if (e.getSource() == buttonA || e.getSource() == buttonB || e.getSource() == buttonC || e.getSource() == buttonD) {
            JButton clickedButton = (JButton) e.getSource();
            questionTimer.stop();
            displayAnswer(clickedButton, correctAnswer);
        }
        if (e.getSource() == readyButton){
            client.send("STEP_FINISHED");
        }
    }

    private void handleState() {
        switch (currentState) {
            case INITIAL:
                mainMenuPanel();
                break;
            case ENTER_USERNAME:
                enterUserNamePanel();
                break;
            case CHOOSE_CATEGORY:
                showCategoriesPanel();
                break;
            case QUIZZING:
                startGamePanel();
                break;
            case WAITING:
                waitingForOtherPlayerPanel();
                client.send("STEP_FINISHED");
                break;
            case SHOW_SCORE_THIS_ROUND:
                roundFinishedPanel();
                break;
            case FINAL_WAIT:
                waitingForOtherPlayerPanel();
            case FINAL:
                finalScorePanel();
                break;
        }
    }

    private void setUpFrame() {
        setTitle("Quiz");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(new Color(0, 50, 76));
    }

    private void mainMenuPanel() {
        clearPanel();
        setUpLabel(title, 150, 50, 400, 100, "Quiz");
        title.setFont(new Font("Impact", Font.BOLD, 100));
        title.setBackground(new Color(0, 50, 76));
        title.setForeground(new Color(211, 211, 211));
        title.setBounds(150, 50, 400, 100);

        add(playButton);
        add(quitButton);
        playButton.addActionListener(this);
        quitButton.addActionListener(this);

        setUpButton(playButton, 250, 250, 200, 100, "Play");
        setUpButton(quitButton, 250, 370, 200, 100, "Quit");
    }

    private void enterUserNamePanel() {
        clearPanel();
        JLabel userNameLabel = new JLabel();
        setUpLabel(userNameLabel, 150, 50, 400, 350, "Enter your username");
        setUpTextField(userNameField, 150, 275, 400, 50, "", true);
        setUpButton(enterNameButton, 250, 350, 200, 50, "Enter");

    }

    private void showCategoriesPanel() {
        client.send("STEP_FINISHED");
        userNameField.setText("");
        clearPanel();
        add(title);
        title.setText("Select a category");
        title.setFont(new Font("Impact", Font.BOLD, 40));

        setUpButton(category1Button, 100, 300, 200, 100, "Animals");
        setUpButton(category2Button, 400, 300, 200, 100, "Science");

    }

    private void startGamePanel() {
        client.send("STEP_FINISHED");
        try {
            this.questionArray = (ArrayList[][]) client.receive();

        } catch (Exception e) {
            e.printStackTrace();
        }


        currentQuestionIndex = 0;
        score = 0;

        timeLeft = timeFromServer;

        clearPanel();

        add(title);
        title.setBounds(150, 50, 400, 100);
        title.setText("Question " + (currentQuestionIndex + 1));
        title.setFont(new Font("Impact", Font.BOLD, 70));

        setUpLabel(question, 25, 200, 650, 100, "");
        setUpButton(buttonA, 45, 350, 300, 100, "");
        setUpButton(buttonB, 355, 350, 300, 100, "");
        setUpButton(buttonC, 45, 470, 300, 100, "");
        setUpButton(buttonD, 355, 470, 300, 100, "");

        timerBar = new JProgressBar(0, timeFromServer * 100);
        timerBar.setValue(timeFromServer * 100);
        timerBar.setForeground(new Color(75, 181, 67));
        timerBar.setBackground(new Color(211, 211, 211));
        timerBar.setBounds(150, 150, 400, 20);
        add(timerBar);

        questionTimer = new javax.swing.Timer(32, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                timeLeft -= 0.064;
                timerBar.setValue((int)(timeLeft * 100));

                if (timeLeft <= timeFromServer / 3) {
                    timerBar.setForeground(new Color(181, 67, 67));
                }

                if (timeLeft <= 0) {
                    questionTimer.stop();
                    displayAnswer(null, correctAnswer);
                }
            }
        });
        nextQuestion();

    }


    private void waitingForOtherPlayerPanel() {
        clearPanel();
        JLabel waitingForOtherPlayerLabel = new JLabel();
        setUpLabel(waitingForOtherPlayerLabel, 150, 50, 400, 350, "Waiting for the other player");

        Timer dotTimer = new javax.swing.Timer(500, new ActionListener() {
            private int dots = 0;
            public void actionPerformed(ActionEvent e) {
                dots = (dots + 1) % 4;
                waitingForOtherPlayerLabel.setText("Waiting for the other player" + ".".repeat(dots));
            }
        });

        dotTimer.start();
    }

    private void roundFinishedPanel() {
        try {
           opponentScoreThisRound = client.opponentScoreThisRound=(int)client.receive();
           opponentUsername = (String)client.receive();
           opponentTotalScore = client.opponentTotalScore+=client.opponentScoreThisRound;
        }
        catch (Exception e)
        {e.printStackTrace();}

        clearPanel();
        add(title);
        title.setText("Round finished!");
        title.setBounds(150, 50, 400, 80);
        title.setFont(new Font("Impact", Font.BOLD, 50));

        JLabel ScoreText = new JLabel();
        JLabel ScoreNumber = new JLabel();
        JLabel opponentScoreText = new JLabel();
        JLabel opponentScoreNumber = new JLabel();

        setUpLabel(ScoreText, 150, 150, 400, 50, "Your Score this round");
        setUpLabel(ScoreNumber, 150, 200, 400, 80, String.valueOf(score));
        ScoreNumber.setFont(new Font("Impact", Font.BOLD, 50));

        JLabel divider = new JLabel();
        setUpLabel(divider, 50, 285, 600, 30, "- - - - - - - - - - - - - - - -");

        setUpLabel(opponentScoreText, 150, 325, 400, 50, opponentUsername + "'s Score this round");
        setUpLabel(opponentScoreNumber, 150, 375, 400, 80, String.valueOf(opponentScoreThisRound));
        opponentScoreNumber.setFont(new Font("Impact", Font.BOLD, 50));

        setUpButton(readyButton, 250, 500, 200, 80, "Continue");
    }

    private void checkAnswer(String answer) {

            if (answer.equals(correctAnswer))
                score++;

            currentQuestionIndex++;

            if (currentQuestionIndex < questionArray[0].length) {
                nextQuestion();
            } else {
                totalScore+=score;
                totalQuestions+=questionsPerRound;
                try {
                    client.send(score);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
    }

    private void displayAnswer(JButton clickedButton, String correctAnswer) {
        if (clickedButton != null && clickedButton.getText().equals(correctAnswer)) {
            clickedButton.setBackground(new Color(75, 181, 67));
        } else {
            if (clickedButton != null) {
                clickedButton.setBackground(new Color(181, 67, 67));
            }
            getButtonWithAnswer(correctAnswer).setBackground(new Color(75, 181, 67));
        }

        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);


        Timer pauseTimer = new javax.swing.Timer(1500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e2) {
                setDefaultButtonColor();

                String answer;
                if (clickedButton != null) {
                    answer = clickedButton.getText();
                } else {
                    answer = "";
                }
                checkAnswer(answer);
            }
        });

        pauseTimer.setRepeats(false);
        pauseTimer.start();
    }

    private JButton getButtonWithAnswer(String answer) {
        if (buttonA.getText().equals(answer)) return buttonA;
        if (buttonB.getText().equals(answer)) return buttonB;
        if (buttonC.getText().equals(answer)) return buttonC;
        return buttonD;
    }

    private void nextQuestion() {
        String currentQuestion = String.valueOf(questionArray[currentQuestionIndex][0].get(0));
        List<String> options = questionArray[currentQuestionIndex][1];

        String correctAnswer = String.valueOf(questionArray[currentQuestionIndex][2].get(0));
        this.correctAnswer = correctAnswer;


        setDefaultButtonColor();
        title.setText("Question " + (currentQuestionIndex + 1));
        question.setText(currentQuestion);
        buttonA.setText(options.get(0));
        buttonB.setText(options.get(1));
        buttonC.setText(options.get(2));
        buttonD.setText(options.get(3));


        timeLeft = timeFromServer;
        timerBar.setValue(timeFromServer);
        timerBar.setForeground(new Color(75, 181, 67));
        questionTimer.start();
    }

    private void finalScorePanel() {
        clearPanel();

        add(title);
        title.setText("Final score");
        title.setBounds(150, 30, 400, 80);
        title.setFont(new Font("Impact", Font.BOLD, 50));

        JLabel finalScoreText = new JLabel();
        JLabel finalScoreNumber = new JLabel();
        JLabel finalText = new JLabel();

        setUpLabel(finalScoreText, 150, 120, 400, 50, "Your Final Score");
        setUpLabel(finalScoreNumber, 150, 170, 400, 80, String.valueOf(totalScore));
        finalScoreNumber.setFont(new Font("Impact", Font.BOLD, 50));
        setUpLabel(finalText, 150, 250, 400, 30, "out of " + totalQuestions);

        JLabel divider = new JLabel();
        setUpLabel(divider, 50, 290, 600, 30, "- - - - - - - - - - - - - - - -");

        JLabel opponentFinalScoreText = new JLabel();
        JLabel opponentFinalScoreNumber = new JLabel();
        JLabel opponentFinalText = new JLabel();

        setUpLabel(opponentFinalScoreText, 150, 320, 400, 50, opponentUsername + "'s Final Score");
        setUpLabel(opponentFinalScoreNumber, 150, 370, 400, 80, String.valueOf(opponentTotalScore));
        opponentFinalScoreNumber.setFont(new Font("Impact", Font.BOLD, 50));
        setUpLabel(opponentFinalText, 150, 450, 400, 30, "out of " + totalQuestions);

        setUpButton(quitButton, 250, 530, 200, 80, "Quit");
    }

    private void setDefaultButtonColor() {
        buttonA.setBackground(new Color(211, 211, 211));
        buttonB.setBackground(new Color(211, 211, 211));
        buttonC.setBackground(new Color(211, 211, 211));
        buttonD.setBackground(new Color(211, 211, 211));
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    }

    private void setUpButton(JButton button, int x, int y, int width, int height, String text) {
        add(button);
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(211, 211, 211));
        button.setFont(new Font("Impact", Font.BOLD, 27));
        button.setFocusable(false);
        button.setText(text);
    }

    private void setUpTextField(JTextField textField, int x, int y, int width, int height, String text,
                                boolean editable) {
        add(textField);
        textField.setBounds(x, y, width, height);
        textField.setBackground(new Color(211, 211, 211));
        textField.setFont(new Font("Impact", Font.BOLD, 27));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBorder(null);
        textField.setEditable(editable);
        textField.setFocusable(editable);
        textField.setText(text);
        textField.requestFocus();
        textField.addActionListener(this);
    }

    private void setUpLabel(JLabel label, int x, int y, int width, int height, String text) {
        add(label);
        label.setText(text);
        label.setBounds(x, y, width, height);
        label.setForeground(new Color(211, 211, 211));
        label.setFont(new Font("Impact", Font.BOLD, 27));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void clearPanel() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }

    private void setupActionlisteners(){
        buttonA.addActionListener(this);
        buttonB.addActionListener(this);
        buttonC.addActionListener(this);
        buttonD.addActionListener(this);
        category1Button.addActionListener(this);
        category2Button.addActionListener(this);
        playButton.addActionListener(this);
        quitButton.addActionListener(this);
        enterNameButton.addActionListener(this);
        readyButton.addActionListener(this);
    }


    public static void main(String[] args) {
        new GamePanel();
    }
}
