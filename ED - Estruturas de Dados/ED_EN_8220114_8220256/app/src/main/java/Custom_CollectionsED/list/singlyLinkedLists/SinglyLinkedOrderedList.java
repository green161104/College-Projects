package Custom_CollectionsED.list.singlyLinkedLists;

import Custom_CollectionsED.list.OrderedListADT;
import Custom_CollectionsED.list.singlyLinkedLists.SinglyLinkedList;
import Custom_CollectionsED.nodes.LinearNode;

/**
 * This class is an implementation of the Ordered List, which used a singly linked list as support.
 * It implements the OrderedListADT, and extends SinglyLinkedList abstract class
 * @param <T> the data type to be stored in the collection, must be comparable
 */
public class SinglyLinkedOrderedList<T> extends SinglyLinkedList<T> implements OrderedListADT<T> {

    /**
     *  <ul> the add method :
     *  <li> takes in a node to add and checks if the list is empty;</li>
     *  <li> iterates through the whole list checking to see where the new element should be added at</li>
     *  <li> adds the element before the current element in the iteration</li>
     *  <li> updates head reference if needed </li>
     *  <li> updates counter and modCount variables</li>
     *  </ul>
     * @param element element to be added
     */
    @Override
    public void add(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);

        // Check if the list is empty
        if (isEmpty()) {
            head = newNode;
            counter++;
            modCount++;
            return;
        }

        LinearNode<T> current = head;
        LinearNode<T> previous = null;

        // Iterate through the list to find the correct position
        while (current != null) {
            T currentElement = current.getElement();

            if (currentElement != null) {
                int comparison = ((Comparable<T>) currentElement).compareTo(element);

                if (comparison > 0) {
                    // Insert before the current node
                    newNode.setNext(current);
                    previous.setNext(newNode);
                    } else {
                        // If the newNode is inserted before the head, update the head
                        head = newNode;
                    }
                    counter++;
                    modCount++;
                    return;
                }

            // Move to the next node
            previous = current;
            current = current.getNext();
        }

        // If reached the end of the list, insert at the end
        previous.setNext(newNode);
        counter++;
        modCount++;
    }


}
