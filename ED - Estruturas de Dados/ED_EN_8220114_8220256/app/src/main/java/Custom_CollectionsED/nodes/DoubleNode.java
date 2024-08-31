package Custom_CollectionsED.nodes;

/**
 * This classes serves as a representation of a double node, to be used when handling doubly
 * linked lists. Each node contains references to the preceding, and succeeding nodes.
 * @param <T> The data type to be stored in the nodes.
 */
public class DoubleNode<T> {
    /**
     * references to the next node, as well as the previous.
     */
    private DoubleNode<T> next, previous;

    /**
     * Reference to the value stored within the node
     */
    private T element;

    /**
     * Constructor which returns an empty node
     */
    public DoubleNode()
    {
        next = null;
        element = null;
        previous = null;
    }

    /**
     * Create a node, which holds a reference to the value passed to the constructor as a parameter
     * @param elem value to be stored in the node
     */
    public DoubleNode (T elem)
    {
        next = null;
        element = elem;
        previous = null;
    }

    /**
     * @return a reference to the next node
     */
    public DoubleNode<T> getNext()
    {
        return next;
    }

    /**
     * @return a reference to the previous node
     */
    public DoubleNode<T> getPrevious() {
        return previous;
    }

    /**
     * Sets the node passed as a parameter to be the previous node
     * @param node to be set as the previous
     */
    public void setPrevious (DoubleNode<T> node) {
        previous = node;
    }

    /**
     * Sets the node passed as a parameter to be the next node
     * @param next
     */
    public void setNext(DoubleNode<T> next) {
        this.next = next;
    }

    /**
     * @return Returns a reference to the value stored within the node
     */
    public T getElement() {
        return element;
    }

    /**
     * Sets the value stored within the node to whichever value was passed as a parameter
     * @param elem value to be stored in the node
     */
    public void setElement (T elem){
        element = elem;
    }
}
