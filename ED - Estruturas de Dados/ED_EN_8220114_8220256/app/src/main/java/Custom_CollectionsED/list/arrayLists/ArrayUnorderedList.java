package Custom_CollectionsED.list.arrayLists;

import Custom_CollectionsED.list.UnorderedListADT;
import Custom_CollectionsED.list.arrayLists.ArrayList;

import java.util.NoSuchElementException;

/**
 * This class is an implementation of an Unordered list, which uses arrays as a support.
 * It implements the UnorderedListADT, and extends the ArrayList abstract class.
 * @param <T> the data type stored in the structure, must be comparable
 */
public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {
    public ArrayUnorderedList() {
        super();
    }

    /**
     * <ul> The addToFront method:
     * <li>takes in an element to be added, as a parameter</li>
     * <li>checks if the array has reached maximum capacity, if so,
     * resizes the array with a call to resizeArray(), which is present
     * in the superclass</li>
     *<li>shifts all of the elements, so that the new element
     * can be added at the front, as the first slot is now empty</li>
     * Finally, the modCount and the counter to keep track of the number of elements
     * are updated.
     * @param element element to be added to the array
     * </ul>
     */
    @Override
    public void addToFront(T element) {
        if (counter == elements.length) {
            super.resizeArray();
        }

        //shift existing elements to make space for the new element
        for (int i = counter; i > 0; i--) {
            elements[i] = elements[i - 1];
        }

        // Add the new element at the front
        elements[0] = element;
        counter++;
        modCount++;
    }

    /**
     * the addToRear method, takes in an element to be added,
     * check to see if the array needs to be resized, and then adds
     * the given element to the last unused index in the array.
     *
     * After that updates counter and modCount variables
     * @param element element to be added to the array
     */
    @Override
    public void addToRear(T element) {
        if (counter == elements.length) {
            resizeArray();
        }

        elements[counter] = element;
        counter++;
        modCount++;
    }

    /**
     * <ul>the add after method :
     * <li> checks for resizing array;</li>
     * <li> looks through the array in search of the target element and saves it's index;
     * <li> shifts all elements in the array after the target array;</li>
     * <li> adds the element to be added to the index after the target element;</li>
     * <li> updates counter and modCount variable;</li>
     *</ul>
     *
     * @param element element to be added to the array
     * @param target target element to which the user wishes to add after
     * @throws NoSuchElementException if the target element is not found in the arrayList
     */
    @Override
    public void addAfter(T element, T target) {
        if (counter == elements.length) {
            resizeArray();
        }

        int current = 0;

        // Find the target element
        while (current < counter && ((Comparable<T>)elements[current]).compareTo(target) != 0) {
            current++;
        }

        if (current == counter) {
            // Target not found
            throw new NoSuchElementException("Target element not found: " + target);
        }

        // Shift elements to make space for the new element
        for (int i = counter; i > current + 1; i--) {
            elements[i] = elements[i - 1];
        }

        // Add the new element after the target
        elements[current + 1] = element;
        counter++;
        modCount++;
    }

}
