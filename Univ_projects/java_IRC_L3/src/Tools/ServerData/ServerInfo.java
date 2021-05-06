package Tools.ServerData;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that represent the information of a server
 */
public class ServerInfo {
    private final String ip;
    private final int port;

/////////// GETTERS /////////////////////////////
    public ServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
/////////// GETTERS /////////////////////////////
    public String getIp() { return ip; }
    public int getPort() { return port; }

    public static ServerInfo getRemoteServerInfo( Socket socket){
        return new ServerInfo(socket.getInetAddress().getHostName(), socket.getPort());
    }
    public static ServerInfo getSocketServerInfo( ServerSocket socket){
        return new ServerInfo(socket.getInetAddress().getHostAddress(), socket.getLocalPort());
    }


    @Override
    public boolean equals(Object other){
        other = other instanceof ServerInfo ? ((ServerInfo) other) : null;
        if ( other == null ) return false;
        return this.ip.equals(((ServerInfo) other).ip) && this.port== ((ServerInfo) other).port;
    }

    @Override
    public String toString(){ return "["+this.ip+":"+this.port+"]"; }
}
