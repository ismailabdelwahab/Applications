package Servers.ChatamuCentral.ClientThreads;

import Tools.HashMapManagement.HashmapManager;
import Tools.SocketManagement.SocketEnding;
import Tools.SocketManagement.SocketParser;
import Tools.StringParser.ServerStringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class ReadFromClientThread implements Runnable {
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int DEFAULT_QUEUE_CAPACITY = 100;

    private HashMap<String, ArrayBlockingQueue<String>> hashmapQueues;
    private ArrayBlockingQueue<String> newQueueOfMessages;

    private final Socket clientSocket;
    private final String socketIdentity;
    private String username;
    private final PrintWriter out;
    private final BufferedReader in;

    public ReadFromClientThread(HashMap<String, ArrayBlockingQueue<String>> hashmapQueues,
                                Socket clientSocket) throws IOException {
        this.hashmapQueues = hashmapQueues;
        this.clientSocket = clientSocket;
        this.socketIdentity = SocketParser.getSocketIdentity( this.clientSocket );
        newQueueOfMessages = new ArrayBlockingQueue<>( DEFAULT_QUEUE_CAPACITY );
        hashmapQueues.put( this.socketIdentity, newQueueOfMessages);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    private String getClientIdentity(){ return "["+this.username+"]"; }

    @Override
    public void run() {
        String buffer;
        String message;
        try {
            /* ***************** LOG IN PART ******************** */
            buffer = in.readLine();
            this.username = ServerStringParser.parseLogInMessage( buffer );
            if( this.username == null){
                out.println(">ERROR: [LOGIN] aborting chatamu protocol");
                System.err.println( "Error in {login} for client: " + this.socketIdentity  );
                return;
            }else if(this.username.length() > MAX_USERNAME_LENGTH){
                out.println(">ERROR: [USERNAME LENGTH] aborting chatamu protocol");
                System.err.println( ">User tried to use a too long username.");
                return;
            }
            else{ // New user is accepted on the chat
                System.out.println(">>>>>>> New user connected: "+ this.socketIdentity + " as: " + getClientIdentity());
                HashmapManager.addMessageToQueues(hashmapQueues, this.socketIdentity,
                        ">>>>>>> User "+ getClientIdentity() + " joined the chat.");
            }
            /* *********** Writing to client thread ************* */
            new Thread(new WriteToClientThread(hashmapQueues, clientSocket, socketIdentity, out)).start();
            /* ***** CHAT PART (Read from client thread) ******** */
            do {
                buffer = in.readLine();
                if( buffer == null ) { break; }
                message = ServerStringParser.parseChatMessage( buffer );
                if ( message != null ) {
                    System.out.println(getClientIdentity() + ": " + message);
                    HashmapManager.addMessageToQueues(hashmapQueues, socketIdentity,
                            this.username + "> " + message);
                }
                else{
                    out.println(">ERROR: [MESSAGE] chatamu");
                    //System.err.println(">Error in {message} for client: "+ this.socketIdentity + " as: " + getClientIdentity());
                    // break; // Here we don't break anymore, client is not disconnected,
                              // he is just signaled that there was an error in his message
                }
            }while (true);
            // Client disconnected from chat:
            System.out.println("<<<<<< Client disconnected: " + this.socketIdentity + " as: "+ this.username);
            // Remove the key to this socket in the HashMap of queues:
            HashmapManager.removeClientQueueFromQueues(hashmapQueues, this.socketIdentity );
            // Signal to other members that this user is disconnected
            HashmapManager.addMessageToQueues(hashmapQueues, this.socketIdentity,
                    "<<<<<<  User "+ getClientIdentity() +" left the chat.");
        } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        finally { // "Finnaly" block used to be sure that we close all streams and socket.
            try {
                if( clientSocket != null )
                    SocketEnding.closeStreamsAndSocket( this.clientSocket );
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
