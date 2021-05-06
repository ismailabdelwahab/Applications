package Servers.ChatamuFederated.ClientsThreads;

import Tools.HashMapManagement.HashmapManager;
import Tools.SocketManagement.SocketEnding;
import Tools.StringParser.ServerStringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Thread activity:
 * 1) Receive a message from a client. (read "in" of the socket)
 * 2) Adds the message to all Client's queues (except to this own client's queue)
 * 3) Adds also the message to all Server's queues (so that clients on these servers can read the message)
 */
public class ClientReadingThread implements Runnable {
    private HashMap<String, ArrayBlockingQueue<String>> clientsQueues;
    private HashMap<String, ArrayBlockingQueue<String>> serversQueues;

    private final Socket clientSocket;
    private final String socketIdentity;
    private final String username;

    private final BufferedReader in;
    private final PrintWriter out;

    public ClientReadingThread(HashMap<String, ArrayBlockingQueue<String>> clientsQueues,
                               HashMap<String, ArrayBlockingQueue<String>> serversQueues,
                               Socket clientSocket, String socketIdentity, String username) throws IOException {
        this.clientsQueues = clientsQueues;
        this.serversQueues = serversQueues;
        this.clientSocket = clientSocket;
        this.socketIdentity = socketIdentity;
        this.username = username;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }
    private String getClientIdentity(){ return "["+this.username+"]"; }

    @Override
    public void run() {
        String buffer;
        String message;
        try {
            System.out.println(">>>>>>> New user connected: " + socketIdentity + " as: " + "["+username+"]");
            String newConnectionMessage = ">>>>>>> User " + "["+username+"]" + " joined the chat.";
            addMessageToBothQueues( newConnectionMessage );
            /* ****************** CHATTING PART ************** */
            do {
                buffer = in.readLine();
                if( buffer == null ) { break; }
                message = ServerStringParser.parseChatMessage( buffer );
                if ( message != null ) {
                    String chat_output =  this.username + "> " + message;
                    System.out.println( chat_output );
                    addMessageToBothQueues( chat_output );
                } else {// Error in MESSAGE protocol
                    out.println(">ERROR: [MESSAGE] chatamu");
                    //break // <- add this if you want the user to be disconnected in case of error in MESSAGE protocol
                }
            } while (true);
        /* ****************** Client disconnected ************** */
            notifyClientDisconnection();
        } catch (IOException | InterruptedException e) { System.err.println("Error in ClientReadingThread:");e.printStackTrace(); }
    }

    /**
     * Method launched when a client disconnect from the server
     */
    private void notifyClientDisconnection() throws InterruptedException, IOException {
        System.out.println("<<<<<< Client disconnected: " + this.socketIdentity+ " as: "+ getClientIdentity());
        // Remove the key to this socket in the HashMap of queues:
        HashmapManager.removeClientQueueFromQueues(clientsQueues, this.socketIdentity );
        // Signal to other members that this user is disconnected
        addMessageToBothQueues("<<<<<<  User "+ getClientIdentity() +" left the chat.");
        
        if ( this.clientSocket != null )
            SocketEnding.closeStreamsAndSocket( this.clientSocket );
    }

    private void addMessageToBothQueues(String message) throws InterruptedException {
        HashmapManager.addMessageToQueues(clientsQueues, this.socketIdentity, message);
        HashmapManager.addMessageToQueues(serversQueues, this.socketIdentity, message);
    }
}
