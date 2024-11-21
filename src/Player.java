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
    Protocol protocol = new Protocol();

    public Player(Socket socket) {
        this.socket = socket;

        try{this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Skriver ut välkomstmeddelande
            send(protocol.getOutput(0));
            //Läser in username
            username = receive();
            //Välj kategori visas
            send(protocol.getOutput(1));
            //Sparar vald kategori som läses in av GameFlow
            themeChoice = receive();
            //Skickar frågor till spelare
            send(protocol.getOutput(2));
            //Spelet spelas
            //Metod som hanterar spel?
            pointsThisRound = Integer.parseInt(receive());
            send(protocol.getOutput(3));
            //Den här borde gå att göra bättre. En egen metod? Skicka in counterOfRounds och sparar pointsThisRound i arrayen
            addPointsThisRound(counterOfRounds, pointsThisRound);
            //Skickar och visar poäng denna runda
            send(protocol.getOutput(4));
            //Tillbaka till quizzing och rond 2
            send(protocol.getOutput(2));
            //Skickar och visar poäng denna runda
            send(protocol.getOutput(4));
            //Spelet slut och slutresultat visas
            send(protocol.getOutput(5));


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

    public void addPointsThisRound(int roundNumber,int pointsThisRound) {
        pointsAllRounds[roundNumber] = pointsThisRound;
        counterOfRounds++;
    }
}
