import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
//Start på klassen. Skapar writers, readers och kopplar upp socket med port och adress.
public class Client {

    private final int port = 5050;
    private InetAddress address = InetAddress.getLoopbackAddress();

    public Client() {
        //La till för att kunna testa panelerna
        //GamePanel gamePanel = new GamePanel();
        try (Socket socketToServer = new Socket(address, port);
             PrintWriter out = new PrintWriter(socketToServer.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));) {

            //KOD


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
    }

}
