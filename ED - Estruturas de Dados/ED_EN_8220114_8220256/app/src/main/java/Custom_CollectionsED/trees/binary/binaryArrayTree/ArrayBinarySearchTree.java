package Custom_CollectionsED.trees.binary.binaryArrayTree;

import Custom_CollectionsED.trees.binary.binaryArrayTree.ArrayBinaryTree;
import Custom_CollectionsED.trees.binary.interfaces.BinarySearchTreeADT;

import java.util.NoSuchElementException;

/**
 * This class is an implementation of a Binary Search tree, which uses arrays as its support.
 * It implements the BinarySearchTreeADT interface, and extends the ArrayBinaryTree abstract class
 *
 * @param <T> The datatype to be stored in this structure, must be comparable.
 */
public class ArrayBinarySearchTree<T> extends ArrayBinaryTree<T> implements BinarySearchTreeADT<T> {

    /**
     * current height of the tree
     */
    protected int height;

    /**
     * keeps track of the maximum index in the underlying array that is currently being utilized
     */
    protected int maxIndex;


    /**
     * Creates an empty binary search tree.
     */
    public ArrayBinarySearchTree() {
        super();
        height = 0;
        maxIndex = -1;
    }

    /**
     * Creates a binary search with the specified element as its root
     *
     * @param element  the element that will become the root of
     *                 the new tree
     * @param capacity the size desired for the array
     */
    public ArrayBinarySearchTree(T element, int capacity) {
        super(element, capacity);
        height = 1;
        maxIndex = 0;
    }

    @Override
    /**
     * Adds the specified object to this binary search tree in the
     * appropriate position according to its key value. Note that
     * equal elements are added to the right. Also note that the
     * index of the left child of the current index can be found by
     * doubling the current index and adding 1. Finding the index
     * of the right child can be calculated by doubling the current
     * index and adding 2.
     *
     * @param element the element to be added to the search tree
     */ public void addElement(T element) {
        if (tree.length < maxIndex * 2 + 3) {
            expandCapacity();
        }
        Comparable<T> tempElement = (Comparable<T>) element;
        if (isEmpty()) {
            tree[0] = element;
            maxIndex = 0;
        } else {
            boolean added = false;
            int currentIndex = 0;
            while (!added) {
                if (tempElement.compareTo((tree[currentIndex])) < 0) {
                    // go left
                    if (tree[currentIndex * 2 + 1] == null) {
                        tree[currentIndex * 2 + 1] = element;
                        added = true;
                        if (currentIndex * 2 + 1 > maxIndex) {
                            maxIndex = currentIndex * 2 + 1;
                        }
                    } else currentIndex = currentIndex * 2 + 1;
                } else {
                    // go right
                    if (tree[currentIndex * 2 + 2] == null) {
                        tree[currentIndex * 2 + 2] = element;
                        added = true;
                        if (currentIndex * 2 + 2 > maxIndex) maxIndex = currentIndex * 2 + 2;
                    } else currentIndex = currentIndex * 2 + 2;
                }
            }
        }
        height = (int) (Math.log(maxIndex + 1) / Math.log(2)) + 1;
        count++;
    }

    /**
     * Removes a given element from the tree, by looking for it within the array.
     * Makes use of helper methods such as findSuccessor() and removeNode().
     *
     * @param targetElement value to be sought after and removed from the tree
     * @return the reference to the now removed element
     */
    @Override
    public T removeElement(T targetElement) {

        T result = null;
        if (!isEmpty()) {

            if (((Comparable<T>) targetElement).compareTo(getRoot()) < 0) {
                result = getRoot();
                tree[0] = tree[findSuccessorIndex(0)];
                count--;
            } else {
                int current, parent = 0;
                boolean found = false;
                if (((Comparable<T>) targetElement).compareTo(getRoot()) < 0) {
                    current = 1;
                } else {
                    current = 2;
                }
                while (tree[current] != null && !found) {
                    if (((Comparable<T>) targetElement).compareTo(getRoot()) == 0) {
                        found = true;
                        count--;
                        result = tree[current];
                        if (tree[current] == tree[parent * 2 + 1]) {
                            tree[parent * 2 + 1] = tree[findSuccessorIndex(parent * 2 + 1)];
                        } else {
                            tree[parent * 2 + 2] = tree[findSuccessorIndex(parent * 2 + 2)];
                        }
                    } else {
                        parent = current;
                        if (((Comparable<T>) targetElement).compareTo(tree[current]) < 0) {
                            current = current * 2 + 1;
                        } else {
                            current = current * 2 + 2;
                        }
                    }
                    if (!found) {
                        throw new NoSuchElementException("element not found");
                    }
                }

            }
        }
        return result;
    }



    /**
     * Helper method to find the index of the successor of a node.
     * The successor is the smallest element that is LARGER than the original
     *
     * @param target the current index
     * @return the successor to the current index
     */
    private int findSuccessorIndex(int target) {

        int result = -1;
        if ((tree[target * 2 + 1] == null) && (tree[target * 2 + 2] == null)) {
            result = -1;
        }
        if ((tree[target * 2 + 1] != null) && (tree[target * 2 + 2] == null)) {
            result = target * 2 + 1;
        }
        if ((tree[target * 2 + 1] == null) && (tree[target * 2 + 2] != null)) {
            result = target * 2 + 2;
        } else {
            int current = target * 2 + 1;
            int parent = current;

            while (tree[current * 2 + 1] != null) {
                parent = current;
                current = current * 2 + 1;
            }
            if (tree[target * 2 + 2] == tree[current]) {
                tree[current * 2 + 1] = tree[target * 2 + 1];
            } else {
                tree[parent * 2 + 1] = tree[current * 2 + 2];
                tree[current * 2 + 2] = tree[target * 2 + 2];
                tree[current * 2 + 1] = tree[target * 2 + 1];
            }
            result = current;
        }
        return result;
    }



    /**
     * While there are occurrences of a value within a tree, it will  call the
     * remove method on that element until there are no remaining occurrences of that value
     * within the tree
     *
     * @param targetElement to remove occurrences of
     */
    @Override
    public void removeAllOccurrences(T targetElement) {
        while (contains(targetElement)) {
            removeElement(targetElement);
        }
    }

    /**
     * Removes and returns the minimum element of the tree.
     *
     * @return the minimum element of the tree, now removed, or null, if the tree is empty
     */
    @Override
    public T removeMin() {
        T minValue = findMin(); // Use findMin as a helper

        if (minValue != null) {
            removeElement(minValue); // Use removeElement to remove the found minimum
        }

        return minValue;
    }

    /**
     * Removes and returns the maximum element in the binary search tree.
     * If the tree is empty, returns null.
     *
     * @return the maximum element or null if the tree is empty
     */
    @Override
    public T removeMax() {
        T maxValue = findMax(); // Use findMax as a helper

        if (maxValue != null) {
            removeElement(maxValue); // Use removeElement to remove the found maximum
        }

        return maxValue;
    }

    /**
     * @return the minimum value within the tree
     */
    @Override
    public T findMin() {
        if (getRoot() == null) {
            return null; // Empty tree
        }

        int current = 0;
        while (tree[current * 2 + 1] != null) {
            current = current * 2 + 1;
        }
        return tree[current];
    }

    /**
     * @return the maximum value within the tree
     */
    @Override
    public T findMax() {
        if (getRoot() == null) {
            return null; // Empty tree
        }

        int current = 0;
        while (tree[current * 2 + 2] != null) {
            current = current * 2 + 2;
        }
        return tree[current];
    }


}
