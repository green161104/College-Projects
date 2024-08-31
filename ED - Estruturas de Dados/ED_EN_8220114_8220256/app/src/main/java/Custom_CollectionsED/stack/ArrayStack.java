package Custom_CollectionsED.stack;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.stack.StackADT;

public class ArrayStack<T> implements StackADT<T> {

    /**
     * default capacity for stack array.
     */
    private final int DEFAULT_CAPACITY = 100;

    /**
     * saves the index of the top of the stack
     */
    private int top;

    /**
     * saves the stack inside the array
     */
    private T[] stack;

    /**
     * Constructor for the stack class, initiates the array at default capacity,
     * and top at index 0.
     */
    public ArrayStack() {
        top = 0;
        stack = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Custom constructor for the stack class, initiates the array at a given capacity,
     * and top at index 0.
     */
    public ArrayStack(int initialCapacity) {
        top = 0;
        stack = (T[]) (new Object[initialCapacity]);
    }

    /**
     * the expandCapacity is a class private method, it's used by other methods
     * to ensure the stack grows as soon as the max capacity for the stack array has been reached.
     */
    private void expandCapacity() {

        T[] newStack = (T[]) (new Object[stack.length * 2]);

        System.arraycopy(stack, 0, newStack, 0, top);

        this.stack = newStack;
    }

    /**
     * <ul>the push method :
     *  <li> checks if the stack array needs resizing; </li>
     *  <li> makes it so the stack array at index top (last index) becomes the element;</li>
     *  <li> updates the top variable to now be in the new index of the array </li>
     *  </ul>
     *
     * @param element element to be pushed into the stack.
     */
    @Override
    public void push(T element) {
        if (size() == stack.length) {
            expandCapacity();
        }
        stack[top] = element;
        top++;
    }

    /**
     * <ul>the pop method :
     *  <li> Checks whether the stack is empty </li>
     *  <li> Sets aside the top value so that it can be returned</li>
     *  <li> Sets the top of the stack as null, and decreases the counter </li>
     *  <li> Returns the result, the element remove </li>
     *  </ul>
     *
     * @throws EmptyCollectionException in case the stack is empty at the time of access
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty stack :(\n");
        }
        T result = peek();
        top--;
        stack[top] = null;
        return result;
    }

    /**
     *
     * @return the top element present in the stack
     * @throws EmptyCollectionException if the collection is empty at the time of access
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty stack :(\n");
        }

        return stack[top - 1];
    }

    /**
     *
     * @return true if there are no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    /**
     *
     * @return the amount of elements in the stack
     */
    @Override
    public int size() {
        return top;
    }

    /**
     *
     * @return a string representation of each element in the stack
     */
    @Override
    public String toString() {
        StringBuilder stackString = new StringBuilder("Stack: ");
        for (int i = 0; i < size(); i++) {
            stackString.append(stack[i].toString()).append(" ");
        }
        return stackString.toString();
    }

}
