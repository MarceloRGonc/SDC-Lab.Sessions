package Client;

import Bank.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by MGonc on 15/02/16.
 */
public class BankStub implements Bank.Bank {

    Socket s;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    public BankStub() {
        try {
            String host = "localhost";
            int port = 12345;
            this.s = new Socket(host, port);
            oos = new ObjectOutputStream(this.s.getOutputStream());
            ois = new ObjectInputStream(this.s.getInputStream());
        } catch (IOException ex) {
        }
    }

    @Override
    public float getBalance() {
        float balance = -1;
        try {
            Operation r = new Operation(Operation.Type.BALANCE);
            oos.writeObject(r);
            oos.flush();
            balance = ((Operation) ois.readObject()).getAmount();
        } catch (IOException ex) {
            System.out.println("[BankStub] IOexception");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("[BankStub] ClassNotFoundException");
        }
        return balance;
    }

    @Override
    public boolean move(float amount) {
        boolean result = false;
        try {
            Operation r = new Operation(Operation.Type.MOVE, amount);
            oos.writeObject(r);
            oos.flush();
            result = ((Operation) ois.readObject()).getStatus();
        } catch (IOException ex) {
            System.out.println("[BankStub] IOexception");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("[BankStub] ClassNotFoundException");
        }
        return result;
    }

    public void leave() {
        try {
            oos.writeObject(new Operation(Operation.Type.LEAVE));
            ois.close();
            oos.close();
            s.close();
        } catch (IOException ex) {
            System.out.println("[BankStub] IOexception");
        }
    }
}
