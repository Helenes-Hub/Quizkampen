package Server;

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
    private final int WAITING_FOR_SCORE = 7;
    private final int QUIT = 8;
    /*private int currentStateP1 = 0;
    private int currentStateP2 = 0;
     */

    private int timer;
    private int questionsPerRound;
    private int rounds;
    private int counterOfRounds = 0;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Object questions;
    private Boolean roundOver = false;

    public GameFlow(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        //this.currentPlayer = player1;

        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);

        //loading properties
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("src/Server/Settings.properties"));
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

    public void runPlayer1() {
        Thread player1Thread = new Thread(() -> {
            Object message = null;
            player1.send(INITIAL);
            player1.setTurnToChoose(true);
            while (player1.getCurrentState() != QUIT || player2.getCurrentState() != QUIT) {
                System.out.println("tråd 1 aktiv");

                try {
                    message = player1.receive();
                    //properties(player1, message);
                    System.out.println("har mottagit 1 state: " + message);

                    properties(player1, message);

                    System.out.println("har uppdaterat 1 state: " + player1.getCurrentState());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        player1Thread.start();
    }

    public void runPlayer2() {
        Thread player2Thread = new Thread(() -> {
            Object message = null;
            player2.send(INITIAL);
            while (player2.getCurrentState() != QUIT || player1.getCurrentState() != QUIT) {
                System.out.println("tråd 2 aktiv");

                try {
                    message = player2.receive();
                    //properties(player2, message);
                    System.out.println("har mottagit 2 state: " + message + " med nuvarande status: "+ player2.getCurrentState());

                    properties(player2, message);

                    System.out.println("har uppdaterat 2 state: " + player2.getCurrentState());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        player2Thread.start();
    }
    //syncronized

    public void properties(Player player, Object message) {
        //player.send(INITIAL);

        synchronized (this) {
            if (player1.getHasPlayedRound() && player2.getHasPlayedRound()) {
                player1.setHasPlayedRound(false);
                player2.setHasPlayedRound(false);
            }
            }
            System.out.println(player+" processing "+message);
            switch (player.getCurrentState()) {

                case INITIAL:
                    System.out.println("player sent in initial");
                    if (message.equals("STEP_FINISHED")) {
                        System.out.println("player sent in step");
                        player.setCurrentState(ENTER_USERNAME);
                        //System.out.println(player.username);
                        player.send(ENTER_USERNAME);
                    }
                    break;
                case ENTER_USERNAME:
                    if (player.username==null){
                    try {
                        System.out.println("inväntar namn");
                        try {
                        player.username = (String) player.receive();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("användarnamn: "+player.username);
                        if (message.equals("STEP_FINISHED")) {
                            if (player.turnToChoose) {  //om man får välja skickas man vidare här
                                player.setCurrentState(CHOOSE_CATEGORY);
                                player.send(CHOOSE_CATEGORY);
                            } else {
                                player.setCurrentState(WAITING);
                                player.send(WAITING);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }}

                    break;
                case CHOOSE_CATEGORY:
                    if (player.themeChoice==null){
                    try {
                        currentPlayer=player;       //Här sätts currentPlayer. Först till spelare 1.
                        player.themeChoice = (String) player.receive();
                        System.out.println("spelarval: "+player.themeChoice);
                        System.out.println(player.themeChoice);
                        player.turnToChoose = false;    //När man fått välja ändras boolean till false
                        player.getOpponent().turnToChoose = true;   //Och motståndarens ändras till true. Motståndare får välja nästa gång.
                        questions=getQuestions();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }}
                    if (message.equals("STEP_FINISHED")) {
                        player.setCurrentState(QUIZZING);
                        player.send(QUIZZING);
                    }
                    break;
                case QUIZZING:
                    if ("STEP_FINISHED".equals(message) || message.equals(QUIZZING)) {
                        try {
                            System.out.println("skickar till spelare: "+ player.username);
                            player.send(questions);
                            //vart kommer denna ifrån? det är en extra
                            //STEP_FINISHED istället för score
                            //System.out.println("onödig STEP_FINISHED: "+player.receive().toString());
                            System.out.println("inväntar poäng från "+player.username);
                            try {
                            player.pointsThisRound = (int) player.receive();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            player.setHasPlayedRound(true);
                            //System.out.println(player.receive().toString());
                            player.pointsThisRound = (int) player.receive(); //Sparar rundans poäng
                            player.addPointsThisRound(counterOfRounds, player.pointsThisRound); //Sparar poäng till rundnumret - 1. En array där poängen sparar per runda
                            player.setHasPlayedRound(true); //När spelaren spelat en runda så sätts denna till true
                            player.setCurrentState(WAITING);
                            player.send(WAITING);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //player.turnToChoose = false;
                        //Ta emot in poäng
                    }
                    break;
                case WAITING:
                    if (player.opponent.getCurrentState() == WAITING) {
                            player.opponent.send(WAITING);
                        }
                    if (player.getOpponent().hasPlayedRound && (!player.hasPlayedRound)) {
                        player.setCurrentState(QUIZZING);
                        player.send(QUIZZING);
                    }
                    else if (player.turnToChoose && (!player.hasPlayedRound)) {
                        player.setCurrentState(CHOOSE_CATEGORY);
                        player.send(CHOOSE_CATEGORY);

                    } else if (roundOver) {
                        player.setCurrentState(SHOW_SCORE_THIS_ROUND);
                        player.send(SHOW_SCORE_THIS_ROUND);
                        currentPlayer.getOpponent().turnToChoose = true;    //Här ändras så "currentPlayer"s motståndare får välja kategori
                        // nästa gång. Första rundan är currentPlayer spelare 1, så nästa runda får spelare 2 välja kategori.
                    }
                    else {
                        //player.setCurrentState(WAITING);
                        //player.send(null);
                    }
                    break;
                case SHOW_SCORE_THIS_ROUND:
                    if (message.equals("STEP_FINISHED")&& rounds<=counterOfRounds) {
                        player.setCurrentState(FINAL);
                        player.send(FINAL);
                    }
                    else{
                        player.setCurrentState(WAITING);
                        player.send(WAITING);
                    }
            }
    }

        /*
        if (player.getCurrentState() == INITIAL) {
            player.send(INITIAL);
            player.setCurrentState(ENTER_USERNAME);
            //currentState = (int) player.receive();
            //System.out.println("Här är du " + currentState);
            //return currentState;

        } else if (player.getCurrentState() == ENTER_USERNAME) {
            player.send(ENTER_USERNAME);
            try {
                player.username = (String) player.receive();
                System.out.println(player.username);
                player.send(WAITING);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (player.getCurrentState() == WAITING) {

            System.out.println(player.username + " is Waiting");
            //System.out.println("player1 boolean: "+ player1.hasPlayedRound);
            if (player == player1 && player2.hasPlayedRound) {
                System.out.println("player 1 wait quiz");
                player.send(QUIZZING);
            } else if (player == player2 && player1.hasPlayedRound) {
                System.out.println("player 2 wait quiz");
                player.send(QUIZZING);
            } else if (player == player1 && player.turnToChoose) {
                System.out.println("Skickar 1 till kategori");
                //player1.setTurnToChoose(false);
                //player2.setTurnToChoose(true);
                player1.send(CHOOSE_CATEGORY);
            } else if (player == player2 && player.turnToChoose) {
                System.out.println("skickar 2 till kategori");
                //player2.setTurnToChoose(false);
                //player1.setTurnToChoose(true);
                player2.send(CHOOSE_CATEGORY);
            } else {
                player.send(WAITING);
            }


        } else if (player.getCurrentState() == CHOOSE_CATEGORY) {

            this.currentPlayer=player;
            try {
                player.themeChoice = (String) player.receive();
                System.out.println("mottagit " + player.themeChoice);
                questions = getQuestions();
            } catch (Exception e) {
                e.printStackTrace();
            }


            //player.setTurnToChoose(false);

        } else if (player.getCurrentState() == QUIZZING) {
            player.send(QUIZZING);
            System.out.println("ska skicka frågor");
            player.send(questions);

            try {
                player.pointsThisRound = (int) player.receive();
                System.out.println(player.pointsThisRound);
                player.setHasPlayedRound(true);
                counterOfRounds++;
                //player.send(WAITING);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (player.getCurrentState() == SHOW_SCORE_THIS_ROUND) {
            player1.send(SHOW_SCORE_THIS_ROUND);
            player2.send(SHOW_SCORE_THIS_ROUND);
            player1.send(player1.getPointsThisRound());
            player2.send(player2.getPointsThisRound());
            player1.send(player1.getPointsAllRounds());
            player2.send(player2.getPointsAllRounds());
        } else if (player.getCurrentState() == FINAL) {
            player1.send(FINAL);
            player1.send(player1.getTotalScore());
            player2.send(FINAL);
            player2.send(player2.getTotalScore());
        } else if (player.getCurrentState() == QUIT) {
            System.exit(0);
        }
    }
        //return 1;
    }
         */


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

        ArrayList[][] questionArray = new ArrayList[this.questionsPerRound][3];
        for (int i = 0; i < this.questionsPerRound; i++) {
            for (int j = 0; j < 3; j++) {
                questionArray[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < this.questionsPerRound; i++) {
            questionArray[i][0].add(questions.get(i).getQuestion());
            questionArray[i][1].addAll(questions.get(i).getOptions());
            questionArray[i][2].add(questions.get(i).getCorrectAnswer());
            System.out.println(questionArray[i][0].toString());
            System.out.println(questionArray[i][1].toString());
            System.out.println(questionArray[i][2].toString());
        }
        return questionArray;
    }

}


