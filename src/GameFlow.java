import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GameFlow extends Thread {

    private int timer;
    private int questionsPerRound;
    private int rounds;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    String messageIn="";
    String messageOut="";

    public GameFlow(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        this.currentPlayer = player1;

        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);

        //loading properties
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("src/Settings.properties"));
        } catch (Exception e) {
            System.out.println("filen hittades inte");
        }

        this.timer = Integer.parseInt(p.getProperty("timer", "10"));
        this.rounds = Integer.parseInt(p.getProperty("rounds", "4"));
        this.questionsPerRound = Integer.parseInt(p.getProperty("questionsPerRound", "4"));

        player1.setRounds(rounds);
        player2.setRounds(rounds);

    }


    public void run() {
        System.out.println("1");
        messageIn=currentPlayer.receive();
        System.out.println(messageIn);
        System.out.println("2");
        currentPlayer.send(messageIn);
        System.out.println("3");


    }

    public List<QuestionClass> getQuestions(String userThemeChoice) {
        List<QuestionClass> allThemedQuestions = ClassMaker.valueOf(userThemeChoice).getQuestions();
        Collections.shuffle(allThemedQuestions);

        List<QuestionClass> questions = allThemedQuestions.subList(0, this.questionsPerRound);
        return questions;
    }

    public boolean winchecker(String userAnswer, String correctAnswer) {
        if (userAnswer.equals(correctAnswer)) {
            return true;
        }
        return false;
    }
}


