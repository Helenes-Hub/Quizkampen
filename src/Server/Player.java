package Server;

import java.io.*;
import java.net.Socket;

public class Player {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Player opponent;
    private Socket socket;
    private String username;
    private String themeChoice;
    private Boolean turnToChoose = false;
    private Boolean hasPlayedRound = false;
    private Boolean gameOver = false;
    private int pointsThisRound;
    private int totalPoints;

    private int currentState = 0;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThemeChoice() {
        return themeChoice;
    }

    public void setThemeChoice(String themeChoice) {
        this.themeChoice = themeChoice;
    }

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

    public Boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getPointsThisRound() {
        return pointsThisRound;
    }

    public void setPointsThisRound(int pointsThisRound) {
        this.pointsThisRound = pointsThisRound;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
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
