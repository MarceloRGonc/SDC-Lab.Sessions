package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by MGonc on 15/02/16.
 */
public class Client {

    private static float balance = 0;

    private static BufferedReader brConsole;
    private static BufferedWriter bwConsole;

    private static void printMenu() throws IOException {
        bwConsole.write("Welcome\n");
        bwConsole.flush();
        bwConsole.write("1 - Deposit money\n");
        bwConsole.flush();
        bwConsole.write("2 - Withdraw money\n");
        bwConsole.flush();
        bwConsole.write("3 - Bank balance\n");
        bwConsole.flush();
        bwConsole.write("4 - Shutdown\n");
        bwConsole.flush();
    }

    public static void main(String[] args) {
        try {
            BankStub bank = new BankStub();

            brConsole = new BufferedReader(new InputStreamReader(System.in));
            bwConsole = new BufferedWriter(new OutputStreamWriter(System.out));

            boolean shutdown = false;

            while (!shutdown) {

                printMenu();

                String opt = brConsole.readLine().trim();
                String amount;

                switch (opt) {
                    case "1":
                        bwConsole.write("Amount: \n");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();

                        if (bank.move(Float.parseFloat(amount))) {
                            bwConsole.write("[Response] Success\n");
                            bwConsole.flush();
                            balance += Float.parseFloat(amount);
                        } else {
                            bwConsole.write("[Response] Error\n");
                            bwConsole.flush();
                        }
                        break;
                    case "2":
                        bwConsole.write("Amount: \n");
                        bwConsole.flush();
                        amount = brConsole.readLine().trim();
                        if (bank.move(0 - Float.parseFloat(amount))) {
                            bwConsole.write("[Response] Success\n");
                            bwConsole.flush();
                            balance -= Float.parseFloat(amount);
                        } else {
                            bwConsole.write("[Response] Error\n");
                            bwConsole.flush();
                        }
                        break;
                    case "3":
                        bwConsole.write("[Response] Real Balance: " + bank.getBalance() + "\n");
                        bwConsole.flush();
                        bwConsole.write("[Response] Expected Balance: " + balance + "\n");
                        break;
                    case "4":
                        shutdown = true;
                        bank.leave();
                        bwConsole.write("[Response] See you next time\n");
                        bwConsole.flush();
                        break;
                    default:
                        bwConsole.write("[Response] Something went wrong\n");
                        bwConsole.flush();
                }
            }
        } catch (IOException ex) {
            System.out.println("[Client] IOexception");
        }
    }
}
