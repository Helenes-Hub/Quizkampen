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
    private final int WAITING_FOR_SCORE = 7;
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
    private Timer questionTimer;
    private JProgressBar timerBar;
    private int timeLeft;
    int timeFromServer = 10; //den ska ta in tiden för timern från servern


    private String currentCategory;
    private int currentQuestionIndex;
    private int score;
    private ArrayList[][] questionArray;
    private Object fromServer;
    private Object toServer;
    private String correctAnswer;
    private Boolean busy=false;

    Client  client = new Client();


    public GamePanel() {
        setUpFrame();
        while (true) {
            try {
                fromServer = client.receive();
                System.out.println("står och lyssnar");
                if (fromServer != null) {
                    currentState = (int) fromServer;
                    System.out.println("Nuvarande status: " + currentState);
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
            //currentState = ENTER_USERNAME;
            client.send("STEP_FINISHED");
            //handleState();
            return;
        }
        if (e.getSource() == enterNameButton || e.getSource() == userNameField) {
            if (!userNameField.getText().trim().isEmpty()) {
                toServer = userNameField.getText().trim();
                System.out.println(toServer);
                client.send(toServer);
                //client.send("STEP_FINISHED");
                //currentState = WAITING;
                //handleState();
                return;
            }
        }
        if (e.getSource() == category1Button) {
            toServer = category1Button.getText().toUpperCase();
            //client.send(CHOOSE_CATEGORY);
            currentCategory = (String) toServer;
            client.send(toServer.toString());
            //client.send("STEP_FINISHED");
            //currentState = QUIZZING;
            //handleState();
            return;
        }
        if (e.getSource() == category2Button) {
            toServer = category2Button.getText().toUpperCase();
            //client.send(CHOOSE_CATEGORY);
            currentCategory = (String) toServer;
            client.send(toServer.toString());
            //client.send("STEP_FINISHED");
            //currentState = QUIZZING;
            //handleState();
            return;
        }
        if (e.getSource() == buttonA || e.getSource() == buttonB || e.getSource() == buttonC || e.getSource() == buttonD) {
            JButton clickedButton = (JButton) e.getSource();
            questionTimer.stop();
            displayAnswer(clickedButton, correctAnswer);
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
                //client.send(currentState);
                waitingForOtherPlayerPanel();
                client.send("STEP_FINISHED");
                //client.send("STEP_FINISHED");
                break;
            case SHOW_SCORE_THIS_ROUND:
                roundFinishedPanel();
                //Knapp för att gå vidare från denna panel? I actionEvent från den client.send("STEP_FINISHED");
                break;
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
        for (JButton button : new JButton[]{category1Button,category2Button}) {
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }}

        setUpButton(category1Button, 100, 300, 200, 100, "Animals");
        setUpButton(category2Button, 400, 300, 200, 100, "Science");

    }

    private void startGamePanel() {
        client.send("STEP_FINISHED");
        try {
            System.out.println("inväntar listan");
            this.questionArray = (ArrayList[][]) client.receive();
            System.out.println("har mottagit listan");

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
        for (JButton button : new JButton[]{buttonA, buttonB, buttonC, buttonD}) {
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }}

        setUpLabel(question, 50, 200, 600, 100, "");
        setUpButton(buttonA, 45, 350, 300, 100, "");
        setUpButton(buttonB, 355, 350, 300, 100, "");
        setUpButton(buttonC, 45, 470, 300, 100, "");
        setUpButton(buttonD, 355, 470, 300, 100, "");

        timerBar = new JProgressBar(0, timeFromServer);
        timerBar.setValue(10);
        timerBar.setForeground(new Color(75, 181, 67));
        timerBar.setBackground(new Color(211, 211, 211));
        timerBar.setBounds(150, 150, 400, 20);
        add(timerBar);

        questionTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerBar.setValue(timeLeft);

                if (timeLeft <= timeFromServer / 3) {
                    timerBar.setForeground(new Color(181, 67, 67));
                }

                if (timeLeft <= 0) {
                    questionTimer.stop();
                    //String correctAnswer = questions.get(currentQuestionIndex).getCorrectAnswer();
                    displayAnswer(null, correctAnswer);
                }
            }
        });
        nextQuestion();

    }


    private void waitingForOtherPlayerPanel() {
        clearPanel();
        JLabel waitingForOtherPlayerLabel = new JLabel();
        setUpLabel(waitingForOtherPlayerLabel, 150, 50, 400, 350, "Waiting for other player");
    }

    private void roundFinishedPanel() {
        clearPanel();
        JLabel scoreLabel = new JLabel();
        setUpLabel(scoreLabel, 150, 50, 400, 350, "Score this round: " + score);
    }

    private void checkAnswer(String answer) {

            //Server.QuestionClass currentQuestion = questions.get(currentQuestionIndex);
            //System.out.println(correctAnswer);
            if (answer.equals(correctAnswer))
                score++;

            currentQuestionIndex++;
        System.out.println("currentquestionIndex: "+currentQuestionIndex);
        System.out.println(questionArray[0].length);

            if (currentQuestionIndex < questionArray[0].length) {
                System.out.println("anropar next från checkanswer");
                nextQuestion();
            } else {
                System.out.println("skickar poäng till server: " + score);
                //currentState=WAITING;
                try {
                    client.send(score);

                    //client.send("STEP_FINISHED");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //waitingForOtherPlayerPanel();
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


        Timer pauseTimer = new Timer(1500, new ActionListener() {

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

        add(quitButton);
        quitButton.setBounds(250, 470, 200, 100);

        JLabel scoreField = new JLabel();
        setUpLabel(scoreField, 200, 300, 300, 100, "Score: " + score + "/" + questionArray[0].length);

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
        button.setFont(new Font("Impact", Font.BOLD, 30));
        button.setFocusable(false);
        button.addActionListener(this);
        button.setText(text);
    }

    private void setUpTextField(JTextField textField, int x, int y, int width, int height, String text,
                                boolean editable) {
        add(textField);
        textField.setBounds(x, y, width, height);
        textField.setBackground(new Color(211, 211, 211));
        textField.setFont(new Font("Impact", Font.BOLD, 30));
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
        label.setFont(new Font("Impact", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void clearPanel() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }


    public static void main(String[] args) {
        new GamePanel();
    }
}
