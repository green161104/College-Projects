package Custom_CollectionsED.list.doublyLinkedSentinel;

import Custom_CollectionsED.list.UnorderedListADT;
import Custom_CollectionsED.list.doublyLinkedSentinel.DoublyLinkedSentinelList;
import Custom_CollectionsED.nodes.DoubleNode;


import java.util.NoSuchElementException;

/**
 * This class is a representation of an unordered list, implemented with a doubly linked list with sentinel nodes.
 * Implements the UnorderedListADT, and extends the DoublyLinkedSentinelList abstract class
 * @param <T> the data type to be stored in this collection, must be comparable
 */
public class DoublyLinkedUnorderedSentinelList<T> extends DoublyLinkedSentinelList<T> implements UnorderedListADT<T> {


    /**
     * Constructor which returns a reference to a list, using the superclass constructor
     */
    public DoublyLinkedUnorderedSentinelList(){
        super();
    }

    /**
     * The addToFront method will add an element to the front of the list,
     * updating the surrounding nodes to reflect the new addition
     * @param element value to be added to the list
     */
    @Override
    public void addToFront(T element) {

        DoubleNode<T> elementToBeAdded = new DoubleNode<>(element); //create temp node

        if(count == 0){
            head.setNext(elementToBeAdded);
            tail.setPrevious(elementToBeAdded);
            elementToBeAdded.setNext(tail);
            elementToBeAdded.setPrevious(head);

            count++;
            modCount++;
            return; //sair do metodo
        }

        head.getNext().setPrevious(elementToBeAdded); //element in front of head is now gonna point back to NEW element
        elementToBeAdded.setNext(head.getNext()); // NEW element is now gonna point to prior first element
        head.setNext(elementToBeAdded); // head now points to NEW element
        elementToBeAdded.setPrevious(head); //NEW element points back to head

        count++;
        modCount++;

    }

    /**
     * The addToRear method will add an element to the rear of the list,
     * updating the surrounding nodes to reflect the new addition
     * @param element value to be added to the list
     */
    @Override
    public void addToRear(T element) {

        DoubleNode<T> elementToBeAdded = new DoubleNode<>(element); //create temp node

        if(count == 0){
            head.setNext(elementToBeAdded);
            tail.setPrevious(elementToBeAdded);
            elementToBeAdded.setNext(tail);
            elementToBeAdded.setPrevious(head);

            count++;
            modCount++;
            return;
        }

        elementToBeAdded.setPrevious(tail.getPrevious());
        tail.getPrevious().setNext(elementToBeAdded); //element in the back  now  points to NEW element
        elementToBeAdded.setNext(tail); // NEW element is now gonna point to tail
        tail.setPrevious(elementToBeAdded); //tail now sets previous to NEW element

        count++;
        modCount++;

    }

    /**
     * The addAfter method will add an element to the list, after
     * the specified target,
     * updating the surrounding nodes to reflect the new addition
     * @param element value to be added to the list
     * @param target
     */
    @Override
    public void addAfter(T element, T target) { //problema neste método

        if(count == 0){
            System.out.println("The list is empty, but the element has been added to the front.");
            addToFront(element);
            return; //sair do metodo aqui
        }

        DoubleNode<T> elementToBeAdded = new DoubleNode<>(element); //create temp node
        DoubleNode<T> currentNode = head.getNext();

        while(currentNode != tail){ //como podes ter que por um elemento logo atras da cauda, este loop tem de ir ate à cauda
            if(((Comparable<T>)currentNode.getElement()).compareTo(target) == 0){

                elementToBeAdded.setPrevious(currentNode);
                elementToBeAdded.setNext(currentNode.getNext());
                currentNode.getNext().setPrevious(elementToBeAdded);
                currentNode.setNext(elementToBeAdded);
                count++;
                modCount++;
                return;
            }
           currentNode = currentNode.getNext(); //faltava esta linha e o return na linha atrás, ele não estava a criar uma nova iteração
        }

        throw new NoSuchElementException("Unexpected error! ");
    }

}
