package Servers.ChatamuFederated.ServerThreads;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Thread acitivity:
 * 1) Similar to WriteToClientThread but interact with server
 * 2) For the sake of tracking thrown exception this class still exists so i can know if
 *    this thread is causing a problem or if it is the WriteToClientThread.
 */
public class WriteToServerThread implements Runnable{
    private final ArrayBlockingQueue<String> messagesQueues;
    private final PrintWriter out ;

    public WriteToServerThread(ArrayBlockingQueue<String> messagesQueue, Socket socket) throws IOException {
        this.messagesQueues = messagesQueue;
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        while( true ){
            String message;
            if( messagesQueues == null ){ return;}
            try {
                message = messagesQueues.take();
                out.println( message );
            }
            catch (InterruptedException e) {
                System.err.println("Error in WriteToServerThread.java:"); e.printStackTrace();
            }
        }
    }
}
