package Tools.StringParser;

import Tools.Errors.ExceptionError;

public class ArgumentsParser {
    /**
     * @param args Main arguments
     * @param index_in_args port index in "args"
     * @return The port in argument as an int.
     */
    public static int getPort( String[] args, int index_in_args){
        int port = -1;
        try{
            port = Integer.parseInt( args[index_in_args] );
        }
        catch (NumberFormatException e){
            ExceptionError.ThrowExitError("Port must be a number in range: [0 - 65535]", 2 );
        }
        if( port < 0 || port > 65535 )
            ExceptionError.ThrowExitError("Given port: ("+ port +") is out of range: [0 - 65535]", 3);
        return port;
    }

    /**
     *
     * @param args Main arguments
     * @param index_in_args port index in "args"
     * @param DEFAULT_PORT Default value
     * @return The port in argument if it exists. Otherwise return DEFAULT_PORT
     */
    public static int getPort( String[] args, int index_in_args, int DEFAULT_PORT) {
        if (args.length == 0) { return DEFAULT_PORT; }
        return getPort( args, index_in_args);
    }
}
