package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private final int port = 5050;
    private Socket socketToServer;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    protected int opponentScoreThisRound;
    protected int opponentTotalScore;

    public Client() {
        try {
            this.socketToServer = new Socket("127.0.0.1", port);
            this.out = new ObjectOutputStream(socketToServer.getOutputStream());
            this.in = new ObjectInputStream(socketToServer.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object receive() {
        try {
            Object message = in.readObject();
            return message;
        } catch (IOException | ClassNotFoundException e) {
            close();
            System.exit(0);
        }
        return null;
    }

    public synchronized void close() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socketToServer != null) {
                socketToServer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getOpponentScoreThisRound() {
        return opponentScoreThisRound;
    }

    public void setOpponentScoreThisRound(int opponentScoreThisRound) {
        this.opponentScoreThisRound = opponentScoreThisRound;
    }

    public int getOpponentTotalScore() {
        return opponentTotalScore;
    }

    public void setOpponentTotalScore(int opponentTotalScore) {
        this.opponentTotalScore = opponentTotalScore;
    }
}
