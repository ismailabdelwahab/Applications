package Tools.FileParser;

import Tools.Errors.ExceptionError;
import Tools.ServerData.ServerInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileParser {
    private static String RESSOURCE_DIR = "../../Ressources/";

    /**
     * @param cfgFilepath config file
     * @return an Arraylist of ServerInfo representing all servers on the network.
     */
    public static ArrayList<ServerInfo> CfgParsePeers(String cfgFilepath ) {
        ArrayList<ServerInfo> serversInfos = new ArrayList<>();

        File cfgFile = new File( RESSOURCE_DIR+cfgFilepath );
        Scanner sc = null;
        try {
            sc = new Scanner( cfgFile );
            while ( sc.hasNextLine() ) {
                String cfgLine = sc.nextLine();
                cfgLine = cfgLine.substring( cfgLine.indexOf('=') + 2 );
                // Get ip:
                int index_of_space = cfgLine.indexOf(' ');
                String host = cfgLine.substring(0, index_of_space );
                // Get port:
                cfgLine = cfgLine.substring( index_of_space+1 );
                String port = cfgLine;
                //Add them to the array list:
                ServerInfo serverInfo = new ServerInfo(host, Integer.parseInt(port) );
                serversInfos.add( serverInfo );
            }
            sc.close();
        } catch (FileNotFoundException e) {
            ExceptionError.ThrowExitError("Error: File["+cfgFilepath+"] not found.", 10);
        }
        return serversInfos;
    }
}
