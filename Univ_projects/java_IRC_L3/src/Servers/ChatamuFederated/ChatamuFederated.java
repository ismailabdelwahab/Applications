package Servers.ChatamuFederated;

import Servers.ChatamuFederated.ClientsThreads.ClientReadingThread;
import Servers.ChatamuFederated.ClientsThreads.ClientWritingThread;
import Servers.ChatamuFederated.ServerThreads.ReadFromServerThread;
import Servers.ChatamuFederated.ServerThreads.WriteToServerThread;
import Tools.Errors.ExceptionError;
import Tools.FileParser.FileParser;
import Tools.ServerData.ServerInfo;
import Tools.SocketManagement.SocketParser;
import Tools.StringParser.ArgumentsParser;
import Tools.StringParser.ServerStringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatamuFederated {
    private static final int DEFAULT_PORT = 12345;
    private static final int DEFAULT_QUEUE_CAPACITY = 100;
    private static final int MAX_USERNAME_LENGTH = 20;
    private final Executor executor = Executors.newCachedThreadPool();

    private ServerSocket serverSocket;
    private final int SERVER_PORT;

    private HashMap<String, ArrayBlockingQueue<String>> clientsQueues;
    private HashMap<String, ArrayBlockingQueue<String>> serversQueues;
    private ArrayList<ServerInfo> remoteServersInfo;
/////////// Constructor ////////////////////////////////////////////////
    private ChatamuFederated(int port) {
        this.SERVER_PORT = port;
        this.clientsQueues = new HashMap<>();
        this.serversQueues = new HashMap<>();
    }
/////////// Proper usage + treat arguments ////////////////////////
    private static void showProperUsage( int sys_exit_code ){
        System.out.println("Proper usage: java ChatamuFederated [port]");
        System.out.println("Default used port will be: ["+ DEFAULT_PORT +"] if no one is given.");
        System.exit( sys_exit_code );
    }
    private static void checkArguments(String[] args){
        if( args.length == 0 ){ return;}
        if( args.length != 1 ){ showProperUsage( 1 ); }
        if( args[0].equals("-h") ){ showProperUsage( 0 );}
    }
///////////// MAIN //////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        checkArguments( args );

        int server_port = ArgumentsParser.getPort( args, 0, DEFAULT_PORT);
        ChatamuFederated chatamuFederated = new ChatamuFederated( server_port );
        chatamuFederated.launch_server();
    }
///////////// Launch the server  ////////////////////////////////////////
    private void launch_server(){
        System.out.println(">Launching server on port: " + SERVER_PORT);
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            serverSocket.setReuseAddress(true);
            collectRemoteServersAdr();
            connectThisServerToNetWork();
            while( true ) {
                acceptNewConnections();
                // We keep reconnecting this server to the network to be sure that
                // all connection are still up
                connectThisServerToNetWork();
            }
        } catch (IOException ex) { ExceptionError.ThrowExitError(">Error: Server didn't stopped correctly.", ex ,100); }
    }
    private void acceptNewConnections() {
        try {
            Socket socket = serverSocket.accept();
            String socketIdentity = SocketParser.getSocketIdentity(socket);
            /* ********** "LOG IN"/"SERVERCONNECT" PART ******************** */
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String buffer = in.readLine();
            if(  ServerStringParser.isThisServerConnectMessage(buffer) )
                launchWriteToServerThread( socket, socketIdentity );
            else { // If this connection was not from a server:
                String username = ServerStringParser.parseLogInMessage(buffer);
                if (username == null)
                    out.println(">ERROR: [LOGIN] aborting chatamu protocol");
                else if (username.length() > MAX_USERNAME_LENGTH)
                    out.println(">ERROR: [USERNAME LENGTH] aborting chatamu protocol");
                else  // New user is accepted on the chat
                    launchClientsThreads(socket, socketIdentity, username);
            }
        } catch (IOException e) { System.err.println("Error while accepting new connection: (Main thread)"); e.printStackTrace(); }
    }
////////////////////// Connect this server to the network /////////////////////////////////////////////////

    /**
     * Parse a cfg file depeding of if this server is master or not.
     */
    private void collectRemoteServersAdr(){
        //this.remoteServersInfo = this.SERVER_PORT == 12345 ?
        //        FileParser.CfgParsePeers("peers/master.cfg") :
        //        FileParser.CfgParsePeers("peers/peers.cfg")  ;
        this.remoteServersInfo = FileParser.CfgParsePeers("peers/peers.cfg");
    }
    private void connectThisServerToNetWork() {
        ArrayList<ServerInfo> successfulConnections = new ArrayList<>();
        ServerInfo thisServerInfo = ServerInfo.getSocketServerInfo( this.serverSocket );
        for(ServerInfo serverInfo : this.remoteServersInfo) {
            if( serverInfo.equals(thisServerInfo) ){ continue; }
            try {
                Socket socketToRemoteServer = new Socket( serverInfo.getIp(), serverInfo.getPort() );
                launchReadFromServerThread( socketToRemoteServer );
                successfulConnections.add( serverInfo );
            }
            catch (IOException ignored) {
                //If connection fails, we ignore it. This connection will be done we remote server is opened.
            }
        }
        //Update the remote server's list: remove the serverInfo that had a successful connection already.
        this.remoteServersInfo.removeAll( successfulConnections );
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void launchClientsThreads( Socket socket, String socketIdentity, String username ) throws IOException {
        clientsQueues.put( socketIdentity, new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY) );
        executor.execute( new ClientWritingThread( clientsQueues.get(socketIdentity), socket ) );
        executor.execute( new ClientReadingThread(clientsQueues, serversQueues, socket, socketIdentity, username) );
    }
    private void launchWriteToServerThread(Socket socket, String socketIdentity ) throws IOException {
        serversQueues.put( socketIdentity, new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY) );
        executor.execute( new WriteToServerThread( serversQueues.get(socketIdentity), socket ) );
    }
    private void launchReadFromServerThread(Socket socketToRemoteServer) throws IOException {
        String socketIdentity = SocketParser.getSocketIdentity( socketToRemoteServer);
        PrintWriter out = new PrintWriter(socketToRemoteServer.getOutputStream(), true);
        out.println("SERVERCONNECT");
        executor.execute( new ReadFromServerThread(clientsQueues, socketToRemoteServer, socketIdentity) );
    }
}
