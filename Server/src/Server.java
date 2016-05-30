import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by franck on 5/30/16.
 */
public class Server {

    private int port;
    private ServerSocket servSocket;

    public Server (int port){
        this.port = port;
        try {
            this.servSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run (){
        try {
            Connection clientCo = new Connection (servSocket.accept());
            (new Thread(){
                public void run(){
                    clientCo.run();
                }}).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
