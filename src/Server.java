import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    //Bör denna kallas för Server?

    private final int port = 5050;

    public Server() {

        try(ServerSocket serverS = new ServerSocket(port)) {
            while (true) {
                GameFlow player1 = new GameFlow(serverS.accept());
                GameFlow player2 = new GameFlow(serverS.accept());

                player1.start();
                player2.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
