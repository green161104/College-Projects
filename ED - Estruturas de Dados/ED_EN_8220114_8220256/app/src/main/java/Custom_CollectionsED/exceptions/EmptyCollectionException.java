package Custom_CollectionsED.exceptions;

/**
 * This exception is to be thrown when a collection is empty at the time of access
 */
public class EmptyCollectionException extends Exception {
    public EmptyCollectionException(String msg){
        super(msg);
    }
}