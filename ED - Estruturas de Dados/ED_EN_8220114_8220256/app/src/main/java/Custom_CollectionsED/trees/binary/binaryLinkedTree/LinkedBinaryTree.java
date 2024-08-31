package Custom_CollectionsED.trees.binary.binaryLinkedTree;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.list.arrayLists.ArrayUnorderedList;
import Custom_CollectionsED.nodes.BinaryTreeNode;
import Custom_CollectionsED.queue.LinkedQueue;
import Custom_CollectionsED.trees.binary.interfaces.BinaryTreeADT;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * LinkedBinaryTree represents a binary tree where each node has at most two children,
 * referred to as the left and right subtrees. It implements the BinaryTreeADT interface.
 *
 * @param <T> the type of elements stored in the tree
 */
public abstract class LinkedBinaryTree<T> implements BinaryTreeADT<T> {
    protected int count;
    protected BinaryTreeNode<T> root;

    /**
     * Creates an empty binary tree.
     */
    public LinkedBinaryTree() {
        count = 0;
        root = null;
    }

    /**
     * Creates a binary tree with the specified element as the root.
     *
     * @param rootElement value to assign to the root element
     */
    public LinkedBinaryTree(T rootElement) {
        count = 1;
        root = new BinaryTreeNode<>(rootElement);
    }

    /**
     * Returns the element at the root of the tree.
     *
     * @return the element at the root
     */
    @Override
    public T getRoot() {
        return this.root.getElement();
    }

    /**
     * Checks if the tree is empty.
     *
     * @return true if the tree is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in the tree.
     *
     * @return the number of elements in the tree
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Checks if the tree contains the specified element.
     *
     * @param targetElement the element to search for
     * @return true if the element is found, false otherwise
     */
    @Override
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
            // If find() doesn't throw an exception, the element is found
            return true;
        } catch (NoSuchElementException e) {
            // If find() throws an exception, the element is not found
            return false;
        }
    }

    /**
     * Finds and returns the specified element in the tree.
     *
     * @param targetElement the element to search for
     * @return the found element
     * @throws NoSuchElementException if the element is not found
     */
    @Override
    public T find(T targetElement) {
        BinaryTreeNode<T> current = findAgain(targetElement, root);
        if (current == null) {
            throw new NoSuchElementException("Element not found");
        }

        return current.getElement();
    }

    /**
     * Recursive helper method to find the specified element in the tree.
     * Recursively searches both subtrees in search for the element.
     *
     * @param targetElement the element to search for
     * @param current       the current node being checked
     * @return the node containing the element, or null if not found
     */
    private BinaryTreeNode<T> findAgain(T targetElement, BinaryTreeNode<T> current) {
        if (current == null) return null;

        if (((Comparable<T>)current.getElement()).compareTo(targetElement) == 0) return current; //checks if it's the current node

        BinaryTreeNode<T> temp = findAgain(targetElement, current.getLeftChild()); //searches the left subtree

        if (temp == null)
            temp = findAgain(targetElement, current.getRightChild()); //searches in the right subtree
        return temp; // if found, returns the element, otherwise null
    }

    /**
     * Performs an inorder traversal of the tree and populates a list with elements.
     *
     * @param node     the current node being processed
     * @param tempList the list to populate with elements
     */
    protected void inorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            inorder(node.getLeftChild(), tempList); // searches left side
            tempList.addToRear(node.getElement()); // processes current node
            inorder(node.getRightChild(), tempList); // searches right side
        }
    }

    /**
     * Performs a preorder traversal of the tree and populates a list with elements.
     *
     * @param node     the current node being processed
     * @param tempList the list to populate with elements
     */
    protected void preOrder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            tempList.addToRear(node.getElement()); // Process the current node
            preOrder(node.getLeftChild(), tempList); // Recursively process left subtree
            preOrder(node.getRightChild(), tempList); // Recursively process right subtree
        }
    }

    /**
     * Performs a postorder traversal of the tree and populates a list with elements.
     *
     * @param node     the current node being processed
     * @param tempList the list to populate with elements
     */
    protected void postOrder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            postOrder(node.getLeftChild(), tempList); // Recursively process left subtree
            postOrder(node.getRightChild(), tempList); // Recursively process right subtree
            tempList.addToRear(node.getElement()); // Process the current node
        }
    }

    /**
     * Returns an iterator for inorder traversal of the tree.
     *
     * @return an iterator for inorder traversal
     */
    @Override
    public Iterator iteratorInOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();
        inorder(root, tempList);
        return tempList.iterator();
    }

    /**
     * Returns an iterator for preorder traversal of the tree.
     *
     * @return an iterator for preorder traversal
     */
    @Override
    public Iterator iteratorPreOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();
        preOrder(root, tempList);
        return tempList.iterator();
    }

    /**
     * Returns an iterator for postorder traversal of the tree.
     *
     * @return an iterator for postorder traversal
     */
    @Override
    public Iterator iteratorPostOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();
        postOrder(root, tempList);
        return tempList.iterator();
    }

    /**
     * Returns an iterator for level-order traversal of the tree.
     *
     * @return an iterator for level-order traversal
     */
    @Override
    public Iterator<T> iteratorLevelOrder() {
        ArrayUnorderedList<T> results = new ArrayUnorderedList<>();
        if (root != null) {
            LinkedQueue<BinaryTreeNode<T>> nodes = new LinkedQueue<>(); // create the nodes queue
            nodes.enqueue(root);

            while (!nodes.isEmpty()) {
                BinaryTreeNode<T> current = null;
                try {
                    current = nodes.dequeue();
                } catch (EmptyCollectionException e) {
                    throw new RuntimeException(e);
                }
                results.addToRear(current.getElement()); // Process the current node

                if (current.getLeftChild() != null) {
                    nodes.enqueue(current.getLeftChild());
                }

                if (current.getRightChild() != null) {
                    nodes.enqueue(current.getRightChild());
                }
            }
        }

        return results.iterator();
    }

    /**
     * Displays the tree in a structured format, indicating levels and node positions.
     */
    public void display() {
        Iterator<T> levelOrderIterator = iteratorLevelOrder();

        int level = 0;
        int levelNodeCount = 1;
        int currentNodeCount = 0;

        while (levelOrderIterator.hasNext()) {
            T current = levelOrderIterator.next();

            for (int i = 0; i < levelNodeCount; i++) {
                System.out.print("   ");
            }
            System.out.print(current);

            currentNodeCount++;

            if (currentNodeCount == levelNodeCount) {
                System.out.println(); // Move to the next level
                level++;
                levelNodeCount *= 2;
                currentNodeCount = 0;
            } else {
                System.out.print("   ");
            }
        }
        System.out.println(); // Add a newline at the end for better readability
    }
}




