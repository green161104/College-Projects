package Custom_CollectionsED.trees.binary.avl;

import Custom_CollectionsED.nodes.BinaryTreeNode;
import Custom_CollectionsED.trees.binary.binaryLinkedTree.LinkedBinaryTree;

/**
 * This class is the implementation of an AVL tree, and extends the LinkedBinaryTree abstract class
 * @param <T> the data type to be stored int heis tree, must be comparable
 */
public class AVLTree<T> extends LinkedBinaryTree<T> {

    /**
     * Default constructor for AVLTree class.
     * Calls the constructor of the superclass (LinkedBinaryTree).
     */
    public AVLTree(){
        super();
    }

    /**
     * Updates the height of a given node based on the heights of its left and right children.
     *
     * @param node the node for which to update the height
     */
    public void updateHeight(BinaryTreeNode<T> node) {
        if (node != null) {
            node.setHeight(1 + Math.max(height(node.getLeftChild()), height(node.getRightChild())));
        }
    }

    /**
     * Returns the height of the given node.
     *
     * @param node the node for which to retrieve the height
     * @return the height of the node or -1 if the node is null
     */
    public int height(BinaryTreeNode<T> node) {
        return node == null ? -1 : node.getHeight();
    }

    /**
     * Calculates and returns the balance factor of a given node,
     * which is the difference between the height of the right subtree
     * and the height of the left subtree.
     *
     * @param node the node for which to calculate the balance factor
     * @return the balance factor of the node
     */
    public int getBalance(BinaryTreeNode<T> node) {
        return (node == null) ? 0 : height(node.getRightChild()) - height(node.getLeftChild());
    }

    /**
     * Inserts a new element into the AVL tree while maintaining the AVL property.
     *
     * @param targetElement the element to be inserted
     */
    public void insertElement(T targetElement) {
        if (isEmpty()) {
            root = new BinaryTreeNode<>(targetElement);
        } else {
            root = insert(root, targetElement);
        }
        count++;
    }

    /**
     * Recursive helper method for inserting an element into the AVL tree.
     * Performs standard Binary Search Tree insert and then updates height and rebalances the tree.
     *
     * @param node    the current node being processed
     * @param element the element to be inserted
     * @return the root of the updated subtree after insertion and rebalancing
     */
    private BinaryTreeNode<T> insert(BinaryTreeNode<T> node, T element) {
        if (node == null) {
            return new BinaryTreeNode<>(element);
        }

        if (((Comparable<T>) element).compareTo(node.getElement()) < 0) {
            node.setLeftChild(insert(node.getLeftChild(), element));
        } else if (((Comparable<T>) element).compareTo(node.getElement()) > 0) {
            node.setRightChild(insert(node.getRightChild(), element));
        } else {
            // AVL trees can't have duplicates
            return node;
        }

        // Update height of the current node
        updateHeight(node);

        // Perform rebalance
        return rebalance(node);
    }

    /**
     * Removes the specified element from the AVL tree while maintaining the AVL property.
     *
     * @param targetElement the element to be removed
     * @return the removed element or null if the element is not found
     */
    public T removeElement(T targetElement) {
        if (isEmpty()) {
            return null; // element not found, maybe do an exception
        } else {
            BinaryTreeNode<T>[] result = new BinaryTreeNode[1]; // Stores the result from the deletion
            root = remove(root, targetElement, result);
            count--;
            return (result[0] != null) ? result[0].getElement() : null;
        }
    }

    /**
     * Recursive helper method for removing an element from the AVL tree.
     * Performs standard Binary Search Tree delete and then updates height and rebalances the tree.
     *
     * @param node    the current node being processed
     * @param element the element to be removed
     * @param result  an array to store the result of the deletion
     * @return the root of the updated subtree after deletion and rebalancing
     */
    private BinaryTreeNode<T> remove(BinaryTreeNode<T> node, T element, BinaryTreeNode<T>[] result) {
        if (node == null) {
            return null; // element not found
        }

        if (((Comparable<T>) element).compareTo(node.getElement()) < 0) {
            node.setLeftChild(remove(node.getLeftChild(), element, result));
        } else if (((Comparable<T>) element).compareTo(node.getElement()) > 0) {
            node.setRightChild(remove(node.getRightChild(), element, result));
        } else {
            // Element found, delete
            result[0] = node; // Store the node to be returned

            // Case 1: Node with only one child or no child
            if (node.getLeftChild() == null) {
                return node.getRightChild();
            } else if (node.getRightChild() == null) {
                return node.getLeftChild();
            }

            // Case 2: Node with two children, find the inorder successor (smallest node in the right subtree)
            BinaryTreeNode<T> successor = findMin(node.getRightChild());
            // Copy the inorder successor's data to this node
            node.setElement(successor.getElement());
            // Delete the inorder successor
            node.setRightChild(remove(node.getRightChild(), successor.getElement(), result));
        }

        // Update height of the current node
        updateHeight(node);

        // Perform rebalance
        return rebalance(node);
    }

    /**
     * Finds and returns the minimum element in the given subtree.
     *
     * @param node the root of the subtree to search
     * @return the node containing the minimum element in the subtree
     */
    private BinaryTreeNode<T> findMin(BinaryTreeNode<T> node) {
        while (node.getLeftChild() != null) {
            node = node.getLeftChild();
        }
        return node;
    }

    /**
     * Performs a right rotation on the given pivot node to maintain the AVL property.
     * This method assumes that the pivot and its left child are not null.
     * @param pivot the node at which the rotation is performed
     * @return the new root of the rotated subtree
     */
    private BinaryTreeNode<T> rotateRight(BinaryTreeNode<T> pivot) {
        if (pivot == null || pivot.getLeftChild() == null) {
            return pivot; // no rotation to be made
        }

        BinaryTreeNode<T> leftChild = pivot.getLeftChild();
        pivot.setLeftChild(leftChild.getRightChild()); // 1st rotation step
        leftChild.setRightChild(pivot); // 2nd rotation step
        updateHeight(pivot);
        updateHeight(leftChild);
        return leftChild;
    }

    /**
     * Performs a left rotation on the given pivot node to maintain the AVL property.
     * This method assumes that the pivot and its right child are not null.
     *
     * @param pivot the node at which the rotation is performed
     * @return the new root of the rotated subtree
     */
    private BinaryTreeNode<T> rotateLeft(BinaryTreeNode<T> pivot) {
        if (pivot == null || pivot.getRightChild() == null) {
            return pivot;
        }

        BinaryTreeNode<T> rightChild = pivot.getRightChild();
        pivot.setRightChild(rightChild.getLeftChild());
        rightChild.setLeftChild(pivot);
        updateHeight(pivot);
        updateHeight(rightChild);
        return rightChild;
    }

    /**
     * Rebalances the AVL tree rooted at the given pivot node to maintain the AVL property.
     * This method checks the balance factor and performs rotations if necessary.
     *
     * @param pivot the root of the subtree to be rebalanced
     * @return the new root of the rebalanced subtree
     */
    private BinaryTreeNode<T> rebalance(BinaryTreeNode<T> pivot) {
        updateHeight(pivot);
        int balance = getBalance(pivot);
        if (balance > 1) {
            BinaryTreeNode<T> rightChild = pivot.getRightChild();
            if (rightChild != null && height(rightChild.getRightChild()) > height(rightChild.getLeftChild())) {
                pivot = rotateLeft(pivot);
            } else {
                if (rightChild != null) {
                    rightChild.setRightChild(rotateRight(rightChild.getRightChild()));
                }
                pivot = rotateLeft(pivot);
            }
        } else if (balance < -1) {
            BinaryTreeNode<T> leftChild = pivot.getLeftChild();
            if (leftChild != null && height(leftChild.getLeftChild()) > height(leftChild.getRightChild())) {
                pivot = rotateRight(pivot);
            } else {
                if (leftChild != null) {
                    leftChild.setLeftChild(rotateLeft(leftChild.getLeftChild()));
                }
                pivot = rotateRight(pivot);
            }
        }
        return pivot;
    }
}
