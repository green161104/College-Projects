package captureTheFlagGame.Exceptions;

/**
 * This Exception occurs when there is an attempt to access an index that isn't available
 */
public class NoSuchIndexException extends Exception{

    public NoSuchIndexException(String message){
        super(message);
    }
}
