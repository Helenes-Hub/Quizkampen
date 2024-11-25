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
    private final int WAITING_FOR_SCORE =7;
    private final int QUIT=8;
    private int currentStateP1 = 0;
    private int currentStateP2 = 0;

    private int timer;
    private int questionsPerRound;
    private int rounds;
    private int counterOfRounds = 0;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Object questions;
    private Boolean roundOver=false;

    public GameFlow(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        //this.currentPlayer = player1;

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

        runPlayer1();
        runPlayer2();

    }

    public void runPlayer1(){
        Thread player1Thread=new Thread(() -> {
            properties(player1, currentStateP1);
            while (currentStateP1 != QUIT || currentStateP2 != QUIT) {
                System.out.println("tråd 1 aktiv");

                try {
                    currentStateP1 = (int) player1.receive();
                    System.out.println("har mottagit 1 state: " + currentStateP1);

                    properties(player1, currentStateP1);

                    System.out.println("har uppdaterat 1 state: " + currentStateP1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        player1Thread.start();
    }

    public void runPlayer2(){
        Thread player2Thread=new Thread(() -> {
            properties(player2, currentStateP2);
            while (currentStateP2 != QUIT || currentStateP1 != QUIT) {
                System.out.println("tråd 2 aktiv");

                try {
                    currentStateP2 = (int) player2.receive();
                    System.out.println("har mottagit 2 state: " + currentStateP2);

                    properties(player2, currentStateP2);

                    System.out.println("har uppdaterat 2 state: " + currentStateP2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        player2Thread.start();
    }

    public void properties(Player player, int currentState) {

        if (player1.hasPlayedRound && player2.hasPlayedRound) {
            roundOver = true;
        }

        if (currentState == INITIAL){
            player.send(INITIAL);
            //currentState = (int) player.receive();
            //System.out.println("Här är du " + currentState);
            //return currentState;

        } else if (currentState == ENTER_USERNAME){
            player.send(ENTER_USERNAME);
            try {
                player.username = (String) player.receive();
                System.out.println(player.username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player1.setTurnToChoose(true);

            player.send(WAITING);

        }else if(currentState == WAITING){

            System.out.println(player.username+ " is Waiting");
            System.out.println("player1 boolean: "+ player1.hasPlayedRound);
            if (player==player1 && player2.hasPlayedRound){
                System.out.println("player 1 wait quiz");
                player.send(QUIZZING);
            }
            else if (player==player2 && player1.hasPlayedRound){
                System.out.println("player 2 wait quiz");
                player.send(QUIZZING);
            }
            else if (player==player1 && player.turnToChoose){
                System.out.println("Skickar 1 till kategori");
                player1.send(CHOOSE_CATEGORY);
                player1.setTurnToChoose(false);}
            else if (player==player2 && player.turnToChoose){
                System.out.println("skickar 2 till kategori");
                player2.send(CHOOSE_CATEGORY);
                player2.setTurnToChoose(false);}
            else{player.send(WAITING);}


        }else if (currentState == CHOOSE_CATEGORY){

            this.currentPlayer=player;
            player.themeChoice = (String) player.receive();
            System.out.println("mottagit "+ player.themeChoice);
            questions=getQuestions();
            player.send(QUIZZING);
            player.setTurnToChoose(false);

        }else if (currentState == QUIZZING){
            //player.send(QUIZZING);
            System.out.println("ska skicka frågor");
            player.send(questions);

            try {
                player.pointsThisRound = (int) player.receive();
                System.out.println(player.pointsThisRound);
                player.setHasPlayedRound(true);
                counterOfRounds++;
            } catch (Exception e) {
                e.printStackTrace();
            }


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
        else if (currentState==QUIT){
            System.exit(0);
        }
        //return 1;
    }



    /*
    public void run() {
        int temp = 4;

        if (currentState == INITIAL){
            player1.send(INITIAL);
            player2.send(INITIAL);
            currentState = (int) player1.receive();
            System.out.println("Här är du " + currentState);
        } else if (currentState == ENTER_USERNAME){
            player1.send(ENTER_USERNAME);
            player2.send(ENTER_USERNAME);
            player1.username = (String) player1.receive();
            player2.username = (String) player2.receive();
            currentState = (int) player1.receive();
            //temp = (int) player2.receive();
            System.out.println(currentState);
            System.out.println(currentState);
            System.out.println(player1.username);
            System.out.println(player2.username);
        }else if(currentState == WAITING){
            player1.send(QUIZZING);
            player2.send(WAITING);
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

    } */

    public ArrayList[][] getQuestions() {
        String userThemeChoice = currentPlayer.getThemeChoice();
        List<QuestionClass> allThemedQuestions = ClassMaker.valueOf(userThemeChoice).getQuestions();
        Collections.shuffle(allThemedQuestions);
        List<QuestionClass> questions = allThemedQuestions.subList(0, this.questionsPerRound);

        ArrayList[][] questionArray= new ArrayList[this.questionsPerRound][3];
        for (int i = 0; i < this.questionsPerRound; i++) {
            for (int j = 0; j < 3; j++) {
                questionArray[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < this.questionsPerRound; i++) {
            questionArray[i][0].add(questions.get(i).getQuestion());
            questionArray[i][1].addAll(questions.get(i).getOptions());
            questionArray[i][2].add(questions.get(i).getCorrectAnswer());
        }
        return questionArray;
    }

}


