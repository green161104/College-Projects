package Custom_CollectionsED.stack;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.nodes.LinearNode;
import Custom_CollectionsED.stack.StackADT;

/**
 * This class is an implementation is the stack data structure, which uses a singly linked list
 * as a support.
 * @param <T> the data type to be used in the structure
 */
public class LinkedStack<T> implements StackADT<T> {

    /**
     * the node sitting at the top of the stack
     */
    private LinearNode<T> top;

    /**
     * the number of nodes
     */
    private int count;

    /**
     * default constructor, which creates an empty stack
     */
    public LinkedStack() {
        top = null;
        count = 0;
    }

    /**
     * <ul>The push method:
     * <li>Creates a new node, with the value that was passed as a parameter</li>
     * <li>This new node sits on top of the stack, so, its next field will be set to the previous top of the stack</li>
     * <li>The top is now the new node, and the count is incremented</li>
     * </ul>
     * @param element to be added to the stack
     */
    @Override
    public void push(T element) {
        LinearNode<T> newNode = new LinearNode(element);
        newNode.setNext(top);
        top = newNode;
        count++;
    }

    /**
     * <ul>The pop method:
     * <li>Checks if the stack is empty</li>
     * <li>Saves the current top of the stack so that it can later be returned</li>
     * <li>The top is now the node which succeeds it</li>
     * <li>The result is returned, and the count is decremented</li>
     * </ul>
     *
     * @return the element removed, previous top
     * @throws EmptyCollectionException is the collection is empty at the time of access
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (count == 0) {
            throw new EmptyCollectionException("no elements ? :(");
        }
        T result = top.getElement();
        top = top.getNext();
        count--;
        return result;
    }

    /**
     * @return a reference to the value stored at the top of the stack
     * @throws EmptyCollectionException in case the collection is empty at the moment of access
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (count == 0) {
            throw new EmptyCollectionException("no elements ? :(");
        }
        return top.getElement();
    }

    /**
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     *
     * @return the amount of elements in the stack
     */
    @Override
    public int size() {
        return count;
    }

    /**
     *
     * @return a string representation of the stack
     */
    @Override
    public String toString() {
        LinearNode<T> current = top; // start from the first data node (after head cuz sentinel)
        String linkedString = "Stack :";
        while (current != null) {
            linkedString += current.getElement().toString() + " ";
            current = current.getNext();
        }
        return linkedString;
    }
}