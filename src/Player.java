import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//Spelarklass som sköter in och utströmmar(kommunikation) och skapar spelare till GameFlow
public class Player {

    BufferedReader in;
    PrintWriter out;
    Player opponent;
    Socket socket;
    String username;
    String themeChoice;
    int counterOfRounds = 0;
    int pointsThisRound;
    int[] pointsAllRounds;


    public Player(Socket socket) {
        this.socket = socket;

        try{this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void send(String message){
        out.println(message);
    }

    public String receive()  {
        try {
            return in.readLine();
        } catch (IOException e) {
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
        counterOfRounds++;
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
