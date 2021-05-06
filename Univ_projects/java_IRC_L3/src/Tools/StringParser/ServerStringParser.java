package Tools.StringParser;

/**
 * Class that is used to parse messages coming
 * from a client when we are in a server
 */
public class ServerStringParser {
    public static String parseLogInMessage(String text){
        if(text.length() < 6) {return null;}
        String  loginPrefix;
        String username;
        loginPrefix = text.substring(0,6);
        if( ! loginPrefix.equals("LOGIN ") ){ return null;}
        username = text.substring(6);
        if( username.indexOf(' ') != -1 ) { return null; } // no space allowed in username
        return username;
    }
    public static String parseChatMessage(String text){
        if(text.length() < 8) {return null;}
        String  messagePrefix;
        String message;
        messagePrefix = text.substring(0,8);
        if( ! messagePrefix.equals("MESSAGE ") ){ return null;}
        message = text.substring(8);
        return message;
    }
    public static boolean isThisServerConnectMessage(String text){
        return (text.compareTo("SERVERCONNECT")==0);
    }
}
