package Clients.NormalClient;

import Tools.Errors.ExceptionError;
import Tools.ServerData.ServerInfo;
import Tools.SocketManagement.SocketEnding;
import Tools.StringParser.ClientStringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NormalClient {
    private ServerInfo serverInfo;
    private Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
/////////////// Constructor ////////////////////////////////////////////////////////////
    public NormalClient() throws IOException {
        askServerConnect();
        initSocket();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
/////////////// Initialize client /////////////////////////////////////
    /**
     * Ask the client to connect with command SERVERCONNECT
     * And store (ip,port) in "this.serverInfo"
     */
    private void askServerConnect() throws IOException {
        while( this.serverInfo == null) {
            System.out.println("Please connect to a server:");
            System.out.println("SERVERCONNECT <server_ip> <server_port>");
            String connectCommand = stdin.readLine();
            serverInfo = ClientStringParser.parseConnectMessage(connectCommand);
        }
    }
    /**
     * Start the connection to (ip,port) in "this.serverInfo"
     */
    private void initSocket() throws IOException {
        String host = serverInfo.getIp();
        int port = serverInfo.getPort();
        System.out.println("Trying to connect to: " + host + ":" + port);
        try { this.socket = new Socket(host, port); }
        catch (UnknownHostException e) {
            ExceptionError.ThrowExitError(("ERROR : Unknown host: "+ host), 3);
        }catch ( ConnectException e ){
            ExceptionError.ThrowExitError(("ERRROR: Connection refused. [Server may not be up]"), 4);
        }
    }
//////////////////////// MAIN /////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        NormalClient client = new NormalClient();

        // Creation of a thread to handle write operation:
        Runnable writeThread = new WriteHandler(client.socket, client.out, client.stdin );
        new Thread( writeThread ).start();

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
}
