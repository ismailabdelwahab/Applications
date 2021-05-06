package Tools.StringParser;


import Tools.ServerData.ServerInfo;

/**
 * Class used by clients to parse strings
 */
public class ClientStringParser {
    public static ServerInfo parseConnectMessage(String text){
        if( text.length() < 13 ) return null;
        String  serverConnectPrefix;
        String ip;
        int port;
            //Check command prefix:
        serverConnectPrefix = text.substring(0,14);
        if( ! serverConnectPrefix.equals("SERVERCONNECT ") ){ return null;}
        text = text.substring(14);
            // Extract ip:
        int ip_length = text.indexOf(' ');
        ip = text.substring(0,ip_length);
            // Extract port:
        String port_as_string = text.substring(ip_length+1);
        port = Integer.parseInt( port_as_string );

        return new ServerInfo(ip, port);
    }
}
