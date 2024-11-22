import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GameFlow extends Thread {

    private final int INITIAL = 0;
    private final int ENTER_USERNAME = 1;
    private final int CHOOSE_CATEGORY = 2;
    private final int QUIZZING = 3;
    private final int WAITING = 4;
    private final int SHOW_SCORE_THIS_ROUND = 5;
    private final int FINAL = 6;

    private int timer;
    private int questionsPerRound;
    private int rounds;
    private int counterOfRounds = 0;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    //private final Protocol protocol = new Protocol();

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
        //----properties load and set end

        //Skriver ut välkomstmeddelande
        currentPlayer.send(INITIAL);
        //Välj kategori visas
        player1.send(ENTER_USERNAME);
        currentPlayer.username = (String) currentPlayer.receive();
        System.out.println(currentPlayer.username);
        player1.send(CHOOSE_CATEGORY);
        //Sparar vald kategori som läses in av GameFlow
        player1.themeChoice = (String) player1.receive();
        //Skickar frågor till spelare
        player1.send(QUIZZING);
        //Spelet spelas
        //Metod som hanterar spel?
        player1.pointsThisRound = Integer.parseInt((String) player1.receive());
        player1.send(SHOW_SCORE_THIS_ROUND);
        //Den här borde gå att göra bättre. En egen metod? Skicka in counterOfRounds och sparar pointsThisRound i arrayen
        player1.addPointsThisRound(counterOfRounds, player1.pointsThisRound);
        //Håller koll på vilken runda vi är på
        counterOfRounds++;
        //Skickar och visar poäng denna runda
        player1.send(WAITING);
        //Tillbaka till quizzing och rond 2
        player1.send(QUIZZING);
        //Skickar och visar poäng denna runda
        player1.send(SHOW_SCORE_THIS_ROUND);
        //Spelet slut och slutresultat visas
        player1.send(FINAL);

        player1.close();
    }

    public List<QuestionClass> getQuestions() {
        String userThemeChoice = currentPlayer.getThemeChoice();
        List<QuestionClass> allThemedQuestions = ClassMaker.valueOf(userThemeChoice).getQuestions();
        Collections.shuffle(allThemedQuestions);
        List<QuestionClass> questions = allThemedQuestions.subList(0, this.questionsPerRound);
        //List<String><String>questionsToClient = new ArrayList<String>();


        return questions;
    }

    public boolean winchecker(String userAnswer, String correctAnswer) {
        if (userAnswer.equals(correctAnswer)) {
            return true;
        }
        return false;
    }
}


