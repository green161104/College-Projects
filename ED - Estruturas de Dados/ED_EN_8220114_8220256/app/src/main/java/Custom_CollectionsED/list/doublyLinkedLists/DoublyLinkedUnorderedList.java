package Custom_CollectionsED.list.doublyLinkedLists;

import Custom_CollectionsED.list.UnorderedListADT;
import Custom_CollectionsED.list.doublyLinkedLists.DoublyLinkedList;
import Custom_CollectionsED.nodes.DoubleNode;


/**
 * This class is an implementation of an Ordered List, which uses a doubly linked list as a support.
 *
 * @param <T> The data type to be stored in the structure, must be comparable.
 */
public class DoublyLinkedUnorderedList<T> extends DoublyLinkedList<T> implements UnorderedListADT<T> {

    /**
     *<ul> Adds an element to the front of the doubly linked list.
     * <li> Creates a new node containing the specified element.</li>
     * <li> If the list is empty, sets both head and tail to the new node.</li>
     * <li> Inserts the new node before the current head node and updates references accordingly.</li>
     *</ul>
     * @param element the element to be added to the front of the list
     */
    @Override
    public void addToFront(T element) {
        DoubleNode<T> newNode = new DoubleNode<>(element);

        // If the list is empty, set both head and tail to the new node
        if (counter == 0) {
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }

        counter++;
        modCount++;
    }


    /**
     * <ul>Adds an element to the rear (end) of the doubly linked list.
     * <li> Creates a new node containing the specified element. </li>
     * <li> If the list is empty, sets both head and tail to the new node.</li>
     * <li> Inserts the new node after the current tail node and updates references accordingly.</li>
     *</ul>
     * @param element the element to be added to the rear of the list
     */
    @Override
    public void addToRear(T element) {
        DoubleNode<T> newNode = new DoubleNode<>(element);

        // If the list is empty, set both head and tail to the new node
        if (counter == 0) {
            head = tail = newNode;
        } else {
            newNode.setPrevious(tail);
            tail.setNext(newNode);
            tail = newNode;
        }

        counter++;
        modCount++;
    }


    /**
     * <ul>Adds an element after a specified target element in the doubly linked list.
     * <li> Creates a new node containing the element to be added.</li>
     * <li> Finds the node containing the target element in the list.</li>
     * <li> Updates connections to insert the new node after the target node.</li>
     * <li> Updates references of adjacent nodes to accommodate the new node.</li>
     * <li> Updates tail reference if the new node is added after the current tail.</li>
     *</ul>
     *
     * @param elementToAdd the element to be added after the target element
     * @param target the target element after which the new element will be added
     */
    @Override
    public void addAfter(T elementToAdd, T target) {
        DoubleNode<T> newNode = new DoubleNode<>(elementToAdd);
        DoubleNode<T> targetNode = findNode(target);

        // Update connections to insert the new node after the target node
        newNode.setNext(targetNode.getNext());
        newNode.setPrevious(targetNode);

        if (targetNode.getNext() != null) {
            targetNode.getNext().setPrevious(newNode);
        }

        targetNode.setNext(newNode);

        // If the new node is added after the current tail, update tail
        if (targetNode == tail) {
            tail = newNode;
        }

        counter++;
        modCount++;
    }

}
