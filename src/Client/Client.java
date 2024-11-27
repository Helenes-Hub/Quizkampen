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

    public Client() {
        try {
            this.socketToServer = new Socket("127.0.0.1", port);
            this.out = new ObjectOutputStream(socketToServer.getOutputStream());
            this.in = new ObjectInputStream(socketToServer.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

             //new Client.GamePanel(this);
    }

    public void send(Object message){
        try {
            System.out.println("skickar till server: "+ message.toString());
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object receive()  {
        try {
            Object message = in.readObject();
            System.out.println("tar emot från server: "+ message.toString());
            return message;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client kan inte ta emot.");
            close();
            System.out.println("Försöker stänga.");
            System.exit(1);
        }
        return "FEL FEL FEL";
    }

    public synchronized void close() {
        try{
            if(socketToServer != null) {socketToServer.close();}
            if(in != null) {in.close();}
            if(out != null) {out.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        Client client = new Client();
    }
     */

}
