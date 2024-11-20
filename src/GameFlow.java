import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GameFlow extends Thread {

    private Player player1;
    private Player player2;
    private Player currentPlayer;


    public GameFlow(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        this.currentPlayer = player1;

        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);
    }

    public void run() {
        int timer;
        int rounds;
        int questionsPerRound=0;

        //-----properties load and set start
        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/Settings.properties"));
        }

        catch (Exception e) {
            System.out.println("filen hittades inte");
        }

        timer= Integer.parseInt(p.getProperty("timer", "10"));
        rounds = Integer.parseInt(p.getProperty("rounds", "4"));
        questionsPerRound = Integer.parseInt(p.getProperty("questionsPerRound", "4"));

        player1.setRounds(rounds);
        player2.setRounds(rounds);

        //----properties load and set end

        GamePanel  gamePanel = new GamePanel();

        String questionText;
        String correctAnswer;
        List<String> options;

        //JButton input på vilket tema som är valt
        String användarVal="ANIMALS";

        //hämtar in listan med alla frågor i temat, shufflar
        List<QuestionClass> allThemedQuestions= ClassMaker.valueOf(användarVal).getQuestions();
        Collections.shuffle(allThemedQuestions);

        //hämtar ut de första shufflade frågorna med antal questionsPerRound
        List<QuestionClass> questions=allThemedQuestions.subList(0, questionsPerRound);


        for (QuestionClass question : questions) {
            options = question.getOptions();
            questionText = question.getQuestion();
            correctAnswer = question.getCorrectAnswer();



            String userAnswer = "JButton text.valueOf";

            if (userAnswer.equals(question.getCorrectAnswer())) {
                System.out.println("Yasss poäng!");
            } else {
                System.out.println("Fel, inga poäng ");
            }
        }

    }
}




