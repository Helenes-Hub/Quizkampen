import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    //Bör denna kallas för Server?

    private final int port = 5050;

    public ServerListener() {

        try(ServerSocket serverS = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverS.accept();

                GameFlow gf = new GameFlow(socket);
                gf.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerListener serverL = new ServerListener();
    }
}
