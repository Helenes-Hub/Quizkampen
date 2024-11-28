package Server;

import java.io.*;
import java.net.Socket;

public class Player {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    protected Player opponent;
    private Socket socket;
    protected String username;
    protected String themeChoice;
    protected Boolean turnToChoose = false;
    protected Boolean hasPlayedRound = false;
    protected Boolean gameOver = false;
    int pointsThisRound;
    int[] pointsAllRounds;
    int totalPoints;

    private final int INITIAL = 0;
    private final int ENTER_USERNAME = 1;
    private final int CHOOSE_CATEGORY = 2;
    private final int QUIZZING = 3;
    private final int WAITING = 4;
    private final int SHOW_SCORE_THIS_ROUND = 5;
    private final int FINAL = 6;
    private final int WAITING_FOR_SCORE = 7;
    private int currentState = 0;

    public Boolean getHasPlayedRound() {
        return hasPlayedRound;
    }

    public void setHasPlayedRound(Boolean hasPlayedRound) {
        this.hasPlayedRound = hasPlayedRound;
    }

    public Boolean getTurnToChoose() {
        return turnToChoose;
    }

    public void setTurnToChoose(Boolean turnToChoose) {
        this.turnToChoose = turnToChoose;
    }

    public Player(Socket socket) {
        this.socket = socket;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("Send failed");
        }
    }

    public Object receive() throws InterruptedException {
        try {
            Object message = in.readObject();
            return message;
        } catch (IOException | ClassNotFoundException e) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            throw new RuntimeException();
        }
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setRounds(int rounds) {
        pointsAllRounds = new int[rounds];
    }

    public String getUsername() {
        return username;
    }

    public String getThemeChoice() {
        return themeChoice;
    }

    public void addPointsThisRound(int roundNumber, int pointsThisRound) {
        pointsAllRounds[roundNumber] = pointsThisRound;
    }

    public int getPointsThisRound() {
        return pointsThisRound;
    }

    public int[] getPointsAllRounds() {
        return pointsAllRounds;
    }

    public int getTotalScore() {
        int totalScore = 0;
        for (int points : pointsAllRounds) {
            totalScore = totalScore + points;
        }
        return totalScore;
    }

    public int getINITIAL() {
        return INITIAL;
    }

    public int getENTER_USERNAME() {
        return ENTER_USERNAME;
    }

    public int getCHOOSE_CATEGORY() {
        return CHOOSE_CATEGORY;
    }

    public int getQUIZZING() {
        return QUIZZING;
    }

    public int getWAITING() {
        return WAITING;
    }

    public int getSHOW_SCORE_THIS_ROUND() {
        return SHOW_SCORE_THIS_ROUND;
    }

    public int getFINAL() {
        return FINAL;
    }

    public int getWAITING_FOR_SCORE() {
        return WAITING_FOR_SCORE;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public void close() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
