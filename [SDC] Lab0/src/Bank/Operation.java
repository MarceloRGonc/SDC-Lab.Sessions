package Bank;

import java.io.Serializable;

/**
 * Created by MGonc on 15/02/16.
 */
public class Operation implements Serializable {

    Type op;
    float amount;
    boolean status;

    public enum Type { MOVE, BALANCE, LEAVE }

    public Operation(Type op) {
        this.op = op;
    }

    public Operation(Type op, boolean status) {
        this.op = op;
        this.status = status;
    }

    public Operation(Type op, float amount) {
        this.op = op;
        this.amount = amount;
    }

    public Type getOperation() { return this.op; }

    public float getAmount() { return this.amount; }

    public boolean getStatus() { return this.status; }

    public void setOperation(Type op) { this.op = op; }

    public void setAmount(float amount) { this.amount = amount; }

    public void setStatus(boolean status) { this.status = status; }
}
