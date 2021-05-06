package Tools.SocketManagement;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SocketChannelParser {

    public static String getChannelIdentity(SocketChannel sc) throws IOException {
        String host_adr = sc.socket().getInetAddress().getHostName();
        int port = sc.socket().getPort();
        return "["+host_adr+":"+port+"]";
    }
}
