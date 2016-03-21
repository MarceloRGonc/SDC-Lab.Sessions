package Server;

import java.io.*;
import java.net.*;
import Bank.BankImpl;
import Messages.*;
import net.sf.jgcs.*;
import net.sf.jgcs.Message;
import net.sf.jgcs.ip.*;
import static Messages.Message.*;

/**
 * Created by MGonc on 15/02/16.
 */
public class Server implements MessageListener {

    private static BankImpl bank = null;
    private static DataSession dSession = null;
    private static IpGroup group = null;
    private static Protocol p = null;

    public Server(IpGroup group, Protocol p) {
        try {
            this.group = group;
            this.p = p;

            this.dSession = p.openDataSession(group);
            this.dSession.setMessageListener(this);
            ControlSession cs = this.p.openControlSession(group);
            cs.join();
        } catch (GroupException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {

            bank = new BankImpl();

            System.out.println("[Server] Server started");

            IpProtocolFactory pf = new IpProtocolFactory();
            group = new IpGroup("231.1.1.1:54321");
            p = pf.createProtocol();
            new Server(group, p);
            while (true) {
                Thread.sleep(10000);
            }
        } catch (GroupException ex){
            ex.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int count = 0;
    private void add() { count++; }
    private int getCount() { return count; }

    public Object onMessage(Message msg) {
        try {
            ObjectInputStream oInput;
            ByteArrayInputStream bInput = new ByteArrayInputStream(msg.getPayload());
            oInput = new ObjectInputStream(bInput);
            Messages.Message op = (Messages.Message) oInput.readObject();

            Response res = null;
            boolean flag = true;
            if(op instanceof Operation) {
                switch (op.getType()) {
                    case MOVE:
                        float value = op.getAmount();
                        boolean result = bank.move(value);
                        res = new Response(Type.MOVE, result, ((Operation) op).getVmid(),
                                ((Operation) op).getMsgNumber());
                        break;
                    case BALANCE:
                        res = new Response(Type.BALANCE, bank.getBalance(),
                                ((Operation) op).getVmid(), ((Operation) op).getMsgNumber());
                        break;
                    case LEAVE:
                        res = new Response(Type.LEAVE, ((Operation) op).getVmid(),
                                ((Operation) op).getMsgNumber());
                        flag = false;
                        break;
                }

                ByteArrayOutputStream bOuput = new ByteArrayOutputStream();
                ObjectOutputStream oosHere = new ObjectOutputStream(bOuput);

                oosHere.writeObject(res);
                byte[] data = bOuput.toByteArray();
                Message toSend = dSession.createMessage();
                toSend.setPayload(data);
                System.out.println("[" + res.getVmid().hashCode() + " - " + res.getMsgNumber()  + "] "
                        + "Sent response! Balance: " + bank.getBalance());
                dSession.multicast(toSend, new IpService(), null);
                add();

                if(!flag){ System.out.println("Movements: " + getCount()); }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
