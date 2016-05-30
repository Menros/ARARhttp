import java.net.Socket;

/**
 * Created by franck on 5/30/16.
 */
public class Connection extends Thread {

    private Socket coSocket;

    public Connection (Socket s){
        this.coSocket = s;
    }
}
