package Tools.SocketManagement;

import java.net.Socket;

public class SocketParser {

    public static String getSocketIdentity(Socket socket){
        return "["+socket.getInetAddress()+":"+socket.getPort()+"]";
    }
}
