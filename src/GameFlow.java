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
    private int currentState = 0;

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

        if (currentState == INITIAL){
            player1.send(INITIAL);
            player2.send(INITIAL);
        } else if (currentState == ENTER_USERNAME){
            player1.send(ENTER_USERNAME);
            player2.send(ENTER_USERNAME);
            player1.username = (String) player1.receive();
            player2.username = (String) player2.receive();
            System.out.println(currentPlayer.username);
        }else if (currentState == CHOOSE_CATEGORY){
            player1.send(CHOOSE_CATEGORY);
            player1.themeChoice = (String) player1.receive();
            player2.send(WAITING);
        }else if (currentState == QUIZZING){
            player1.send(QUIZZING);
            player1.send(getQuestions());
            player2.send(WAITING);
            player1.send(SHOW_SCORE_THIS_ROUND);
            player2.send(QUIZZING);
            player2.send(getQuestions());
            player1.pointsThisRound = Integer.parseInt((String) player1.receive());
            player1.addPointsThisRound(counterOfRounds, player1.pointsThisRound);
            player2.pointsThisRound = Integer.parseInt((String) player2.receive());
            player2.addPointsThisRound(counterOfRounds, player2.pointsThisRound);
            counterOfRounds++;
        }else if (currentState == SHOW_SCORE_THIS_ROUND){
            player1.send(SHOW_SCORE_THIS_ROUND);
            player2.send(SHOW_SCORE_THIS_ROUND);
            player1.send(player1.getPointsThisRound());
            player2.send(player2.getPointsThisRound());
            player1.send(player1.getPointsAllRounds());
            player2.send(player2.getPointsAllRounds());
        } else if (currentState == FINAL){
            player1.send(FINAL);
            player1.send(player1.getTotalScore());
            player2.send(FINAL);
            player2.send(player2.getTotalScore());
        }

        player1.close();
    }

    public ArrayList[][] getQuestions() {
        String userThemeChoice = currentPlayer.getThemeChoice();
        List<QuestionClass> allThemedQuestions = ClassMaker.valueOf(userThemeChoice).getQuestions();
        Collections.shuffle(allThemedQuestions);
        List<QuestionClass> questions = allThemedQuestions.subList(0, this.questionsPerRound);
        ArrayList[][] questionArray= new ArrayList[3][3];
        for (int i = 0; i < this.questionsPerRound; i++) {
            for (int j = 0; j < 3; j++) {
                questionArray[i][j] = new ArrayList<>(); // Initialize each ArrayList
            }
        }
        for (int i = 0; i < this.questionsPerRound; i++) {
            String currentQuestion = questions.get(i).getQuestion();
            List<String> options = questions.get(i).getOptions();
            questionArray[i][0].add(questions.get(i).getQuestion());
            questionArray[i][1].addAll(questions.get(i).getOptions());
            questionArray[i][2].add(questions.get(i).getCorrectAnswer());
            System.out.println(questionArray[i][0]);
            System.out.println(questionArray[i][1]);
            System.out.println(questionArray[i][2]);
        }
        return questionArray;
    }

    public boolean winchecker(String userAnswer, String correctAnswer) {
        if (userAnswer.equals(correctAnswer)) {
            return true;
        }
        return false;
    }
}


