import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class GamePanel extends JFrame implements ActionListener {

    private JTextField title = new JTextField("Quiz");
    private JTextField question = new JTextField();
    private JButton playButton = new JButton("Play");
    private JButton quitButton = new JButton("Quit");
    private JButton category1Button = new JButton();
    private JButton category2Button = new JButton();
    private JButton buttonA = new JButton();
    private JButton buttonB = new JButton();
    private JButton buttonC = new JButton();
    private JButton buttonD = new JButton();

    private int currentQuestionIndex;
    private int score;
    private List<QuestionClass> questions;

    Socket socket;
    String messageOut="";
    String messageIn="";
    PrintWriter out;
    BufferedReader in;

    public GamePanel(){

        try {
            socket = new Socket("127.0.0.1",5050);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("fel vid writer/reader initialisering", e);
        }

        setTitle("Quiz");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(new Color(0, 50, 76));


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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitButton) {
            System.exit(0);
        }
        if (e.getSource() == playButton) {
            showCategories();
        }
        if (e.getSource() == category1Button) {
            startGame(ClassMaker.ANIMALS);
        }
        if (e.getSource() == category2Button) {
            startGame(ClassMaker.SCIENCE);
        }
        if (e.getSource() == buttonA || e.getSource() == buttonB || e.getSource() == buttonC || e.getSource() == buttonD) {
            JButton clickedButton = (JButton) e.getSource();
            checkAnswer(clickedButton.getText());
        }
    }

    private void showCategories() {
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

    private void startGame(ClassMaker category) {

        messageOut="hehehe";
        System.out.println("1");
        send(messageOut);
        System.out.println("2");
        messageIn=receive();
        System.out.println("3");
        System.out.println(messageIn);
        System.out.println("4");

        questions = category.getQuestions();
        currentQuestionIndex = 0;
        score = 0;
        QuestionClass currentQuestion = questions.get(currentQuestionIndex);
        List<String> options = currentQuestion.getOptions();

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
            endGame();
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

    public void send(String message){
        out.println(message + "\n");
    }

    public String receive(){
        try {
            String message = in.readLine();
            if(message!=null){
                return message;
            }
            return "failed to read message";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void endGame() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        add(title);
        title.setText("Game Over");

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
