package Clients.SimpleClient;

import Tools.SocketManagement.SocketEnding;
import Tools.StringParser.ArgumentsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SimpleClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/////////////// Constructor ////////////////////////////////////////////////////////////
    public SimpleClient(String host, int port) throws IOException {
        initTheSocket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
/////////////// Check args AND init connection //////////////////////////////////////////
    private static void showProperUsage(){
        System.out.println("Proper usage: ./SimpleClient <host> <port>");
    }
    private static void checkArguments(String[] args){
        if( args.length != 2 ) {
            showProperUsage();
            System.exit(1);
        }
    }
    private void initTheSocket(String host, int port) throws IOException {
        try {
            System.out.println("Trying to connect to: " + host + ":" + port);
            this.socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.err.println("ERROR : Unknown host: "+ host);
            e.printStackTrace();
            System.exit(3);
        }catch ( ConnectException e ){
            System.err.println("ERRROR: Connection refused. [Server may not be up]");
            System.exit(4);
        }
    }
//////////////////////// MAIN /////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        checkArguments( args );
        String host = args[0];
        int port = ArgumentsParser.getPort(args, 1);
        Executor executor = Executors.newFixedThreadPool( 2 );
        // Creation of this client:
        SimpleClient client = new SimpleClient(host , port);
        // Creation of a thread to handle write operation:
        Runnable writeThread = new WriteHandler(client.socket, client.out, client.stdin );
        executor.execute( writeThread );
        // Main thread will now handle the received messages:
        String buffer;
        try {
            do {
                buffer = client.in.readLine();
                if( buffer != null )
                    System.out.println(buffer);
                else{
                    System.err.println("Connexion was closed by the server.");
                    if( client.socket != null )
                        SocketEnding.closeStreamsAndSocket( client.socket );
                    System.exit(0);
                }
            }while( true );
        } catch (IOException e) {
            System.err.println("ERROR: IOException raised for socket.");
            e.printStackTrace();
        }
    }
////////// Handle all written input /////////////////////////////////////////////////////////////////////
    static class WriteHandler implements Runnable{
        private final Socket clientSocket;
        private final PrintWriter out;
        private final BufferedReader stdin;
        WriteHandler(Socket clientSocket, PrintWriter out, BufferedReader stdin) {
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
                    if ( buffer != null )
                        out.println( buffer );
                    else{
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
}
