package Custom_CollectionsED.list.singlyLinkedLists;

import Custom_CollectionsED.list.ListADT;
import Custom_CollectionsED.list.UnorderedListADT;
import Custom_CollectionsED.list.singlyLinkedLists.SinglyLinkedList;
import Custom_CollectionsED.nodes.DoubleNode;
import Custom_CollectionsED.nodes.LinearNode;

/**
 * This class is an implementation of an Unordered List, which uses a singly linked list as support.
 * It implements the UnorderedListADT and extends the SinglyLinkedList abstract class
 * @param <T> Data type to be used in the data structuremust be comparable
 */
public class SinglyLinkedUnorderedList<T> extends SinglyLinkedList<T> implements UnorderedListADT<T> {

    /**
     * adds the given element as the head of the list.
     * @param element element to be added
     */
    @Override
    public void addToFront(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);

        if (head != null) {
            newNode.setNext(head);
        }
        head = newNode;
        counter++;
        modCount++;
    }


    /**
     * adds the given element to the end of the list.
     * @param element element to be added
     */
    @Override
    public void addToRear(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);

        if (head == null) {
            head = newNode;
        } else {
            LinearNode<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }

        counter++;
        modCount++;
    }


    /**
     * takes in an element and a target element to which we wish to add the first element, after the second.
     * The method will iterate through the entire list looking for the "target" element, and add
     * the elementToAdd right after the target element.
     * @param elementToAdd element to be added.
     * @param target target element to which we wish to add after.
     */
    @Override
    public void addAfter(T elementToAdd, T target) {
        LinearNode<T> newNode = new LinearNode<>(elementToAdd);
        LinearNode<T> targetNode = findNode(target);

        if (targetNode != null) {
            newNode.setNext(targetNode.getNext());
            targetNode.setNext(newNode);
            counter++;
            modCount++;
        } else {
            System.out.println("Target element not found in the list.");
        }
    }

}
