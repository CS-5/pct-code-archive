/*
Carson Seese - 09-28-2019 - TShirtServer.java
CIT344 Assignment 2: TShirt Client/Server

Note: Messages exchanged between the client and server are in CSV format for simplicity. The format is as follows:
[Client ID],[Action],[Size],[OK]. Client ID = The Client's ID generated when a client thread is started (-1 if client
never actually connected), the Action will either be 0 (Disconnect) or 1 (Get a shirt), the size will be the shirt size
(0 = Small, 1 = Medium, 2 = Large), and OK will either be 0 (OK/No Shirt) or 1 (OK/Shirt Available).
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TShirtServer {

    private static final int PORT = 5050;

    private static List inventory = Collections.synchronizedList(new ArrayList<>());

    // Main throws an IOException (as opposed to an internal try/catch) since failure to create a server socket is
    // likely a fatal error and would exit the program anyway.
    public static void main(String[] args) throws IOException {
        synchronized (inventory) {
            for(int i = 0; i < 3; i++) {
                inventory.add(i, 100);
            }
        }
        System.out.println("Starting Inventory: " + inventory.toString());

        ServerSocket server = new ServerSocket(PORT);
        ExecutorService tp = Executors.newCachedThreadPool();

        while(true) {
            Socket socket = server.accept();

            System.out.println(String.format("[Port %d] Connected!", socket.getPort()));
            tp.execute(new ClientHandler(
                    inventory,
                    socket,
                    socket.getInputStream(),
                    socket.getOutputStream()));
        }
    }
}

class ClientHandler implements Runnable {

    private int clientPort, clientId = -1;
    private List inventory;
    private Socket clientSocket;
    private InputStream clientIs;
    private OutputStream clientOs;

    private final String MSG_FMT = "%d,%d,%d,%d\n";
    private final String LOG_FMT = "[Client #%d, Port %d] %s";

    /**
     * ClientHandler initializes a new client handler thread
     * @param i Inventory list
     * @param s Client Socket
     * @param is Client InputStream
     * @param os Client OutputStream
     */
    public ClientHandler(List i, Socket s, InputStream is, OutputStream os) {
        this.inventory = i;
        this.clientPort = s.getPort();
        this.clientSocket = s;
        this.clientOs = os;
        this.clientIs = is;
    }

    /**
     * Get the total number of shirts in the inventory
     * @return An array of shirt totals. Index 0 = Small, 1 = Medium, 2 = Large
     */
    public int[] getInventory() {
        int[] out = new int[3];
        synchronized (inventory) {
            for(int i = 0; i < inventory.size(); i++) {
                out[i] = (int) inventory.get(i);
            }
        }
        return out;
    }

    /**
     * Get the total number of shirts of a given size
     * @param size Small = 0, Medium = 1, Large = 2
     * @return Total shirts
     */
    private int getTotal(int size) {
        synchronized (inventory) {
            return (int)inventory.get(size);
        }
    }

    /**
     * Get shirt checks if there are enough shirts of a certain size. If there are, it decrements the total by 1 and
     * returns true. Otherwise returns false.
     * @param size Small = 0, Medium = 1, Large = 2
     * @return Available shirt
     */
    private boolean getShirt(int size) {
        if (getTotal(size) == 0) return false;
        synchronized (inventory) {
            int i = (int)inventory.get(size) - 1;
            inventory.set(size, i);
        }
        return true;
    }

    /**
     * Get the string related to shirt size.
     * @param size Small = 0, Medium = 1, Large = 2
     * @return Small = 0, Medium = 1, Large = 2
     */
    private String getSize(int size) {
        switch (size) {
            case 0:
                return "small";
            case 1:
                return "medium";
            case 2:
                return "large";
            default:
                return "unknown size";
        }
    }

    @Override
    public void run() {
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientIs));
        BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientOs, StandardCharsets.UTF_8));

        String line;
        String[] clientMessage;
        try {
            while((line = clientReader.readLine()) != null) {
                clientMessage = line.split(","); // Message type is CSV, so split at each comma
                clientId = Integer.parseInt(clientMessage[0]);

                if (Integer.parseInt(clientMessage[1]) == 0) break; // If the client requests a disconnect

                int size = Integer.parseInt(clientMessage[2]);
                System.out.println(String.format(LOG_FMT, clientId, clientPort, "Shirt size: " + getSize(size) + " requested!"));
                if (getShirt(size)) {
                    clientWriter.write(String.format(MSG_FMT, clientId, 1, size, 1)); // If the shirt was successfully retrieved
                    clientWriter.flush();

                    System.out.println(String.format(LOG_FMT, clientId, clientPort, "Shirt size: " + getSize(size) + " retrieved!"));

                    // Print current inventory
                    StringBuilder sb = new StringBuilder("Current Inventory: ");
                    int[] inventory = getInventory();
                    for(int i = 0; i < inventory.length; i++) {
                        sb.append(String.format("%s %d. ", getSize(i), inventory[i]));
                    }
                    System.out.println(String.format(LOG_FMT, clientId, clientPort, sb.toString()));
                    continue;
                }
                clientWriter.write(String.format(MSG_FMT, clientId, 1, size, 0)); // If the shirt is unavailable
                clientWriter.flush();

                System.out.println(String.format(LOG_FMT, clientId, clientPort, "Shirt size: " + getSize(size) + " unavailable."));
            }
        } catch (IOException e) {
            System.err.println(String.format(LOG_FMT, clientId, clientPort, "Client disconnected unexpectedly."));
        }

        // Close clientReader, clientWriter, and clientSocket
        try {
            System.out.println(String.format(LOG_FMT, clientId, clientPort, "Disconnecting."));
            clientReader.close();
            clientWriter.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(String.format(LOG_FMT, clientId, clientPort, "Exception while attempting to disconnect: " + e.toString()));
        }
    }
}
