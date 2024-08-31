package Custom_CollectionsED.trees.binary.binaryArrayTree;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.list.arrayLists.ArrayUnorderedList;
import Custom_CollectionsED.queue.LinkedQueue;
import Custom_CollectionsED.trees.binary.interfaces.BinaryTreeADT;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is an implementation of a Binary Tree data structure, which
 * uses arrays as a support. It is an abstract class, which then serves as a support for
 * inheritance.
 * @param <T> the data type to be stored in the collection, must be comparable
 */
public abstract class ArrayBinaryTree<T> implements BinaryTreeADT<T> {

    /**
     * Default capacity of the array
     */
    private final int DEFAULT_CAPACITY = 50;
    /**
     * Counter to keep track of the size of the tree
     */
    protected int count;
    /**
     * Array to hold the structure of the tree
     */
    protected T[] tree;

    /**
     * Default constructor for the tree, uses the default array capacity to size the array,
     * and starts the count at zero.
     */
    public ArrayBinaryTree() {
        tree = (T[]) new Object[DEFAULT_CAPACITY];
        count = 0;
    }

    /**
     * Custom constructor for the tree, which takes in a value
     * to assign to the root element (first spot of the array), and a custom capacity
     * @param rootElement value to be stored in the root of the tree
     * @param capacity custom capacity for the tree
     */
    public ArrayBinaryTree(T rootElement, int capacity) {
        count = 1;
        tree = (T[]) new Object[capacity];
        tree[0] = rootElement;

    }

    /**
     * Expands the capacity of the tree, by duplicating its current length
     */
    protected void expandCapacity() {
        T[] newArray = (T[]) new Object[tree.length * 2];
        System.arraycopy(tree, 0, newArray, 0, count);
        tree = newArray;
    }

    /**
     * Retrieves the root of the tree, which is the value stored in the first slot of the array
     * @return the root element of the tree
     */
    @Override
    public T getRoot() {
        return tree[0];
    }



    /**
     * Checks if the tree is empty, returning true if so, false otherwise
     * @return
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * @return the size of the tree, as in, the amount of elements stored.
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Makes use of the find method to look for a specific element in the tree.
     * <li>if said element is found, the method returns true</li>
     * <li>if it isn't, the find() method throws an exception,
     * which is then caught in the contains() method, causing it to return false.</li>
     * @param targetElement the element being sought in the tree.
     * @return whether the element exists (true) or not (false)
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
     * Seeks an element in the array, returning its respective reference if found,
     * and throws an exception otherwise.
     * @param targetElement the element being sought in the tree
     * @throws NoSuchElementException in case the element being sought after does not exist
     * @return the reference to the element being searched for
     */
    @Override
    public T find(T targetElement) {
        T tobeFound = null;
        boolean found = false;
        for (int i = 0; i < count && !found; i++)
            if (((Comparable<T>)targetElement).compareTo(tree[i]) == 0) {
                found = true;
                tobeFound = tree[i];
            }
        if (!found)
            throw new NoSuchElementException("binary tree");
        return tobeFound;
    }

    /**
     * Serves as a support to an inorder iterator, and stores the elements in an ArrayList
     * in inorder
     * @param node starting node for the sort
     * @param templist list where the elements are to be stored
     */
    protected void inorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length)
            if (tree[node] != null) {
                inorder(node * 2 + 1, templist);
                templist.addToRear(tree[node]);
                inorder((node + 1) * 2, templist);
            }
    }

    /**
     * Serves as a support to a preorder iterator, and stores the elements in an ArrayList
     * in preorder
     * @param node starting node for the sort
     * @param tempList list where the elements are to be stored
     */
    protected void preOrder(int node, ArrayUnorderedList<T> tempList) {
        if (node < tree.length && tree[node] != null) {
            tempList.addToRear(tree[node]); // Process the current node
            preOrder(node * 2 + 1, tempList); // Recursively process left subtree
            preOrder((node + 1) * 2, tempList); // Recursively process right subtree
        }
    }

    /**
     * Serves as a support to a postorder iterator, and stores the elements in an ArrayList
     * in postorder
     * @param node starting node for the sort
     * @param tempList list where the elements are to be stored
     */
    protected void postOrder(int node, ArrayUnorderedList<T> tempList) {
        if (node < tree.length && tree[node] != null) {
            postOrder(node * 2 + 1, tempList); // Recursively process left subtree
            postOrder((node + 1) * 2, tempList); // Recursively process right subtree
            tempList.addToRear(tree[node]); // Process the current node
        }
    }

    /**
     * Provides an iterator for the tree, by sorting it inorder, and retrieving the
     * iterator present in the ArrayList implementation
     * @return an iterator to iterate over this tree in order
     */
    @Override
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<T>();
        inorder(0, templist);
        return templist.iterator();
    }

    /**
     * Provides an iterator for the tree, by sorting it preorder, and retrieving the
     * iterator present in the ArrayList implementation
     * @return an iterator to iterate over this tree in pre-order
     */
    @Override
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        preOrder(0, tempList);
        return tempList.iterator();
    }

    /**
     * Provides an iterator for the tree, by sorting it post order, and retrieving the
     * iterator present in the ArrayList implementation
     * @return an iterator to iterate over this tree in post order
     */
    @Override
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<T>();
        postOrder(0, tempList);
        return tempList.iterator();
    }

    /**
     * Provides an iterator for the tree, by sorting it level order, and retrieving the
     * iterator present in the ArrayList implementation
     * @return an iterator to iterate over this tree in level order
     */
    @Override
    public Iterator<T> iteratorLevelOrder() {
        ArrayUnorderedList<T> results = new ArrayUnorderedList<>();
        LinkedQueue<Integer> nodes = new LinkedQueue<>();

        if (tree.length > 0 && tree[0] != null) {
            nodes.enqueue(0);  // Start with the root index

            while (!nodes.isEmpty()) {
                int current = 0;
                try {
                    current = nodes.dequeue();
                } catch (EmptyCollectionException e) {
                    throw new RuntimeException(e);
                }
                results.addToRear(tree[current]);  // Process the current node

                int leftChild = current * 2 + 1;
                int rightChild = (current + 1) * 2;

                // Enqueue left child if not out of bounds and not null
                if (leftChild < tree.length && tree[leftChild] != null) {
                    nodes.enqueue(leftChild);
                }

                // Enqueue right child if not out of bounds and not null
                if (rightChild < tree.length && tree[rightChild] != null) {
                    nodes.enqueue(rightChild);
                }
            }
        }

        return results.iterator();
    }

    /**
     * Method for the purpose of displaying the list, makes use of the level order iterator,
     * so that the list is presented true to its real form, level by level.
     */
    public void display() {
        Iterator<T> levelOrderIterator = iteratorLevelOrder();

        int level = 0;
        int levelNodeCount = 1;
        int currentNodeCount = 0;

        while (levelOrderIterator.hasNext()) {
            T current = levelOrderIterator.next();
            System.out.print(current + "   ");

            currentNodeCount++;

            if (currentNodeCount == levelNodeCount) {
                System.out.println(); // Move to the next level
                level++;
                levelNodeCount *= 2;
                currentNodeCount = 0;
            }
        }
    }

}
