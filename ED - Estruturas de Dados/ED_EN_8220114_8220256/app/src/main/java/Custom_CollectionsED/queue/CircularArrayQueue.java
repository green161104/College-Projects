package Custom_CollectionsED.queue;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.queue.QueueADT;

/**
 * This class is an implementation of a Queue which uses an array as a support, specifically,
 * a circular array.
 * It implements the QueueADT interface.
 * @param <T> the data type to be used in the structure, must be comparable
 */
public class CircularArrayQueue<T> implements QueueADT<T> {

    /**
     *  default capacity for the initial array
     */
    private int DEFAULT_CAPACITY = 20;
    /**
     * saves the index of the first element.
     */
    private int front;
    /**
     * saves the index of the last element.
     */
    private int rear;
    /**
     * saves the amount of elements in the array
     */
    private int size;
    /**
     * the array variable representing the queue
     */
    private T[] circularQueue;

    /**
     * construct for the circularArrayQueue class.
     * initiates an array with 20 capacity for a given T data-type.
     * Initiates front, rear and size all at 0.
     */
    public CircularArrayQueue() {
        this.front = 0;
        this.rear = 0;
        this.size = 0;
        circularQueue = (T[]) new Object[DEFAULT_CAPACITY];
    }

    /**
     * constructor for a custom size.
     * @param length desired size of the array.
     */
    public CircularArrayQueue(int length) {
        this.front = 0;
        this.rear = 0;
        this.size = 0;
        circularQueue = (T[]) new Object[length];
    }


    /**
     * <ul>takes in an element and :
     * <li> checks if the array length needs to be increased;</li>
     * <li> if there's no elements in the queue, adds the element as the front and read of the queue;</li>
     * <li> if there are elements, adds the element at the rear of the queue;</li>
     * <li> updates the rear of the queue to = (rear + 1) % circularQueue.length;</li>
     * <li> updates the size variable;</li>
     * </ul>
     *
     * @param element the element to be added to the rear of this queue
     */

    @Override
    public void enqueue(T element) {
        if (size == circularQueue.length) {
            arrayExpansion();
        }
        if (size == 0) {
            circularQueue[front] = circularQueue[rear] = element;
        }
        circularQueue[rear] = element;
        rear = (rear + 1) % circularQueue.length;
        size++;
    }

    /**
     * <ul> the dequeue method :
     * <li> checks the size of the queue, if 0 throws an EmptyCollectionException </li>
     * <li> saves the first element in the list, and updates the first used used index to null</li>
     * <li>returns the front of the list and updates the size variable.</li>
     * </ul>
     * @throws EmptyCollectionException if the list is empty
     * @return the first element in the queue
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (size == 0) {
            throw new EmptyCollectionException("No elements? :(");
        }

        T result = circularQueue[front];
        circularQueue[front] = null; // Remove the reference to the element

        front = (front + 1) % circularQueue.length; // Move front in a circular manner
        size--;

        return result;
    }

    /**
     * retrieves the first element of the queue
     * @return the first element.
     * @throws EmptyCollectionException if size == 0.
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (size == 0) {
            throw new EmptyCollectionException("No elements? :(");
        }
        return circularQueue[front];
    }

    /**
     * Indicates whether the queue is empty, according to the size.
     *
     * @return true if the size of the queue is 0, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return an int value representing the amount of elements in the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * resizes the array by duplicating its capacity and copies the old array into the new array.
     */
    private void arrayExpansion() {
        T[] newQueue = (T[]) (new Object[circularQueue.length * 2]);

        System.arraycopy(circularQueue, 0, newQueue, 0, size);

        this.circularQueue = newQueue;
    }

    /**
     *
     * @return a string representation of every element in the queue.
     */
    @Override
    public String toString() {
        String sb = "Current queue:\n";
        for (int i = 0; i < size; i++) {
            int index = (front + i) % circularQueue.length;
            sb += circularQueue[index] + " \n";
        }
        return sb;
    }

}
