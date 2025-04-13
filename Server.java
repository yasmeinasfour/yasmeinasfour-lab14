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

    private int countFactors(long number) {
        int count = 0;
        for (long i = 1; i <= number; i++) {
            if (number % i == 0) {
                count++;
            }
        }
        return count;
    }

    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String handshake = in.readLine();
            if (!"12345".equals(handshake)) {
                out.println("couldn't handshake");
                clientSocket.close();
                return;
            }

            String numberStr = in.readLine();
            try {
                long number = Long.parseLong(numberStr);
                if (number > Integer.MAX_VALUE) {
                    out.println("number too large");
                } else {
                    int factorCount = countFactors(number);
                    out.println("number " + number + " has " + factorCount + " factors");
                }
            } catch (Exception e) {
                out.println("exception on the server");
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}