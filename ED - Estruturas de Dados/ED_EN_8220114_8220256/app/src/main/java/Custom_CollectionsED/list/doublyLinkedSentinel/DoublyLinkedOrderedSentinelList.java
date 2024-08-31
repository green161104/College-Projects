package Custom_CollectionsED.list.doublyLinkedSentinel;

import Custom_CollectionsED.list.OrderedListADT;
import Custom_CollectionsED.list.doublyLinkedSentinel.DoublyLinkedSentinelList;
import Custom_CollectionsED.nodes.DoubleNode;

/**
 * This class is a representation of an ordered list, implemented through a doubly linked list with sentinel nodes.
 * Implements the OrderedListAdt, and extends the DoublyLinkedSentinelList abstract class
 * @param <T> the data type to be stored within this structure, must be comparable
 */
public class DoublyLinkedOrderedSentinelList<T> extends DoublyLinkedSentinelList<T> implements OrderedListADT<T> {


    /**
     * <ul> The add method:
     *  <li>Adds the specified element to the ordered list, ensuring it is inserted in the correct position.</li>
     *  <li>The method iterates through the existing elements, comparing each one, and finds the appropriate slot for the new element.</li>
     *</ul>
     * @param element the value to be added to the ordered list
     */
    @Override
    public void add(T element) {

        DoubleNode<T> elementToBeAdded = new DoubleNode<>(element);

        if (count == 0) {
            //check if empty
            head.setNext(elementToBeAdded);
            tail.setPrevious(elementToBeAdded);
            elementToBeAdded.setPrevious(head);
            elementToBeAdded.setNext(tail);
            count++;
            modCount++;
            return; //sair do metodo
        }

        //caso nao esteja vazio
        DoubleNode<T> currentNode = head.getNext();
        while (currentNode != tail) {
            int comparison = ((Comparable<T>) currentNode.getElement()).compareTo(element);

            if (comparison > 0) {
                //inserir elemento e sair do loop + metodo
                elementToBeAdded.setNext(currentNode); //o novo elemento fica em atras ao que já lá estava
                elementToBeAdded.setPrevious(currentNode.getPrevious()); //e fica a frente do anterior desse
                currentNode.getPrevious().setNext(elementToBeAdded);//atualizar referencia para o elemento anterior do novo
                count++;
                modCount++;
                return;
            }

            currentNode = currentNode.getNext();//nova iteração
        }

        // caso o elemento novo seja "superior" a todos os outros, fica inserido no final (cauda)
        elementToBeAdded.setPrevious(tail.getPrevious());
        tail.getPrevious().setNext(elementToBeAdded);
        elementToBeAdded.setNext(tail);
        tail.setPrevious(elementToBeAdded);

        count++;
        modCount++;
    }
}



