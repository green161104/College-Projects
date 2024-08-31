package Custom_CollectionsED.list.arrayLists;

import Custom_CollectionsED.list.OrderedListADT;
import Custom_CollectionsED.list.arrayLists.ArrayList;

/**
 * This class is an implementation of an ordered list with the use of arrays as a support.
 * It implements the OrderedListADT, and extends the abstract ArrayList class
 *
 * @param <T> The datatype to be stored in this structure, must be comparable.
 */
public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {

    /**
     * Default constructor for the Array Ordered List, which has a default size,
     * makes use of the superclass constructor
     */
    public ArrayOrderedList() {
        super();
    }

    /**
     * <ul>The add method :
     * <li> Checks whether the list is empty, if so, simply adds the element to the arrayList;</li>
     * <li>If it's not empty, compares the given element to all elements in the array until it has found
     * its correct position
     * <li> Shifts all elements after the found element one spot to the left and then adds the new element in the empty slot;</li>
     * <li> Updates counter and modCount variables;</li>
     * </ul>
     *
     * @param element element to be Added to the ArrayList
     */
    @Override
    public void add(T element) {

        if (isEmpty()) { //se estiver vazio, é só colocar o elemento
            elements[counter] = element;
            counter++;
            modCount++;
            return;
        }

        // if the array is full, resize it
        if (counter == elements.length) {
            super.resizeArray();
        }

        int current = 0; // keep count of the current index, will be the index of the new element once we know where to put it

        // Find position for the new element, using compareTo
        while (current < counter) {
            T currentElement = elements[current];
            // Null check before making the comparison
            if (currentElement != null) {
                int comparison = ((Comparable<T>) currentElement).compareTo(element); // assumes element is comparable

                if (comparison > 0) {
                    break; // found the correct position
                }
                current++;
            }
        }

        // Shift elements to make space for the new element
        for (int i = counter; i > current; i--) {
            elements[i] = elements[i - 1];
        }
        elements[current] = element;
        counter++;
        modCount++;
    }


}


