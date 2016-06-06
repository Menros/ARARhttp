import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by franck on 5/30/16.
 */
public class Connection extends Thread {

    //Attributes

    private Socket coSocket;
    private int errorState;

    //Constructors

    public Connection (Socket s){
        this.coSocket = s;
        this.errorState = 0;
    }

    //Methods

    public void run(){

        //Opening Input Socket Stream

        InputStream sIStream = null;
        try {
            sIStream = coSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream bufSIStream = new BufferedInputStream(sIStream);
        System.out.println("InputStream created");

        //Creating scanner

        Scanner sc = new Scanner(bufSIStream);
        sc.useDelimiter(" ");
        sc.next();

        //Opening file

        String fileName = sc.next();

        File fic = new File ("."+fileName);
        FileInputStream fileStream = null;

        try {
            fileStream = new FileInputStream(fic);
        } catch (FileNotFoundException e) {
            this.errorState = 404;
            System.out.println("Error 404 - not found");
        }

        System.out.println("File opened");

        //Reading file

        byte[] fileContent;
        if(this.errorState == 0) {
            fileContent = new byte[(int) fic.length()];
            try {
                fileStream.read(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(this.errorState == 404){
            fileContent = ("Error 404 - file not found").getBytes();
        }
        else fileContent = ("Unknown error").getBytes();

        //Opening Output Socket Stream

        OutputStream sOStream = null;
        try {
            sOStream = coSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //BufferedOutputStream bufSOStream = new BufferedOutputStream(sOStream);

        System.out.println("Output Stream created");

        //Building response

        String type = "";
        byte[] headers;

        if(this.errorState == 0) {
            int i = fileName.lastIndexOf('.');
            if (i >= 0) {
                type = fileName.substring(i + 1);
            }

            headers = ("HTTP/1.1 200 OK \r\n" +
                    "Content-Type: " + type + "\r\n" +
                    "Content-Length: " + fic.length() + "\r\n\r\n").getBytes();
        }
        else if(this.errorState == 404){
            headers = ("HTTP/1.1 404 Not Found \r\n" +
                    "Content-Type: html\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n\r\n").getBytes();
        }
        else{
            headers = ("HTTP/1.1 520 Web server is returning an unknown error \r\n" +
                    "Content-Type: html\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n\r\n").getBytes();
        }

        byte[] responseText = new byte[fileContent.length + headers.length];
        System.arraycopy(headers, 0, responseText, 0, headers.length);
        System.arraycopy(fileContent, 0, responseText, headers.length, fileContent.length);


        System.out.println("Response : " + new String(responseText));

        //Writing in stream

        try {
            sOStream.write(responseText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sOStream.flush();
            System.out.println("Response sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
