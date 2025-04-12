import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handshake() {
    out.println("12345");
    }
    public String request(String number) {
    try {
        out.println(number);
        return in.readLine();
    } catch (IOException e) {
        e.printStackTrace();
        return "failed to send request.";
    }
    }




}