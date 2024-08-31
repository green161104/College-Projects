package Custom_CollectionsED.exceptions;


/**
 * This exception is to be thrown when an element is not found at the time of access
 */
public class ElementNotFoundException extends Exception {
    public ElementNotFoundException(String msg) {
        super(msg);
    }
}
