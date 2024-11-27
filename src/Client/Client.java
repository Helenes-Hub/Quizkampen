package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
//Start på klassen. Skapar writers, readers och kopplar upp socket med port och adress.
public class Client {

    private final int port = 5050;
    private InetAddress address = InetAddress.getLoopbackAddress();
    Socket socketToServer;
    ObjectOutputStream out;
    ObjectInputStream in;

    int opponentScoreThisRound;
    int opponentTotalScore;
    int score;
    int totalScore;

    public Client() {
        try {this.socketToServer = new Socket("127.0.0.1", port);
            this.out = new ObjectOutputStream(socketToServer.getOutputStream());
             this.in = new ObjectInputStream(socketToServer.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        //new Client.GamePanel(this);
    }

    public void send(Object message){
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object receive()  {
        try {
            Object message = in.readObject();
            return message;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try{
            in.close();
            out.close();
            socketToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

}
