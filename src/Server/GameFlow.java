package Server;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GameFlow extends Thread {

    Thread player1Thread;
    Thread player2Thread;

    private final int INITIAL = 0;
    private final int ENTER_USERNAME = 1;
    private final int CHOOSE_CATEGORY = 2;
    private final int QUIZZING = 3;
    private final int WAITING = 4;
    private final int SHOW_SCORE_THIS_ROUND = 5;
    private final int FINAL = 6;
    private final int FINAL_WAIT = 7;
    private final int QUIT = 8;

    private int timer;
    private int questionsPerRound;
    private int rounds;
    private int counterOfRounds = 0;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Object questions;
    private Boolean roundOver = false;
    private volatile Boolean gameIsOver = false;

    public GameFlow(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

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
        player1Thread = new Thread(() -> {
            Object message = null;
            player1.send(timer);
            player1.send(questionsPerRound);
            player1.send(INITIAL);
            player1.setTurnToChoose(true);
            while (!player1.gameOver && !gameIsOver) {
                try {
                    message = player1.receive();
                    if (!gameIsOver) {
                        properties(player1, message);
                    }
                } catch (RuntimeException e) {
                    endGame(true);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        player1Thread.start();
    }

    public void runPlayer2() {
        player2Thread = new Thread(() -> {
            Object message = null;
            player2.send(timer);
            player2.send(questionsPerRound);
            player2.send(INITIAL);
            while (!player2.gameOver && !gameIsOver) {
                try {
                    message = player2.receive();
                    if (!gameIsOver) {
                        properties(player2, message);
                    }
                } catch (RuntimeException e) {
                    endGame(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        player2Thread.start();
    }

    public void properties(Player player, Object message) {

        synchronized (this) {
            if (message == null || message.equals("QUIT")) {
                endGame(true);
            }

            if (player1.getHasPlayedRound() && player2.getHasPlayedRound()) {
                player1.setHasPlayedRound(false);
                player2.setHasPlayedRound(false);
                currentPlayer.themeChoice = null;
                currentPlayer.turnToChoose = false;
                currentPlayer.opponent.turnToChoose = true;
                counterOfRounds++;
                roundOver = true;
            }
        }
        switch (player.getCurrentState()) {

            case INITIAL:
                if (message.equals("STEP_FINISHED")) {
                    player.setCurrentState(ENTER_USERNAME);
                    player.send(ENTER_USERNAME);
                }
                break;
            case ENTER_USERNAME:
                if (player.username == null) {
                    try {
                        player.username = (String) player.receive();
                        player.setCurrentState(WAITING);
                        player.send(WAITING);
                    } catch (RuntimeException e) {
                        endGame(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_CATEGORY:
                if (player.themeChoice == null) {
                    try {
                        currentPlayer = player;

                        try {
                            player.themeChoice = (String) player.receive();
                            questions = getQuestions();
                            player.setCurrentState(QUIZZING);
                            player.send(QUIZZING);
                        } catch (RuntimeException e) {
                            endGame(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case QUIZZING:
                if ("STEP_FINISHED".equals(message) || message.equals(QUIZZING)) {
                    try {
                        player.send(questions);
                        try {
                            player.pointsThisRound = (int) player.receive();
                            player.totalPoints += currentPlayer.pointsThisRound;
                            player.setHasPlayedRound(true);
                            player.setCurrentState(WAITING);
                            player.send(WAITING);
                        } catch (RuntimeException e) {
                            endGame(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case WAITING:
                synchronized (this) {

                    if (player.getOpponent().hasPlayedRound && (!player.hasPlayedRound)) {
                        player.setCurrentState(QUIZZING);
                        player.send(QUIZZING);
                        break;
                    } else if (roundOver) {
                        player.setCurrentState(SHOW_SCORE_THIS_ROUND);
                        player.send(SHOW_SCORE_THIS_ROUND);
                        player.opponent.setCurrentState(SHOW_SCORE_THIS_ROUND);
                        player.opponent.send(SHOW_SCORE_THIS_ROUND);
                        roundOver = false;
                        try {
                            player1.send(player1.opponent.pointsThisRound);
                            player1.send(player1.opponent.username);
                            player2.send(player2.opponent.pointsThisRound);
                            player2.send(player2.opponent.username);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    } else if (player.turnToChoose && (!player.hasPlayedRound)) {
                        player.setCurrentState(CHOOSE_CATEGORY);
                        player.send(CHOOSE_CATEGORY);
                        break;
                    } else if (player.opponent.getCurrentState() == WAITING) {
                        player.opponent.send(WAITING);
                        break;
                    } else {
                        System.out.println("inga kriterium uppnåddes");
                    }
                    break;
                }
            case SHOW_SCORE_THIS_ROUND:
                if (player.opponent.getCurrentState() == WAITING
                        && player.opponent.hasPlayedRound
                        && rounds <= counterOfRounds) {
                    player.setCurrentState(FINAL);
                    player.opponent.setCurrentState(FINAL);
                    player.send(FINAL);
                    player.opponent.send(FINAL);
                    break;
                } else if (message.equals("STEP_FINISHED") && rounds <= counterOfRounds) {
                    player.setCurrentState(FINAL);
                    player.send(FINAL_WAIT);
                    break;
                } else {
                    player.setCurrentState(WAITING);
                    player.send(WAITING);
                }
            case FINAL:
                if (message.equals("QUIT")) {
                    endGame(true);
                }
                /*if (player.getCurrentState() == FINAL &&
                        player.opponent.getCurrentState() == FINAL) {
                    player1.send(FINAL);
                    player2.send(FINAL);
                    break;
                } Detta stör stängningen på något sätt....
                 */
        }
    }

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
            List<String> shuffledOptions = new ArrayList<>(questions.get(i).getOptions());
            Collections.shuffle(shuffledOptions);
            questionArray[i][1].addAll(shuffledOptions);
            questionArray[i][2].add(questions.get(i).getCorrectAnswer());
        }
        return questionArray;
    }

    public synchronized void endGame(Boolean gameOver) {
        if (gameOver && !gameIsOver) {
            gameIsOver = true;
            System.out.println("Trådar stängs av " + Thread.currentThread().getName());

            try {
                player1.gameOver = true;
                player2.gameOver = true;

                Thread.sleep(100);

                player1.close();
                player2.close();

                player1Thread = null;
                player2Thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                gameIsOver = false;
            }
        }
    }
}


