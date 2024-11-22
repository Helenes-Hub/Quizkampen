import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class GamePanel extends JFrame implements ActionListener {

    private final int INITIAL = 0;
    private final int ENTER_USERNAME = 1;
    private final int CHOOSE_CATEGORY = 2;
    private final int QUIZZING = 3;
    private final int WAITING = 4;
    private final int SHOW_SCORE_THIS_ROUND = 5;
    private final int FINAL = 6;
    private int currentState = 0;

    private JTextField title = new JTextField("Quiz");
    private JTextField question = new JTextField();
    private JTextField userNameField = new JTextField();
    private JButton playButton = new JButton("Play");
    private JButton quitButton = new JButton("Quit");
    private JButton enterNameButton = new JButton("Enter");
    private JButton category1Button = new JButton();
    private JButton category2Button = new JButton();
    private JButton buttonA = new JButton();
    private JButton buttonB = new JButton();
    private JButton buttonC = new JButton();
    private JButton buttonD = new JButton();

    private String currentCategory;
    private int currentQuestionIndex;
    private int score;
    private List<QuestionClass> questions;
    private Object fromServer;
    private Object toServer;
    Client client=new Client();


    public GamePanel() {

        setUpFrame();
       // handleState();
        while (true){
            fromServer=client.receive();
            currentState=Integer.parseInt(fromServer.toString());
            System.out.println(currentState);
            handleState();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitButton) {
            System.exit(0);
        }
        if (e.getSource() == playButton) {
            currentState = ENTER_USERNAME;
            //handleState();
        }
        if (e.getSource() == enterNameButton || e.getSource() == userNameField) {
           if(!userNameField.getText().trim().isEmpty()) {
               toServer=userNameField.getText().trim();
               client.send(toServer.toString());
               currentState = CHOOSE_CATEGORY;
               //handleState();
               return;
           }
        }
        if (e.getSource() == category1Button) {
            toServer=category1Button.getText().toUpperCase();
            currentCategory = (String) toServer;
            client.send(toServer.toString());
            currentState = QUIZZING;
            //handleState();
        }
        if (e.getSource() == category2Button) {
            toServer=category1Button.getText().toUpperCase();
            currentCategory = (String) toServer;
            client.send(toServer.toString());
            currentState = QUIZZING;
            //handleState();
        }
        if (e.getSource() == buttonA || e.getSource() == buttonB || e.getSource() == buttonC || e.getSource() == buttonD) {
            JButton clickedButton = (JButton) e.getSource();
            checkAnswer(clickedButton.getText());
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
                client.receive();
                startGamePanel(questions);
                break;
            case WAITING:
                waitingForOtherPlayerPanel();
                break;
            case SHOW_SCORE_THIS_ROUND:
                roundFinishedPanel();
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
        add(title);
        title.setEditable(false);
        title.setFocusable(false);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(null);
        title.setFont(new Font("Impact", Font.BOLD, 100));
        title.setBackground(new Color(0, 50, 76));
        title.setForeground(new Color(211, 211, 211));
        title.setBounds(150, 50, 400, 100);

        add(playButton);
        add(quitButton);
        playButton.addActionListener(this);
        quitButton.addActionListener(this);

        playButton.setBounds(250, 250, 200, 100);
        playButton.setBackground(new Color(211, 211, 211));
        playButton.setFont(new Font("Impact", Font.BOLD, 30));
        playButton.setFocusable(false);

        quitButton.setBounds(250, 370, 200, 100);
        quitButton.setBackground(new Color(211, 211, 211));
        quitButton.setFont(new Font("Impact", Font.BOLD, 30));
        quitButton.setFocusable(false);
    }

    private void enterUserNamePanel() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        JLabel userNameLabel = new JLabel("Enter your username");
        userNameLabel.setFont(new Font("Impact", Font.BOLD, 30));
        userNameLabel.setForeground(new Color(211, 211, 211));
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameLabel.setBounds(150, 50, 400, 350);
        add(userNameLabel);

        userNameField.setBackground(new Color(211, 211, 211));
        userNameField.setFont(new Font("Impact", Font.BOLD, 30));
        userNameField.setHorizontalAlignment(SwingConstants.CENTER);
        userNameField.setBorder(null);
        userNameField.setBounds(150, 275, 400, 50);
        userNameField.addActionListener(this);
        add(userNameField);
        userNameField.requestFocus();

        enterNameButton.setBounds(250, 350, 200, 50);
        enterNameButton.setBackground(new Color(211, 211, 211));
        enterNameButton.setFont(new Font("Impact", Font.BOLD, 30));
        enterNameButton.setFocusable(false);
        enterNameButton.addActionListener(this);
        add(enterNameButton);
    }

    private void showCategoriesPanel() {
        userNameField.setText("");
        getContentPane().removeAll();
        revalidate();
        repaint();

        add(title);
        title.setText("Select a category");
        title.setFont(new Font("Impact", Font.BOLD, 40));

        category1Button.setText("Animals");
        add(category1Button);
        category1Button.setBounds(100, 300, 200, 100);
        category1Button.setBackground(new Color(211, 211, 211));
        category1Button.setFont(new Font("Impact", Font.BOLD, 30));
        category1Button.setFocusable(false);
        category1Button.addActionListener(this);

        category2Button.setText("Science");
        add(category2Button);
        category2Button.setBounds(400, 300, 200, 100);
        category2Button.setBackground(new Color(211, 211, 211));
        category2Button.setFont(new Font("Impact", Font.BOLD, 30));
        category2Button.setFocusable(false);
        category2Button.addActionListener(this);

    }

    private void startGamePanel(List<QuestionClass> questions) {

        //questions = category.getQuestions();
        currentQuestionIndex = 0;
        score = 0;
        QuestionClass currentQuestion = questions.get(currentQuestionIndex);
        // list[currentQuestionindex][0]
        List<String> options = currentQuestion.getOptions();
        //list = {1. fråga- options- rätt svar
        //          {}
        getContentPane().removeAll();
        revalidate();
        repaint();

        add(title);
        title.setBounds(150, 50, 400, 100);
        title.setText("Question " + (currentQuestionIndex + 1));
        title.setFont(new Font("Impact", Font.BOLD, 70));

        add(question);
        question.setBounds(50, 200, 600, 100);
        question.setBorder(null);
        question.setEditable(false);
        question.setHorizontalAlignment(SwingConstants.CENTER);
        question.setBackground(new Color(0, 50, 76));
        question.setForeground(new Color(211, 211, 211));
        question.setFont(new Font("Impact", Font.BOLD, 30));
        question.setFocusable(false);
        question.setText(currentQuestion.getQuestion());

        add(buttonA);
        buttonA.setBounds(45, 350, 300, 100);
        buttonA.setBackground(new Color(211, 211, 211));
        buttonA.setFont(new Font("Impact", Font.BOLD, 30));
        buttonA.setText(options.get(0));
        buttonA.setFocusable(false);
        buttonA.addActionListener(this);

        add(buttonB);
        buttonB.setBounds(355, 350, 300, 100);
        buttonB.setBackground(new Color(211, 211, 211));
        buttonB.setFont(new Font("Impact", Font.BOLD, 30));
        buttonB.setText(options.get(1));
        buttonB.setFocusable(false);
        buttonB.addActionListener(this);

        add(buttonC);
        buttonC.setBounds(45, 470, 300, 100);
        buttonC.setBackground(new Color(211, 211, 211));
        buttonC.setFont(new Font("Impact", Font.BOLD, 30));
        buttonC.setText(options.get(2));
        buttonC.setFocusable(false);
        buttonC.addActionListener(this);

        add(buttonD);
        buttonD.setBounds(355, 470, 300, 100);
        buttonD.setBackground(new Color(211, 211, 211));
        buttonD.setFont(new Font("Impact", Font.BOLD, 30));
        buttonD.setText(options.get(3));
        buttonD.setFocusable(false);
        buttonD.addActionListener(this);
    }

    private void waitingForOtherPlayerPanel() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        JLabel waitingForOtherPlayerLabel = new JLabel("Waiting for other player");
        waitingForOtherPlayerLabel.setFont(new Font("Impact", Font.BOLD, 30));
        waitingForOtherPlayerLabel.setForeground(new Color(211, 211, 211));
        waitingForOtherPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingForOtherPlayerLabel.setBounds(150, 50, 400, 350);
        getContentPane().add(waitingForOtherPlayerLabel);
    }

    private void roundFinishedPanel() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        JLabel scoreLabel = new JLabel("Score this round: " + score);
        scoreLabel.setFont(new Font("Impact", Font.BOLD, 30));
        scoreLabel.setForeground(new Color(211, 211, 211));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setBounds(150, 50, 400, 350);
        add(scoreLabel);
    }

    private void checkAnswer(String answer) {
        QuestionClass currentQuestion = questions.get(currentQuestionIndex);
        if(answer.equals(currentQuestion.getCorrectAnswer())) {
            score++;
        }
        currentQuestionIndex++;

        if(currentQuestionIndex < questions.size()) {
            nextQuestion();
        }
        else {
            finalScorePanel();
        }
    }

    private void nextQuestion() {
        QuestionClass currentQuestion = questions.get(currentQuestionIndex);
        List<String> options = currentQuestion.getOptions();

        title.setText("Question " + (currentQuestionIndex + 1));
        question.setText(currentQuestion.getQuestion());
        buttonA.setText(options.get(0));
        buttonB.setText(options.get(1));
        buttonC.setText(options.get(2));
        buttonD.setText(options.get(3));
    }

    private void finalScorePanel() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        add(title);
        title.setText("Final score");

        add(quitButton);
        quitButton.setBounds(250, 470, 200, 100);

        JTextField scoreField = new JTextField();
        add(scoreField);
        scoreField.setEditable(false);
        scoreField.setFocusable(false);
        scoreField.setHorizontalAlignment(SwingConstants.CENTER);
        scoreField.setBackground(new Color(0, 50, 76));
        scoreField.setForeground(new Color(211, 211, 211));
        scoreField.setFont(new Font("Impact", Font.BOLD, 30));
        scoreField.setBorder(null);
        scoreField.setBounds(200, 300, 300, 100);
        scoreField.setText("Score: " + score + "/" + questions.size());
    }

    public static void main(String[] args) {
        new GamePanel();
    }
}
