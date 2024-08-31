package Custom_CollectionsED.list.doublyLinkedLists;

import Custom_CollectionsED.list.ListADT;
import Custom_CollectionsED.nodes.DoubleNode;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is an implementation of a List data structure, with the use of doubly
 * linked lists as a support. It implements the ListADT interface
 *
 * @param <T> The data type to be stored in the list, must be comparable
 */
public abstract class DoublyLinkedList<T> implements ListADT<T> {

    /**
     * Reference to the first node in the doubly linked list.
     */
    protected DoubleNode<T> head;

    /**
     * Reference to the last node in the doubly linked list.
     */
    protected DoubleNode<T> tail;

    /**
     * Counter to keep track of the number of elements in the doubly linked list.
     */
    protected int counter;

    /**
     * Modification count to keep track of the number of modifications made to the list.
     */
    protected int modCount;

    /**
     * <ul>Constructor for the doubly linked list.
     * <li>Initializes head and tail nodes as nodes without data.</li>
     * <li>Initializes the counter and modCount variables to zero.</li>
     * </ul>
     */
    public DoublyLinkedList() {
        head = tail = new DoubleNode<T>();
        this.counter = 0;
        this.modCount = 0;
    }

    /**
     * <ul>Removes and returns the first element in the doubly linked list.
     * <li> Checks if the list is empty, returns null if so</li>
     * <li> Removes the first element and updates head and tail references</li>
     * <li> Updates links if more than one element exists in the list</li>
     * <li> Adjusts counters for size and modifications</li>
     * </ul>
     *
     * @return the removed element from the beginning of the list, or null if the list is empty
     */
    @Override
    public T removeFirst() {
        if (head == null) {
            return null; // list is empty
        }

        DoubleNode<T> nodeToRemove = head;
        head = head.getNext();

        if (head != null) {
            // More than one element in the list
            head.setPrevious(null);
        } else {
            // Only one element in the list
            tail = null;
        }

        counter--;
        modCount++;
        return nodeToRemove.getElement();
    }

    /**
     * <ul>Removes and returns the last element in the doubly linked list.
     * <li> Checks if the list is empty; returns null if so</li>
     * <li> Removes the last element and updates tail and head references</li>
     * <li> Updates links if more than one element exists in the list</li>
     * <li> Adjusts counters for size and modifications</li>
     * </ul>
     *
     * @return the removed element from the end of the list, or null if the list is empty.
     */
    @Override
    public T removeLast() {
        if (tail == null) {
            return null; // list is empty
        }

        DoubleNode<T> nodeToRemove = tail;
        tail = tail.getPrevious();

        if (tail != null) {
            // More than one element in the list
            tail.setNext(null);
        } else {
            // Only one element in the list
            head = null;
        }

        counter--;
        modCount++;
        return nodeToRemove.getElement();
    }


    /**
     * <ul>Finds and returns the node containing the specified element in the doubly linked list.
     * <li> Starts from the head and iterates through the list to find the node with the specified element;</li>
     * <li> Returns the node containing the element if found;</li>
     * </ul>
     *
     * @param element the element to find in the doubly linked list
     * @return the node containing the specified element
     * @throws NoSuchElementException if the element is not found in the list
     */
    protected DoubleNode<T> findNode(T element) {

        DoubleNode<T> currentNode = head; //start at head

        // iterate through the list to find the node with the specified element
        while (currentNode != null) {
            if (((Comparable<T>)currentNode.getElement()).compareTo(element) == 0) {
                return currentNode;
            }
            currentNode = currentNode.getNext();//next iteration
        }
        throw new NoSuchElementException();
    }

    /**
     * <ul>Removes the specified element from the doubly linked list, if present.
     * <li> Finds the node containing the specified element;</li>
     * <li> Adjusts the references of previous and next nodes to skip the node to be removed;</li>
     * <li> Updates head and tail references if necessary;</li>
     * <li> Updates counter and modCount variables;</li>
     * </ul>
     *
     * @param element the element to be removed from the list
     * @return the removed element if found; null if the element is not present in the list
     */
    @Override
    public T remove(T element) {

        DoubleNode<T> toBeRemoved = findNode(element); //start at head

        // Update the previous and next nodes to skip the node to be removed
        if (toBeRemoved.getPrevious() != null) {
            toBeRemoved.getPrevious().setNext(toBeRemoved.getNext());
        } else {
            // if it has no elements before, means  its the head
            head = toBeRemoved.getNext();
        }

        if (toBeRemoved.getNext() != null) {
            toBeRemoved.getNext().setPrevious(toBeRemoved.getPrevious());
        } else {
            //if it has no elements after, means  its the tail
            tail = toBeRemoved.getPrevious();
        }

        // Set the element of the removed node to null
        toBeRemoved.setElement(null);

        counter--;
        modCount++;
        return toBeRemoved.getElement();
    }

    /**
     * Retrieves the element at the beginning of the doubly linked list.
     *
     * @return the element at the beginning of the list
     */
    @Override
    public T first() {
        return head.getElement();
    }

    /**
     * Retrieves the element at the end of the doubly linked list.
     *
     * @return the element at the end of the list
     */
    @Override
    public T last() {
        return tail.getElement();
    }


    /**
     * Checks whether the specified element is present in the doubly linked list.
     *
     * @param element the element to check for in the list
     * @return true if the element is found in the list, false otherwise
     */
    @Override
    public boolean contains(T element) {
        DoubleNode<T> currentNode = head;
        while (currentNode.getElement() != null) {
            if (((Comparable<T>)currentNode.getElement()).compareTo(element) == 0) {
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    /**
     * Checks if the doubly linked list is empty.
     *
     * @return true if the list contains no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    /**
     * Retrieves the number of elements currently in the doubly linked list.
     *
     * @return the number of elements in the list
     */
    @Override
    public int size() {
        return counter;
    }

    /**
     * Provides an iterator for the elements in the doubly linked list.
     *
     * @return an iterator over the elements in the list
     */
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * Returns a string representation of the elements in the doubly linked list.
     * The string contains elements in the order they appear in the list, separated by spaces.
     *
     * @return a string representation of the doubly linked list elements
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("DoublyLinkedList: ");

        DoubleNode<T> current = head;
        while (current != null) {
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
     * Private inner class implementing the Iterator interface for the DoublyLinkedList.
     * Provides iteration over the elements of the list.
     */
    private class MyIterator implements Iterator<T> {
        private DoubleNode<T> current;
        private int expectedModCount;

        /**
         * Constructs an iterator for the DoublyLinkedList.
         * Initializes the iterator with the head node and the expected modification count.
         */
        public MyIterator() {
            current = head;
            expectedModCount = modCount;
        }

        /**
         * Checks if there is a next element in the iteration.
         *
         * @return true if there is a next element, false otherwise
         */
        @Override
        public boolean hasNext() {
            return current != null && current.getNext() != null;
        }

        /**
         * Retrieves the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if there are no more elements in the iteration
         */
        @Override
        public T next() {
            checkConcurrentMods();

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T element = current.getElement();
            current = current.getNext();
            return element;
        }

        private void checkConcurrentMods() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("concurrent mod!");
            }
        }
    }

}
