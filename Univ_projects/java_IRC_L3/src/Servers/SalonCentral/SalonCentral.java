package Servers.SalonCentral;

import Tools.SocketManagement.SocketEnding;
import Tools.SocketManagement.SocketParser;
import Tools.StringParser.ArgumentsParser;
import Tools.StringParser.ServerStringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SalonCentral {
    private static final int DEFAULT_PORT = 12345;
    private static final int MAX_USERNAME_LENGTH = 20;
    private final int SERVER_PORT;

    private ServerSocket serverSocket;
/////////// Constructor ////////////////////////////////////////////////
    public SalonCentral( int port ) { this.SERVER_PORT = port; }
/////////// Proper usage + treat arguments ////////////////////////
    private static void showProperUsage(){
        System.out.println("Proper usage: java SalonCentral [port]");
        System.out.println("Default used port will be: ["+ DEFAULT_PORT +"] if no one is given.");
    }
    private static void checkArguments(String[] args){
        if( args.length == 0 ){ return;}
        if( args.length != 1 ){ showProperUsage(); System.exit(1);}
        if( args[0].equals("-h") ){ showProperUsage(); System.exit(0);}
    }
/////////// Start the server  ////////////////////////
    private void start_server(Executor executor){
        System.out.println("Launching server on port: " + SERVER_PORT);
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            serverSocket.setReuseAddress(true);
            while (true)
                executor.execute(new ClientHandler(serverSocket.accept()));
        } catch (IOException ex) {
            System.err.println("Error: Server didn't stopped correctly.");
            System.err.println( ex );
            System.exit( 100 );
        }
    }

    public static void main(String[] args) throws IOException {
        SalonCentral salonCentral;
        Executor executor;
        /* Verify arguments validity */
        checkArguments( args );
        /* Launch server */
        salonCentral = new SalonCentral(ArgumentsParser.getPort(args, 0, DEFAULT_PORT) );
        executor = Executors.newCachedThreadPool();
        salonCentral.start_server( executor );
    }

    private static void printErrorAndCloseSocket( Socket socket, String errorMessage ) throws IOException {
        System.err.println( errorMessage );
        SocketEnding.closeStreamsAndSocket( socket );
    }
////////// Runnable class to handle clients ////////////////////////////////////////////////
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final String socketIdentity;
        private String username;
        //private final PrintWriter out;
        private final BufferedReader in;

        private ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.socketIdentity = SocketParser.getSocketIdentity( clientSocket );
            /* "out" is not used in this version of the server */
            //out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
        private String getClientIdentity(){
            return "["+this.username+"]";
        }

        @Override
        public void run() {
            String buffer;
            String message;
            try {
                /* ***************** LOG IN PART ******************** */
                buffer = in.readLine();
                this.username = ServerStringParser.parseLogInMessage( buffer );
                if( this.username == null /* && [other condition]*/){
                    /* SEND ERROR MESSAGE TO CLIENT IN THE FUTURE VERSIONS */
                    /* Also remove client from any sort of queue where he is stored */
                    printErrorAndCloseSocket(clientSocket, "Error in {login} for client: " + this.socketIdentity );
                    return;
                }else if(this.username.length() > MAX_USERNAME_LENGTH){
                    /* Send message to signal maximum length for username */
                    printErrorAndCloseSocket( clientSocket, "User tried to use a too long username." );
                    return;
                }
                else{
                    System.out.println("New user connected: "+ this.username);
                    //Handle creation of new client (add him to some queue)
                }

                /* ***************** CHAT PART ********************** */
                do {
                    buffer = in.readLine();
                    if( buffer == null ) { break; }
                    message = ServerStringParser.parseChatMessage( buffer );
                    if ( message != null )
                        System.out.println( getClientIdentity() + ": " + message );
                    else{
                        /* SEND ERROR MESSAGE TO CLIENT HERE */
                        System.err.println("Error in {message} for client: " + this.socketIdentity);
                    }
                }while (true);
                System.out.println("Client disconnected: " + this.socketIdentity);
                SocketEnding.closeStreamsAndSocket( clientSocket );
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
