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

    public Client() {
        try {this.socketToServer = new Socket(address, port);
            this.out = new ObjectOutputStream(socketToServer.getOutputStream());
             this.in = new ObjectInputStream(socketToServer.getInputStream());

            close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        try {
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object receive()  {
        try {
            return in.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
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
