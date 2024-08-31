package Custom_CollectionsED.list.doublyLinkedLists;

import Custom_CollectionsED.list.OrderedListADT;
import Custom_CollectionsED.list.doublyLinkedLists.DoublyLinkedList;
import Custom_CollectionsED.nodes.DoubleNode;

/**
 * This class is an implementation of an Ordered List, which uses a doubly linked list as a support.
 *
 * @param <T> The data type to be stored in the structure, must be comparable.
 */
public class DoublyLinkedOrderedList<T> extends DoublyLinkedList<T> implements OrderedListADT<T> {

    /**
     * <ul> Adds an element to the doubly linked list while maintaining ascending order.
     * <li> Creates a new node containing the specified element;</li>
     * <li> If the list is empty, sets the new node as both head and tail;</li>
     * <li> Compares the elements in the list to find the appropriate position for insertion in ascending order;</li>
     * <li> Inserts the node before the first larger element or at the end,
     *   if the element is larger than all existing elements.</li>
     *</ul>
     *
     * @param element the element to be added to the list
     */
    @Override
    public void add(T element) {
        DoubleNode<T> newNode = new DoubleNode<>(element);

        // Check if the list is empty
        if (isEmpty()) {
            head = tail = newNode;
            counter++;
            modCount++;
            return;
        }

        DoubleNode<T> current = head;

        while (current != null) {
            T currentElement = current.getElement();

            //ver se o elemento atual n está nulo
            if (currentElement != null) {
                int comparison = ((Comparable<T>) currentElement).compareTo(element);

                if (comparison > 0) { //se for "mais" que o atual, insere antes dele

                    newNode.setNext(current);
                    newNode.setPrevious(current.getPrevious());

                    if (current.getPrevious() != null) {//se não for a cabeça
                        current.getPrevious().setNext(newNode);
                    } else {//se for a cabeça
                        head = newNode;
                    }

                    current.setPrevious(newNode);

                    counter++;
                    modCount++;
                    return;
                }
            }
            current = current.getNext(); //nova iteração
        }
        // se for "mais" que todos os outros, fica adicionado no final (cauda)
        tail.setNext(newNode);
        newNode.setPrevious(tail);
        tail = newNode;

        counter++;
        modCount++;
    }


}
