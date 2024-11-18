import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private final int port = 5050;

    public Server() {

        try(ServerSocket serverS = new ServerSocket(port)) {
            while (true) {

                Player player1 = new Player(serverS.accept());
                Player player2 = new Player(serverS.accept());
                GameFlow gf = new GameFlow(player1, player2);

                gf.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
