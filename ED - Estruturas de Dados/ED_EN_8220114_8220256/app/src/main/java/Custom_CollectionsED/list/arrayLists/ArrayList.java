package Custom_CollectionsED.list.arrayLists;

import Custom_CollectionsED.list.ListADT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is an implementation of the List data structure, with
 * the usage of arrays as a support for the structure. It implements the ListADT interface.
 * @param <T> The data type to be stored in the list, must be comparable.
 */
public abstract class ArrayList<T> implements ListADT<T> {

    /**
     * default size for the array
     */
    private final int DEFAULT_SIZE = 100;
    /**
     * expand factor for the array
     */
    private final int EXPAND_FACTOR = 2;
    /**
     * counter for the amount of elements in the array
     */
    protected int counter;
    /**
     * counter for the modifications done in the array
     */
    protected int modCount;
    /**
     * array of elements
     */
    protected T[] elements;

    /**
     * Constructor for the ArrayList class, creates an Arraylist object with size,
     * a counter and a modCount at both 0.
     */
    public ArrayList() {
        this.elements = (T[]) new Object[DEFAULT_SIZE];
        this.counter = 0;
        this.modCount = 0;
    }

    /**
     * <ul> The removefirst method :
     *  <li> check if the arrayList is empty;</li>
     *  <li> goes into the arrayList and removes the first element,
     *    by moving all the elements one spot to the left in the array;</li>
     *  <li> updates the counter variable;</li>
     *  <li> updates the modCount variable;</li>
     *</ul>
     * @return the elementRemoved from the arrayList
     */
    @Override
    public T removeFirst() {
        if (counter == 0) {
            throw new NoSuchElementException("empty list :(");
        }
        T elementRemoved = elements[0];
        for (int i = 0; i < counter - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[counter - 1] = null;
        counter--;
        modCount++;
        return elementRemoved;
    }
    /**
     *<ul> The removelast method :
     *  <li> check if the arrayList is empty.</li>
     *  <li> goes into the arrayList and removes the first element,
     *    by moving all the elements one spot to the right in the array;</li>
     *  <li> updates the counter variable;</li>
     *  <li> updates the modCount variable;</li>
     *</ul>
     * @return the elementRemoved from the arrayList
     */
    @Override
    public T removeLast() {
        if (counter == 0) {
            throw new NoSuchElementException("empty list :(");
        }
        T elementRemoved = elements[counter - 1];
        elements[counter - 1] = null;
        counter--;
        modCount++;
        return elementRemoved;
    }


    /**
     *<ul> The remove method :
     *  <li> check if the arrayList is empty;</li>
     *  <li> iterates through the entire array looking to find the index of the
     *  element given as a parameter in the array;</li>
     *  <li> after finding the index for the element to be removed, proceeds to move
     *  all element in the array one spot to the right, beginning at the index of the element
     *  to be removed, thus eliminating it from the array;</li>
     *  <li> turns the last spot in the array into null ( eliminating duplicates for the last element );
     *  updates the counter variable;</li>
     *  <li> updates the modCount variable;</li>
     *</ul>
     * @throws NoSuchElementException if the element doesn't exist in the list.
     * @param element element to be removed
     * @return the removedElement from the array
     */
    @Override
    public T remove(T element) {
        if (counter == 0) {
            throw new NoSuchElementException("Empty list");
        }

        int indexToRemove = -1; //initiate with randome number, -1 cuz -1 usually bad in java
        for (int i = 0; i < counter; i++) {
            if (elements[i].equals(element)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) { //if that value has changed, means it's found the right element
            T removedElement = elements[indexToRemove];
            for (int i = indexToRemove; i < counter - 1; i++) {
                elements[i] = elements[i + 1]; //shift
            }
            elements[counter - 1] = null; //ultimo passa a null
            counter--;
            modCount++;
            return removedElement;
        } else {
            throw new NoSuchElementException("Element doesn't exist :p");
        }
    }

    /**
     * @return first element in the array
     */
    @Override
    public T first() {
        return this.elements[0];
    }

    /**
     * @return last element in the array
     */
    @Override
    public T last() {
        return this.elements[counter - 1];
    }

    /**

     * The contains() method checks whether the given element exists in the arrayList.
     * Returns true if it does exist,
     * eturns false if it doesn't exist.
     * @param element element to check existance of.
     * @return boolean variable representing the existance or not existance of the element.
     */
    @Override
    public boolean contains(T element) {
        for (int i = 0; i < counter; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if the counter variable is at 0, else false.
     */
    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    /**
     * @return value of the counter variable
     */
    @Override
    public int size() {
        return counter;
    }

    /**
     * method used to create custom iterator objects for this given class.
     * @return MyIterator
     */
    @Override
    public MyIterator iterator() {
        return new MyIterator();
    }

    /**
     * returns String representation of all elements in the array
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("List: \n");

        for (int i = 0; i < counter; i++) {
            T currentElement = elements[i];

            // check if it's null, if it is, break so that it doesn't print
            if (currentElement != null) {
                s.append(currentElement.toString()).append("\n");
            } else {
                break;
            }
        }

        return s.toString();
    }

    /**<ul>The resize Array method:
     * <li> creates a new T[] array with double the size of the previous existing array, which
     * is assumed to be filled.</li>
     * <li> After creating the new array, copies the original array into the newArray.</li>
     * <li> Redefines the elements variable as the new array.</li>
     * </ul>
     */
    protected void resizeArray() {
        int newSize = elements.length * EXPAND_FACTOR;
        T[] newElements = (T[]) new Object[newSize];
        System.arraycopy(elements, 0, newElements, 0, counter);
        elements = newElements;
    }

    /**
     * Private inner class implementing the Iterator interface for the ArrayList.
     * Provides iteration over the elements of the list, making it possible to use an
     * enhanced for cicle, i.e, a for each.
     */
    private  class MyIterator implements Iterator<T> {

        /**
         * expectedModCount is used as a safeguard to ensure the iterator
         * isn't iterating through a different arrayList than the one
         * that is the most recent one.
         */
        protected int expectedModCount;
        protected int current;
        protected boolean okayToRemove;

        /**
         * Constructor for the MyIterator class.
         */
        public MyIterator(){
            expectedModCount = modCount;
            current = 0;
            okayToRemove = false;
        }

        /**
         * the checkConcurrentMods is used as supporting method to other methods in the
         * MyIterator class; it is used to assure that expectedModCount variable and modCount
         * variable keep the same value.
         * @throws ConcurrentModificationException in case a concurrent modification happens
         */
        protected void checkConcurrentMods() {
            if (expectedModCount != modCount){
                throw new ConcurrentModificationException("concurrent mod!");
            }
        }

        /**
         * @return true if the current variable is lower than counter variable in ArrayList
         */
        @Override
        public boolean hasNext() {
            checkConcurrentMods();
            return (current < counter);
        }

        /**
         * <ul>the next method :
         *  <li> checks for concurrent mods;</li>
         *  <li> check for hasNext;</li>
         *  <li> changes the okayToRemove variable to true;</li>
         *  <li> returns an element and updates the current variable;</li>
         *
         * </ul>
         * @return an element from the collection the array is iterating through
         * @throws NoSuchElementException if there is no element to iterate after
         */
        @Override
        public T next() {
            checkConcurrentMods();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            this.okayToRemove = true;
            return (T) elements[current++];
        }

        /**
         * <ul>the remove method :
         *  <li> checks for concurrent modificiations;</li>
         *  <li> checks the value of the okayToRemove variable;</li>
         *  <li> if the value of said variable is true, it'll shift all element in the arrayList one spot
         *    to the right, then it'll eliminate the last spot in the ArrayList;</li>
         *  <li> updates counter to update the array size;</li>
         *  <li> updates okayToRemove variable to false;</li>
         *  <li> increments expectedModCount and modCount;</li>
         * </ul>
         * @throws IllegalStateException if the remove method has been called without a preceding next.
         */
        @Override
        public void remove() {
            checkConcurrentMods();
            if (okayToRemove) {
                for (int i = current; i < counter - 1; i++) {
                    elements[i] = elements[i + 1]; //shift de elementos
                }
                elements[counter - 1] = null; //last element is gonna be null, because of the shift
                counter--; // update the size of the array
                okayToRemove = false;
                expectedModCount++;
                modCount++;
            } else {
                throw new IllegalStateException("remove() called without a preceding next()");
            }
        }

    }
}

