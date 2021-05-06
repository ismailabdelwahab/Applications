package Servers.ChatamuCentral.ClientThreads;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class WriteToClientThread implements Runnable{
    private HashMap<String, ArrayBlockingQueue<String>> hashmapQueues;
    private final Socket clientSocket;
    private final String socketIdentity;
    private final PrintWriter out;

    WriteToClientThread(HashMap<String, ArrayBlockingQueue<String>> hashmapQueues,
                        Socket clientSocket, String socketIdentity, PrintWriter out) {
        this.hashmapQueues = hashmapQueues;
        this.clientSocket = clientSocket;
        this.socketIdentity = socketIdentity;
        this.out = out;
    }

    @Override
    public void run() {
        ArrayBlockingQueue<String> queue = hashmapQueues.get( this.socketIdentity );
        while( true ){
            String message;
            // Check if this client's queue still exists:
            if( queue == null ){ return;}
            // Consummate a message from this client's queue
            try {
                message = queue.take();
                out.println( message );
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}
