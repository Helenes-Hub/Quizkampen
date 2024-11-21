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
    Socket socketToServer;
    PrintWriter out;
    BufferedReader in;

    public Client() {
        try {this.socketToServer = new Socket(address, port);
            this.out = new PrintWriter(socketToServer.getOutputStream(), true);
             this.in = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));



            close();

        } catch (IOException e) {
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
