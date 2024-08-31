package Custom_CollectionsED.trees.binary.interfaces;

import trees.binary.interfaces.BinaryTreeADT;

/**
 * This interface details a contract for the implementation of a binary tree
 * @param <T> datatype to be used with this data structure
 */
public interface BinarySearchTreeADT<T> extends BinaryTreeADT<T> {

    /**
     * Adds an element to the tree
     * @param element to be added to the tree
     */
    public void addElement(T element);

    /**
     * Removes an element from the tree, returning a reference to that deleted element's value
     * @param targetElement to be removed from the tree
     * @return a reference to the value of the deleted element
     */
    public T removeElement(T targetElement);

    /**
     * Removes all occurrences of a specified element
     * @param targetElement the element to be removed
     */
    public void removeAllOccurrences(T targetElement);

    /**
     * Removes the minimum element in the tree
     * @return a reference to the value of the deleted element
     */
    public T removeMin();

    /**
     * Removes the maximum element in the tree
     * @return a reference to the value of the deleted element
     */
    public T removeMax();

    /**
     * Seeks and returns the minimum element
     * @return
     */
    public T findMin();

    /**
     * Seeks and returns the maximum element
     * @return
     */
    public T findMax();
}
