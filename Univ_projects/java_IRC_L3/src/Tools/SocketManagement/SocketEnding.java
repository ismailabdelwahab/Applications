package Tools.SocketManagement;

import java.io.IOException;
import java.net.Socket;

/**
 * Class used when a socket is trying to close
 */
public class SocketEnding {
    public static void closeStreamsAndSocket(Socket socket) throws IOException {
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }
}
