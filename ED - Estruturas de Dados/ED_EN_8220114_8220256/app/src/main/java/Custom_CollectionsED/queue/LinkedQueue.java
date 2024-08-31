package Custom_CollectionsED.queue;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.nodes.LinearNode;
import Custom_CollectionsED.queue.QueueADT;

/**
 * This class is an implementation of a queue, which uses a singly linked list as support.
 * It implements the QueueADT interface.
 * @param <T> The data type to be stored in the collection
 */
public class LinkedQueue<T> implements QueueADT<T> {

    /**
     *  saves the front element of the queue
     */
    private LinearNode<T> front;
    /**
     * saves the last element of the queue
     */
    private LinearNode<T> rear;
    /**
     * saves an int value representing the amount of elements in the queue
     */
    private int size;

    /**
     * constructor for LinkedQueue objects, initializes fron and rear as null and size at 0.
     */
    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * <ul> the enqueue method :
     * <li> takes in the element to add; </li>
     * <li> if the queue has size 0, the element becomes the front and rear, updates size;</li>
     * <li> otherwise, the rear has the element set as it's next, and then the rear becomes the newNode.</li>
     * <li> updates size.</li>
     * </ul>
     * @param element the element to be added to the rear of this queue
     */
    @Override
    public void enqueue(T element) {

        LinearNode<T> newNode = new LinearNode<T>(element);
        if (size == 0) {
            this.front = newNode;
            this.rear = newNode;
            size++;
        } else {
            this.rear.setNext(newNode);
            this.rear = newNode;
            size++;
        }
    }
    /**
     * <ul> the dequeue method :
     * <li> checks the size of the queue, if 0, throws exception; </li>
     * <li> saves the front in a LinearNode variable </li>
     * <li> sets the front to the element previous ahead of the front</li>
     * <li> checks if the front is null ( queue only had 1 element ) and sets rear to null if so;</li>
     * <li> updates size variable and returns the element</li>
     * </ul>
     * @return element that has been dequeued
     * @throws EmptyCollectionException if the queue is empty
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Queue is empty.");
        }
        LinearNode<T> temp = this.front;
        this.front = this.front.getNext();
        if (this.front == null) {
            this.rear = null;
        }
        size--;
        return temp.getElement();
    }

    /**
     * the first element, checks if the list is empty, and if it's not, returns the front element
     * without dequeueing it.
     * @return the front element
     * @throws EmptyCollectionException if the list is empty.
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Queue is empty.");
        }
        return front.getElement();
    }

    /**
     *
     * @return true if size 0, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *
     * @return int value representing the amount of elements in the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     *
     * @return a string representation of every element in the queue
     */
    @Override
    public String toString() {
        LinearNode<T> current = front; // start from the first data node (after head cuz sentinel)
        String linkedString = "Queue :\n";
        while (current != null) {
            linkedString += current.getElement().toString() + "\n";
            current = current.getNext();
        }
        return linkedString;
    }

}

