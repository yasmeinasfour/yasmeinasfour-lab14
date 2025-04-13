import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private ServerSocket serverSocket;
    private boolean running;
    private ArrayList<LocalDateTime> connectedTimes;
    private ExecutorService threadPool;



    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.running = true;
            this.connectedTimes = new ArrayList<>();
            this.threadPool = Executors.newCachedThreadPool();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void serve(int numClients) {
        for (int i = 0; i < numClients && running; i++) {
            try {
                Socket clientSocket = serverSocket.accept();
                LocalDateTime connectionTime = LocalDateTime.now();
                connectedTimes.add(connectionTime);

                Runnable task = new Runnable() {
                    public void run() {
                        handleClient(clientSocket);
                    }
                };
                threadPool.execute(task);
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }


}