package Server;

import Bank.BankImpl;
import Bank.Operation;
import java.io.*;
import java.net.Socket;

/**
 * Created by MGonc on 15/02/16.
 */
public class ClientHandler implements Runnable {

    public static Socket ss = null;
    public static BankImpl bank = null;

    public ClientHandler(Socket socket, BankImpl bank) {
        this.bank = bank;
        this.ss = socket;
    }

    @Override
    public void run() {
        ObjectInput brClient = null;
        ObjectOutput bwClient;
        try {
            boolean shutdown = false;
            brClient = new ObjectInputStream(this.ss.getInputStream());
            bwClient = new ObjectOutputStream(this.ss.getOutputStream());

            Operation op;

            while (!shutdown) {
                op = (Operation) brClient.readObject();
                if (op.getOperation() == Operation.Type.MOVE) {
                    float value = op.getAmount();
                    boolean result = this.bank.move(value);
                    op = new Operation(Operation.Type.MOVE, result);
                    bwClient.writeObject(op);
                } else if (op.getOperation() == Operation.Type.BALANCE) {
                    op = new Operation(Operation.Type.BALANCE, this.bank.getBalance());
                    bwClient.writeObject(op);
                } else {
                    op = new Operation(Operation.Type.LEAVE, true);
                    bwClient.writeObject(op);
                    bwClient.close(); brClient.close();
                    shutdown = true;
                }
            }
        } catch (IOException ex) {
            System.out.println("[Client] IOexception");
        } catch (ClassNotFoundException ex) {
            System.out.println("[Client] ClassNotFoundException");
        } finally {
            try {
                if (brClient != null) {
                    brClient.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
