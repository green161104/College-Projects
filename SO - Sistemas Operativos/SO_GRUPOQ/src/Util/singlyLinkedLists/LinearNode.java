package Util.singlyLinkedLists;

/**
 * This class is a representation of a linear node, to be used when handling singly linked lists.
 * Each node holds a reference to the node which succeeds it.
 * @param <T> the data type to be stored in the nodes
 */
public class LinearNode<T> {

    /**
     * Reference to the succeeding node
     */
    private LinearNode<T> next;

    /**
     * value which is stored inside the node
     */
    private T element;

    /**
     * Default constructor for the node which creates an empty node, with no references
     */
    public LinearNode(){
        next = null;
        element = null;
    }

    /**
     * Custom constructor for the linear node, which stores within the node the value passed as a parameter
     * @param element value to be stored within the node
     */
    public LinearNode(T element){
        next = null;
        this.element = element;
    }

    /**
     * @return a reference to the succeeding node
     */
    public LinearNode<T> getNext(){
        return next;
    }

    /**
     * Sets the next node as a reference to the node passed as a parameter
     * @param element node to be set as the next one
     */
    public void setNext(LinearNode<T> element){
        this.next = element;
    }

    /**
     * @return a reference to the value stored in the node
     */
    public T getElement(){
        return element;
    }

    /**
     * Sets the value stored within the node to the value passed as a parameter
     * @param element value to be stored within the node
     */
    public void setElement(T element){
        this.element = element;
    }
}

