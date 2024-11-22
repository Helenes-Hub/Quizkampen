package Server;

import java.io.*;
import java.net.Socket;

//Spelarklass som sköter in och utströmmar(kommunikation) och skapar spelare till Server.GameFlow
public class Player {

    ObjectInputStream in;
    ObjectOutputStream out;
    Player opponent;
    Socket socket;
    String username;
    String themeChoice;
    int pointsThisRound;
    int[] pointsAllRounds;


    public Player(Socket socket) {
        this.socket = socket;

        try{this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public Object receive()  {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
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

    public void addPointsThisRound(int roundNumber,int pointsThisRound) {
        pointsAllRounds[roundNumber] = pointsThisRound;
    }

    public void close() {
        try{
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
