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
    String themeChoice;
    int pointsThisRound;
    int[] pointsAllRounds;
    Protocol protocol;

    public Player(Socket socket) {
        this.socket = socket;

        try(PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            //Skriver ut välkomstmeddelande
            out.println(protocol.getOutput(0));
            //Läser in username
            username = input.readLine();
            //Välj kategori visas
            out.println(protocol.getOutput(1));
            //Sparar vald kategori som läses in av GameFlow
            themeChoice = input.readLine();
            //Skickar frågor till spelare
            out.println(protocol.getOutput(2));
            //Behöver fortsättas



            //KOD

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
}
