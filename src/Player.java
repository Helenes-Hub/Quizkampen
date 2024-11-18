import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {

    Player opponent;
    Socket socket;

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
}
