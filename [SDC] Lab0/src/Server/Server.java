package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Bank.BankImpl;

/**
 * Created by MGonc on 15/02/16.
 */
public class Server {

    static BankImpl bank = null;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            bank = new BankImpl();
            System.out.println("[Server] Server started");
            while (true) {
                Socket socket = ss.accept();
                ClientHandler ch = new ClientHandler(socket, bank);
                ch.run();
            }
        } catch (IOException ex) {
            System.out.println("[Server] IOexception");
        }
    }
}
