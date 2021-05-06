package Clients.NormalClient;

import Tools.SocketManagement.SocketEnding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteHandler implements Runnable {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader stdin;

///////////////////////// Constructor /////////////////////////////////////////////
    public WriteHandler(Socket clientSocket, PrintWriter out, BufferedReader stdin) {
        this.clientSocket = clientSocket;
        this.out = out;
        this.stdin = stdin;
    }

    @Override
    public void run() {
        String buffer;
        try {
            do {
                buffer = stdin.readLine();
                if ( buffer != null ) // Send client's message:
                    out.println( buffer );
                else{                 // Client send CTRL+C signal:
                    System.err.println("You ended the connexion.");
                    System.err.println("Server was notified, goodbye.");
                    if( clientSocket != null )
                       SocketEnding.closeStreamsAndSocket( clientSocket );
                    stdin.close();
                    System.exit(0);
                }
            }while (true);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
