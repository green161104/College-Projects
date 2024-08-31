package Util.singlyLinkedLists;


import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class implementing the ListADT interface representing a singly linked list.
 *
 * @param <T> the type of elements stored in the list, must be comparable
 */
public abstract class SinglyLinkedList<T> implements ListADT<T> {
    protected LinearNode<T> head;
    protected int counter;
    protected int modCount;

    /**
     * Constructs an empty singly linked list.
     * Initializes head as null, counter as 0, and modCount as 0.
     */
    public SinglyLinkedList(){
        this.head = null;
        this.counter = 0;
        this.modCount = 0;
    }

    /**
     * <ul>the removefirst method :
     * <li> checks if the list is empty, if so returns null; </li>
     * <li> returns the element that's the current head of the list; </li>
     * <li> updates counter and modCount variables; </li>
     *</ul>
     * @return the first element in the list, or null if the list is empty
     */
    @Override
    public T removeFirst() {
        if (head == null || counter == 0) {
            return null;
        }

        T toBeRemoved = head.getElement();
        head = head.getNext();
        counter--;
        modCount++;
        return toBeRemoved;
    }

    /**
     * <ul>the removelast method :
     * <li>  checks if the list is empty, if so returns null;</li>
     * <li>  checks to see if list only has one element and returns it if so;</li>
     * <li>  if the list has more than one element, iterates through the entire list until
     * it has reached the last element and then returns it;</li>
     * <li>  updates counter and modCount variables;</li>
     *</ul>
     *
     * @return the last element in the list, or null if the list is empty
     */
    @Override
    public T removeLast() {
        if (head == null || counter == 0) {
            return null;
        }

        if (head.getNext() == null) {
            // Special case: list has only one element
            T toBeRemoved = head.getElement();
            head = null;
            counter--;
            modCount++;
            return toBeRemoved;
        }

        LinearNode<T> current = head;
        LinearNode<T> previous = null;

        while (current.getNext() != null) {
            previous = current;
            current = current.getNext();
        }

        T toBeRemoved = current.getElement();
        previous.setNext(null); // Set the next of the second-to-last node to null
        counter--;
        modCount++;
        return toBeRemoved;
    }


    /**
     * Iterates through the entire list comparing every element to the
     * target element. if it's found, it'll return the element, otherwise,
     * it'll return null and print a "element not found" message.
     *
     * @param element the element to search for in the list
     * @return the node containing the element, or null if not found
     */
    protected LinearNode<T>findNode(T element){
        LinearNode<T> current = head;
        while(current.getNext() != null){
            if(((Comparable<T>)current.getElement()).compareTo(element) == 0)
                return current;
            current = current.getNext();
        }
        System.out.println("Element not found!");
        return null;
    }

    /**
     * <ul>Removes the specified element from the singly linked list, if present.
     * <li> Finds the node containing the specified element;</li>
     * <li> Adjusts the references of previous nodes to skip the node to be removed;</li>
     * <li> Updates head reference if necessary;</li>
     * <li> Updates counter and modCount variables;</li>
     * </ul>
     *
     * @param element the element to be removed from the list
     * @return the removed element if found; null if the element is not present in the list
     */
    @Override
    public T remove(T element) {
        if (head == null || counter == 0) {
            System.out.println("List is empty!");
            return null;
        }

        LinearNode<T> toBeRemoved = findNode(element);

        if (toBeRemoved == null) {
            System.out.println("Element not found in the list!");
            return null;
        }

        LinearNode<T> previous = null;
        LinearNode<T> current = head;

        //if first element is null
        if (current == toBeRemoved) {
            head = current.getNext();
        } else {
            //find node before toBeRemoved
            while (current != null && current != toBeRemoved) {
                previous = current;
                current = current.getNext();
            }

            // update previous
            if (previous != null) {
                previous.setNext(toBeRemoved.getNext());
            }
        }
        counter--;
        modCount++;
        return toBeRemoved.getElement();
    }

    /**
     * @return the element at the head of the list
     */
    @Override
    public T first() {
        return head.getElement();
    }

    /**
     * @return the last element in the list
     */
    @Override
    public T last() {
        LinearNode<T> current = head;
        while(current.getNext() != null){
            current = current.getNext();
        }
        return current.getElement();
    }

    /**
     * iterates through the list and returns boolean value representing the presence of the element in the list.
     *
     * @param element element to be found in the list.
     * @return true if the element is found in the list, false otherwise.
     */
    @Override
    public boolean contains(T element) {
        return findNode(element) != null;
    }

    /**
     *  returns a boolean value representing if the list is empty or not.
     * @return true if the counter variable is 0, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    /**
     *  returns an int value representing the number of elements in the list
     * @return the value of the counter variable
     */
    @Override
    public int size() {
        return counter;
    }

    /**
     * returns custom iterator for the singlyLinkedList class
     * @return the custom iterator belonging to this class.
     */
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    /**
     * nested class for the iterator for the singlyLinkedList.
     */
    private class MyIterator implements Iterator<T> {
        /**
         * variable used to iterate through list.
         */
        private LinearNode<T> current;

        /**
         * variable used to keep track of the amount of modifications to the list.
         */
        private int expectedModCount;

        /**
         * constructor for MyIterator objects.
         * initiates a current variable at the head of the list, and a
         * expectedModCount with the same value as modCount.
         */
        public MyIterator() {
            current = head;
            expectedModCount = modCount;
        }

        /**
         * the hasNext method exists to represent a boolean value
         * associated to the presence of an element in the list.
         * @return true if current is null, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * <ul>the next method:
         * <li> checks for concurrentMods</li>;
         * <li>checks if hasNext is true or not</li>
         * <li> returns the current element and updates the current variable to the next element in the list.</li>
         * </ul>
         *
         * @throws  NoSuchElementException if the value of hasNext is false.
         * @return the next element in the list
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


        /**
         * @throws UnsupportedOperationException this operation isn't supported by the iterator class
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() is not supported");
        }

        /**
         * supportive method that checks the value of the expectedModCount and modCount.
         */
        private void checkConcurrentMods() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("concurrent mod!");
            }
        }
    }

}
