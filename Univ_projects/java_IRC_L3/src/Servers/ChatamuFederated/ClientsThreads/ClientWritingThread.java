package Servers.ChatamuFederated.ClientsThreads;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Thread activity:
 * 1) Read the messages in the ArrayBlockingQueue
 * 2) Send them to this dedicated thread's client
 */
public class ClientWritingThread implements Runnable {
    private final ArrayBlockingQueue<String> messagesQueues;
    private final PrintWriter out;

    public ClientWritingThread(ArrayBlockingQueue<String> messagesQueue, Socket socket) throws IOException {
        this.messagesQueues = messagesQueue;
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        while( true ){
            String message;
            // First check if the queue still exists: If the client disconnected, then this queue would be null.
            if( messagesQueues == null ){ return;}
            try {
                message = messagesQueues.take(); //This call is blocking.
                out.println( message );
            } catch (InterruptedException e) { System.err.println("Error in ClientwritingThread:"); e.printStackTrace(); }
        }
    }
}
