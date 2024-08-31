package Custom_CollectionsED.list.doublyLinkedSentinel;

import Custom_CollectionsED.list.ListADT;
import Custom_CollectionsED.nodes.DoubleNode;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is a representation of a List, which uses a doubly linked list with sentinel nodes as a support.
 * Implements the ListADT.
 * @param <T> The data type to be stored in the structure, must be comparable
 */
public abstract class DoublyLinkedSentinelList<T> implements ListADT<T> {

    /** Reference to the head of the list, empty, only serves as a pointer to the first element*/
    protected DoubleNode<T> head;

    /** Reference to the tail of the list, empty, only serves as a pointer to the last element*/
    protected DoubleNode<T> tail;

    /** Keeps track of the amount of elements in the list*/
    protected int count;

    /** keeps count of the modifications being made to the list*/
    protected int modCount;

    /**
     * Default constructor, which returns an empty list, with only the head and tail initialized as null
     */
    public DoublyLinkedSentinelList() {
        head = new DoubleNode<>(null);
        tail = new DoubleNode<>(null);
        count = 0;
        modCount = 0;
    }

    /**
     * <ul>The removeFirst method:
     * <li> Checks if the list is empty, if so, prints a message, and returns null</li>
     * <li> Stores the first node in a variable, so that it can then be returned</li>
     * <li> Updates the references for the nodes, so that the previous first node is skipped over</li>
     * <li> Returns the value of the deleted node</li>
     * </ul>
     * @return the value stored withing the deleted node
     */
    @Override
    public T removeFirst() {
        if (count == 0) {
            System.out.println("Empty list!");
            return null;
        }

        DoubleNode<T> first = head.getNext();

        if (first == null) {
            System.out.println("The first node is null.");
            return null;
        }

        head.setNext(first.getNext());

        DoubleNode<T> newFirst = head.getNext();
        newFirst.setPrevious(head);

        count--;
        modCount++;

        return first.getElement();
    }


    /**
     * <ul>The removeLast method:
     * <li> Checks if the list is empty, if so, prints a message, and returns null</li>
     * <li> Stores the last node in a variable, so that it can then be returned</li>
     * <li> Updates the references for the nodes, so that the previous last node is skipped over</li>
     * <li> Returns the value of the deleted node</li>
     * </ul>
     * @return the value stored withing the deleted node
     */
    @Override
    public T removeLast() {

        if (count == 0) {
            System.out.println("The list is empty :(");
        }

        DoubleNode<T> last = tail.getPrevious();  // last is the element before tail

        tail.setPrevious(last.getPrevious()); // set previous for tail as the element before last

        DoubleNode<T> newLast = tail.getPrevious(); // new last is the element that is now linked to tail
        newLast.setNext(tail); // we set the newlast's nextElement as the tail so the element is totally removed

        count--;
        modCount++;

        return (T) last;
    }


    /**
     * Removes a given element from the list, returning its value.
     * @param element to be sought for and removed
     * @return the value stored in the now removed element
     * @throws NoSuchElementException if the element is not found in the list
     */
    @Override
    public T remove(T element) {

        if (count == 0) {
        }

        DoubleNode<T> previous = head;
        DoubleNode<T> current = head.getNext();

        while (current != tail) { //while current not tail, iterate through list
            if (((Comparable<T>)current.getElement()).compareTo(element) == 0) { //does current.getnext = element?
                T toberemoved = current.getElement();
                previous.setNext(current.getNext()); //if so,
                current.getNext().setPrevious(previous);
                if (current == tail) {
                    tail = previous;
                }
                count--;
                modCount++;
                return toberemoved;
            }
            previous = current;
            current = current.getNext();
        }
        throw new NoSuchElementException("Element not found in the list: " + element);
    }

    /**
     * @return the value stored in the first node
     */
    @Override
    public T first() {
        return head.getNext().getElement();
    }

    /**
     * @return the value stored in the last node
     */
    @Override
    public T last() {
        return tail.getPrevious().getElement();
    }

    /**
     * Looks for a given element within the list
     * @param element to be sought after
     * @return true if the element exists, false otherwise
     */
    @Override
    public boolean contains(T element) {

        DoubleNode<T> current = head.getNext();
        while (current.getNext() != element) {
            if (current.getElement().equals(element)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * @return true if the counter is zero, in other words, if the list is empty
     */
    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    /**
     * @return the calue stored in the counter, in other words, the number of elements
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * @return an iterator to be used for this list
     */
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * @return a string representation of the value in each node
     */
    @Override
    public String toString() {

        StringBuilder result = new StringBuilder("DoublyLinkedList: ");

        DoubleNode<T> current = head.getNext();

        while (current != tail) {
            T element = current.getElement();
            if (element != null) {
                result.append(element);
                result.append(" ");
            }
            current = current.getNext();
        }

        return result.toString();
    }

    /**
     * An inner class, detailing the implementation of a custom iterator fit for this list
     */
    protected class MyIterator implements Iterator<T> {


        /** Expected amount of modifications at the time of access; Guarantees
         * the list won't be altered at the time of iteration */
        protected int expectedModCount;

        /** keeps track of the current node */
        protected DoubleNode<T> current;


        /**
         * Checks whether the expected modification count matches the true one
         * @throws  ConcurrentModificationException if not
         */
        private void checkConcurrentMods() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("concurrent mod!");
            }
        }

        /**
         * Constructor for the iterator , which returns a reference to an object of this type.
         */
        public MyIterator() {
            this.expectedModCount = modCount;
            this.current = head.getNext();
        }

        /**
         * @return whether there is a succeeding node (true if so, false otherwise)
         */
        @Override
        public boolean hasNext() {
            checkConcurrentMods();
            return current != null; //checks if it isn't the tail
        }

        /**
         * Advances onto the next succeeding node, if it exists
         * @return the value within the current node
         */
        @Override
        public T next() {
            checkConcurrentMods();

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T data = current.getElement();

            current = current.getNext();

            return data;
        }
    }
}


