package test5;

import java.io.Serializable;

public class Message implements Serializable {

    private String message;
    private int numClient;

    public Message(String m, int n) {
        message = m;
        numClient = n;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Client " + numClient +
                " :: " + message;
    }
}
