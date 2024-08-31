package Custom_CollectionsED.trees.binary.binaryLinkedTree;

import Custom_CollectionsED.nodes.BinaryTreeNode;
import Custom_CollectionsED.trees.binary.binaryLinkedTree.LinkedBinaryTree;
import Custom_CollectionsED.trees.binary.interfaces.BinarySearchTreeADT;

import java.util.NoSuchElementException;

/**
 * This class is a representation of a binary search tree, implemented through the use of a linked list.
 * Each node of the list contains references to its children.
 * It implements the BinarySearchTreeADT interface, and extends the LinkedBinaryTree abstract class
 * @param <T>
 */
public class LinkedBinarySearchTree<T> extends LinkedBinaryTree<T> implements BinarySearchTreeADT<T> {

    public LinkedBinarySearchTree(T element) {
        super(element);
    }

    /**
     *  looks for the correct spot
     *  to insert (if it's smaller, goes on the left side,
     *  if larger, on the right)
     * @param element to be added
     */
    @Override
    public void addElement(T element) {
        BinaryTreeNode<T> temp = new BinaryTreeNode<T>(element);
        Comparable<T> comparableElement = (Comparable<T>) element;
        if (isEmpty())
            root = temp;
        else {
            BinaryTreeNode<T> current = root;
            boolean added = false;
            while (!added) {
                if (comparableElement.compareTo(current.getElement()) < 0) {
                    if (current.getLeftChild() == null) {
                        current.setLeftChild(temp);
                        added = true;
                    } else
                        current = current.getLeftChild();
                } else {
                    if (current.getRightChild() == null) {
                        current.setRightChild(temp);
                        added = true;
                    } else
                        current = current.getRightChild();
                }
            }
        }
        count++;
    }

    @Override
    /**
     * Removes the first element that matches the specified target
     * element from the binary search tree and returns a reference to
     * it. Throws a NoSuchElement exception if the specified target
     * element is not found in the binary search tree.
     *
     * @param targetElement the element being sought in the binary
     * search tree
     * @throws NoSuchElementException if an element not found
     * exception occurs
     */
    public T removeElement(T targetElement) throws NoSuchElementException {
        T result = null;
        if (!isEmpty()) {
            if (((Comparable) targetElement).equals(root.getElement())) {
                result = root.getElement();
                root = replacement(root);
                count--;
            } else {
                BinaryTreeNode<T> current, parent = root;
                boolean found = false;
                if (((Comparable) targetElement).compareTo(root.getElement()) < 0)
                    current = root.getLeftChild();
                else
                    current = root.getRightChild();
                while (current != null && !found) {
                    if (targetElement.equals(current.getElement())) {
                        found = true;
                        count--;
                        result = current.getElement();
                        if (current == parent.getLeftChild()) {
                            parent.setLeftChild(replacement(current));
                        } else {
                            parent.setRightChild(replacement(current));
                        }
                    } else {
                        parent = current;
                        if (((Comparable) targetElement).compareTo(current.getElement()) < 0)
                            current = current.getLeftChild();
                        else
                            current = current.getRightChild();
                    }
                } //while
                if (!found)
                    throw new NoSuchElementException("binary search tree");
            }
        } // end outer if
        return result;
    }

    /**
     * This method will erase al occurrences of the specified element in the tree,
     * by successively calling the removeElement method while there are occurences of the
     * requested value.
     * @param targetElement the value to be erased from the tree
     * @throws NoSuchElementException in case the element is not found
     */
    @Override
    public void removeAllOccurrences(T targetElement) throws NoSuchElementException {
        while (contains(targetElement)) {
            removeElement(targetElement);
        }
    }

    /**
     * Removes and returns the minimum element in the binary search tree.
     * If the tree is empty, returns null.
     *
     * @return the minimum element or null if the tree is empty
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


    @Override
    public T findMin() {
        BinaryTreeNode<T> current = root;

        while (current.getLeftChild() != null) {
            current = current.getLeftChild();
        }

        return current != null ? current.getElement() : null;
    }

    @Override
    public T findMax() {
        BinaryTreeNode<T> current = root;

        while (current.getRightChild() != null) {
            current = current.getRightChild();
        }
        return current != null ? current.getElement() : null;
    }

    /**
     * Returns a reference to a node that will replace the one
     * specified for removal. In the case where the removed node has
     * two children, the inorder successor is used as its replacement.
     *
     * @param node the node to be removed
     * @return a reference to the replacing node
     */
    protected BinaryTreeNode<T> replacement(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> result = null;
        if ((node.getLeftChild() == null) && (node.getRightChild() == null)) {
            result = null;
        }
        else if ((node.getLeftChild() != null) && (node.getRightChild() == null)) {
            result = node.getLeftChild();
        }
        else if ((node.getLeftChild() == null) && (node.getRightChild() != null)) {
            result = node.getRightChild();
        }
        else {
            BinaryTreeNode<T> current = node.getRightChild();
            BinaryTreeNode<T> parent = node;
            while (current.getLeftChild() != null) {
                parent = current;
                current = current.getLeftChild();
            }
            if (node.getRightChild() == current) {
                current.setLeftChild(node.getLeftChild());
            } else {
                parent.setLeftChild(current.getRightChild());
                current.setRightChild(node.getRightChild());
                current.setLeftChild(node.getLeftChild());
            }
            result = current;
        }
        return result;
    }
}
