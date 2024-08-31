package Custom_CollectionsED.trees.binary.heap;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.nodes.HeapNode;
import Custom_CollectionsED.trees.binary.binaryLinkedTree.LinkedBinaryTree;
import Custom_CollectionsED.trees.binary.interfaces.HeapADT;

/**
 * Provides a linked list implementation for a min heap
 *
 * @param <T> data type to store in this collection must be comparable
 */
public class LinkedHeap<T> extends LinkedBinaryTree<T> implements HeapADT<T> {
    public HeapNode<T> lastNode;

    public LinkedHeap() {
        super();
    }

    /**
     * Adds the specified element to this heap in the
     * appropriate position according to its key value
     * Note that equal elements are added to the right.
     *
     * @param obj the element to be added to this head
     */
    @Override
    public void addElement(T obj) {
        HeapNode<T> node = new HeapNode<T>(obj);
        if (root == null)
            root = node;
        else {
            HeapNode<T> next_parent = getNextParentAdd();
            if (next_parent.getLeftChild() == null)
                next_parent.setLeftChild(node);
            else
                next_parent.setRightChild(node);
            node.setParent(next_parent);
        }
        lastNode = node;
        count++;
        if (count > 1)
            heapifyAdd();
    }

    /**
     * Returns the node that will be the parent of the new node
     *
     * @return the node that will be a parent of the new node
     */
    private HeapNode<T> getNextParentAdd() {
        HeapNode<T> result = lastNode;
        while ((result != root) &&
                (result.getParent().getLeftChild() != result))
            result = result.getParent();
        if (result != root)
            if (result.getParent().getRightChild() == null)
                result = result.getParent();
            else {
                result = (HeapNode<T>) result.getParent().getRightChild();
                while (result.getLeftChild() != null)
                    result = (HeapNode<T>) result.getLeftChild();
            }
        else
            while (result.getLeftChild() != null)
                result = (HeapNode<T>) result.getLeftChild();
        return result;
    }

    /**
     * Reorders this heap after adding a node.
     */
    private void heapifyAdd() {
        T temp;
        HeapNode<T> next = lastNode;
        temp = next.getElement();
        while ((next != root) && (((Comparable<T>) temp).compareTo(next.getParent().getElement()) < 0)) {
            next.setElement(next.getParent().getElement());
            next = next.getParent();
        }
        next.setElement(temp);
    }

    /**
     * Remove the element with the lowest value in this heap and
     * returns a reference to it.
     * Throws an EmptyCollectionException if the heap is empty.
     *
     * @return the element with the lowest value in this heap
     * @throws EmptyCollectionException if an empty collection
     *                                  exception occurs
     */
    public T removeMin() {
        if (isEmpty())
            throw new RuntimeException(new EmptyCollectionException("Empty Heap"));
        T minElement = root.getElement();
        if (count == 1) {
            root = null;
            lastNode = null;
        } else {

            HeapNode<T> next_last = getNewLastNode();
            if (lastNode.getParent().getLeftChild() == lastNode)
                lastNode.getParent().setLeftChild(null);
            else
                lastNode.getParent().setRightChild(null);
            root.setElement(lastNode.getElement());
            lastNode = next_last;
            heapifyRemove();
        }
        count--;
        return minElement;
    }

    @Override
    public T findMin() {
        return this.getRoot();
    }

    /**
     * Returns the node that will be the new last node after
     * a remove.
     *
     * @return the node that will be the new last node after
     * a remove
     */
    private HeapNode<T> getNewLastNode() {
        HeapNode<T> result = lastNode;
        while ((result != root) && (result.getParent().getLeftChild() == result))
            result = result.getParent();
        if (result != root)
            result = (HeapNode<T>) result.getParent().getLeftChild();
        while (result.getRightChild() != null)
            result = (HeapNode<T>) result.getRightChild();
        return result;
    }

    /**
     * Reorders this heap after removing the root element.
     */
    private void heapifyRemove() {
        T temp;
        HeapNode<T> node = (HeapNode<T>) root;
        HeapNode<T> left = (HeapNode<T>) node.getLeftChild();
        HeapNode<T> right = (HeapNode<T>) node.getRightChild();
        HeapNode<T> next;
        if ((left == null) && (right == null))
            next = null;
        else if (left == null)
            next = right;
        else if (right == null)
            next = left;
        else if (((Comparable) left.getElement()).compareTo(right.getElement()) < 0)
            next = left;
        else
            next = right;
        temp = node.getElement();
        while ((next != null) && (((Comparable) next.getElement()).compareTo(temp) < 0)) {
            node.setElement(next.getElement());
            node = next;
            left = (HeapNode<T>) node.getLeftChild();
            right = (HeapNode<T>) node.getRightChild();
            if ((left == null) && (right == null))
                next = null;
            else if (left == null)
                next = right;
            else if (right == null)
                next = left;
            else if (((Comparable) left.getElement()).compareTo(right.getElement()) < 0)
                next = left;
            else
                next = right;
        }
        node.setElement(temp);
    }
}
