package Servers.ChatamuFederated.ServerThreads;

import Tools.HashMapManagement.HashmapManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Thread acitivity:
 * 1) Receive a message from a server on the network
 * 2) Print the message to this server's terminal to keep track of the discussion
 * 2) Add this message ONLY to all clients's queues (do not forward this message to any other server !)
 */
public class ReadFromServerThread implements Runnable {
    private final HashMap<String, ArrayBlockingQueue<String>> clientsQueues;
    private final String socketIdentity;
    private final BufferedReader in;

    public ReadFromServerThread(HashMap<String, ArrayBlockingQueue<String>> clientsQueues,
                                Socket socket, String socketIdentity) throws IOException {
        this.clientsQueues = clientsQueues;
        this.socketIdentity = socketIdentity;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String message;
        try {
            while (true){
                message = in.readLine();
                if( message == null ) { break; }
                else {
                    HashmapManager.addMessageToQueues(clientsQueues, this.socketIdentity, message);
                    System.out.println(message);
                }
            }
        }
        catch (IOException | InterruptedException e) {
            System.err.println("Error in ServerReadingThread:"); e.printStackTrace();
        }

    }
}
