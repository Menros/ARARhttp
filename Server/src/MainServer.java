/**
 * Created by franck on 5/30/16.
 */
public class MainServer {

    public static void main(String[] args) {
        Server s = new Server(2253);
        s.run();
    }

}
