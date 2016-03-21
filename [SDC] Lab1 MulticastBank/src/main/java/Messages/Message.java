package Messages;

import java.io.Serializable;

/**
 * Created by MGonc on 27/02/16.
 */
public abstract class Message implements Serializable {

    public enum Type { MOVE, BALANCE, LEAVE }

    public abstract Type getType();

    public abstract float getAmount();

    public abstract boolean getResponse();

}
