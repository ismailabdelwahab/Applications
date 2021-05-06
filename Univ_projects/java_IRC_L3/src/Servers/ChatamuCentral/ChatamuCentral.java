package Servers.ChatamuCentral;

import Servers.ChatamuCentral.ClientThreads.ReadFromClientThread;
import Tools.StringParser.ArgumentsParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatamuCentral {
    private static final int DEFAULT_PORT = 12345;
    private final int SERVER_PORT;

    private static HashMap<String, ArrayBlockingQueue<String>> hashmapQueues;
    private ServerSocket serverSocket;
/////////// Constructors ////////////////////////////////////////////////
    public ChatamuCentral( int port ) {
        this.SERVER_PORT = port;
        hashmapQueues = new HashMap<>();
    }
    public ChatamuCentral() { this( DEFAULT_PORT ); }
/////////// Proper usage /////////////////////////////////////////////////
    private static void showProperUsage(){
        System.out.println("Proper usage: java SalonCentral [port]");
        System.out.println("Default used port will be: ["+ DEFAULT_PORT +"] if no one is given.");
    }
    private static void checkArguments(String[] args){
        if( args.length == 0 ){ return;}
        if( args.length != 1 ){ showProperUsage(); System.exit(1);}
        if( args[0].equals("-h") ){ showProperUsage(); System.exit(0);}
    }
/////////// Start the server  ////////////////////////////////////////////
    private void start_server(Executor executor){
        System.out.println(">Launching server on port: " + SERVER_PORT);
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            serverSocket.setReuseAddress(true);
            while (true)
                executor.execute(new ReadFromClientThread(hashmapQueues, serverSocket.accept()));
        } catch (IOException ex) {
            System.err.println(">Error: Server didn't stopped correctly.\n" +  ex );
            System.exit( 100 );
        }
    }
///////////////// Main ////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        ChatamuCentral chatamuCentral;
        Executor executor;
        /* Verify arguments validity */
        checkArguments( args );
        /* Launch server */
        int port = ArgumentsParser.getPort(args, 0, DEFAULT_PORT );
        chatamuCentral = new ChatamuCentral( port );
        executor = Executors.newCachedThreadPool();
        chatamuCentral.start_server( executor );
    }
}
