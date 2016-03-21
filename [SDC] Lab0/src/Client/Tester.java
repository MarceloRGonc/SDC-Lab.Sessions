package Client;

import Bank.BankImpl;
import java.util.Random;

/**
 * Created by MGonc on 15/02/16.
 */
public class Tester {

    public static void main(String[] args) {
        BankStub bank = new BankStub();

        float moves = 0;
        for (int i = 0; i < 100000; i++) {
            Random rand = new Random();

            int value = rand.nextInt(100000)-46789;

            boolean status = bank.move(value);
            if (status) {
                moves += value;
            }
        }
        System.out.println("Moves: " + moves);
        System.out.println("Bank balance: " + bank.getBalance());
    }
}
