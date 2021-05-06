package Tools.Errors;

/**
 * Used to generalize errors in the project
 */
public class ExceptionError {
    public static void ThrowExitError(String error_comment, int sys_exit_code){
        System.err.println( error_comment );
        System.exit( sys_exit_code );
    }
    public static void ThrowExitError(String error_comment,Exception e, int sys_exit_code){
        System.err.println( error_comment );
        System.err.println( e.toString() );
        System.exit( sys_exit_code );
    }
}
