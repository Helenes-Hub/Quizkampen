import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//Spelarklass som sköter in och utströmmar och skapar spelare till GameFlow
public class Player {

    GameFlow gf;
    Player opponent;
    Socket socket;
    String username;
    int pointsThisRound;
    int[] pointsAllRounds;

    public Player(Socket socket) {
        this.socket = socket;

        try(PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            //KOD

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void setRounds(int rounds) {
        pointsAllRounds = new int[rounds];
    }
}
