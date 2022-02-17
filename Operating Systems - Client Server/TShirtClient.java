/*
Carson Seese - 09-28-2019 - TShirtClient.java
CIT344 Assignment 2: TShirt Client/Server

Note: Messages exchanged between the client and server are in CSV format for simplicity. The format is as follows:
[Client ID],[Action],[Size],[OK]. Client ID = The Client's ID generated when a client thread is started (-1 if client
never actually connected), the Action will either be 0 (Disconnect) or 1 (Get a shirt), the size will be the shirt size
(0 = Small, 1 = Medium, 2 = Large), and OK will either be 0 (OK/No Shirt) or 1 (OK/Shirt Available).
 */

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TShirtClient {

    public static final int NUM_CLIENTS = 100; // Number of client threads to run at a time

    public static void main(String[] args) {
        for(int i = 1; i <= NUM_CLIENTS; i++) {
            new Thread(new Client(i, (int) (Math.random() * ((2 - 0) + 1)))).start();
        }
    }
}
//(int) (Math.random() * ((2 - 0) + 1)))
class Client implements Runnable {

    private int id, size = 0;

    private final String LOG_FMT = "[User #%d] %s";

    /**
     * Client initializes a new client thread
     * @param id Client ID
     * @param size Shirt order Size
     */
    public Client(int id, int size) {
        this.id = id;
        this.size = size;
    }

    /**
     * Get the string related to shirt size.
     * @param size Small = 0, Medium = 1, Large = 2
     * @return Small = 0, Medium = 1, Large = 2
     */
    public String getSize(int size) {
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
        Socket s = null;
        BufferedReader serverReader = null;
        BufferedWriter serverWriter = null;
        try {
            s = new Socket("127.0.0.1", 5050);
            serverReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            serverWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Exception connecting to server or initializing reader/writer. Shutting down. Exception: " + e.toString());
            return;
        }

        System.out.println(String.format(LOG_FMT, id, "Requesting shirt size " + getSize(size)));
        try {
            serverWriter.write(String.format("%d,%d,%d,%d\n", id, 1, size, 1)); // Request a shirt
            serverWriter.flush();

            String line;
            String[] serverMessage;
            while((line = serverReader.readLine()) != null) {
                serverMessage = line.split(","); // Message type is CSV, so split at each comma

                // Check whether or not the message was "OK"
                if(Integer.parseInt(serverMessage[3]) == 0) {
                    System.out.println(String.format(LOG_FMT, id, "No shirts available in size " + getSize(size)));
                    break;
                }
                System.out.println(String.format(LOG_FMT, id, "Got shirt in size " + getSize(size)));
                break;
            }
        } catch (IOException e) {
            System.err.println(String.format(LOG_FMT, id, "Error communicating with server. Exception: " + e.toString()));
        }

        // Inform the server the client is disconnecting and close the serverWriter and serverReader
        try {
            serverWriter.write(String.format("%d,%d,%d,%d\n", id, 0, 0, 0));
            serverWriter.flush();
            serverWriter.close();
            serverReader.close();
        } catch (IOException e) {
            System.err.println(String.format(LOG_FMT, id, "Exception while attempting to disconnect: " + e.toString()));
        }
    }
}
