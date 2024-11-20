import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//Spelarklass som sköter in och utströmmar och skapar spelare till GameFlow
public class Player {

    BufferedReader in;
    PrintWriter out;
    Player opponent;
    Socket socket;
    String username;
    int pointsThisRound;
    int[] pointsAllRounds;

    public Player(Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Player streams", e);
        }


    }
    public void send(String message){
        out.println(message+ "\n");
    }

    public String receive()  {
        try {
            String message = in.readLine();
            if(message!=null){
                return message;
            }
            return "failed to read message";
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
}
