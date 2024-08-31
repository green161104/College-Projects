package Custom_CollectionsED.trees.binary.heap;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.trees.binary.binaryArrayTree.ArrayBinaryTree;
import Custom_CollectionsED.trees.binary.interfaces.HeapADT;

/**
 * Provides an array implementation for a minheap
 *
 * @param <T> data type to be stored in the structure, must be comparable
 */
public class ArrayHeap<T> extends ArrayBinaryTree<T> implements HeapADT<T> {
    public ArrayHeap() {
        super();
    }

    /**
     * Adds the specified element to this heap in the appropriate
     * position according to its key value.
     * Note that equal elements are added to the right.
     *
     * @param obj the element to be added to this heap
     */
    public void addElement(T obj) {
        if (count == tree.length) {
            expandCapacity();
        }
        tree[count] = obj;
        count++;

        if (count > 1) {
            heapifyAdd();
        }
    }

    /**
     * Reorders this heap to maintain the ordering property after
     * adding a node.
     */
    private void heapifyAdd() {
        T temp;
        int next = count - 1;
        temp = tree[next];
        while ((next != 0) && (((Comparable<T>) temp).compareTo(tree[(next - 1) / 2]) < 0)) {
            tree[next] = tree[(next - 1) / 2];
            next = (next - 1) / 2;
        }
        tree[next] = temp;
    }

    /**
     * Remove the element with the lowest value in this heap and
     * returns a reference to it.
     * Throws an EmptyCollectionException if the heap is empty.
     *
     * @return a reference to the element with the
     * lowest value in this head
     * @throws EmptyCollectionException if an empty collection
     *                                  exception occurs
     */
    public T removeMin(){
        if (isEmpty())
            throw new RuntimeException(new EmptyCollectionException("no elements"));
        T minElement = tree[0];
        tree[0] = tree[count - 1];
        heapifyRemove();
        count--;
        return minElement;
    }

    @Override
    public T findMin() {
        return this.getRoot();
    }

    /**
     * Reorders this heap to maintain the ordering property.
     */
    private void heapifyRemove() {
        T temp;
        int node = 0;
        int left = 1;
        int right = 2;
        int next = getNext(left, right);
        temp = tree[node];
        while ((next < count) && (((Comparable<T>) tree[next]).compareTo
                (temp) < 0)) {
            tree[node] = tree[next];
            node = next;
            left = 2 * node + 1;
            right = 2 * (node + 1);
            next = getNext(left, right);
        }
        tree[node] = temp;
    }

    private int getNext(int left, int right) {
        int next;
        if ((tree[left] == null) && (tree[right] == null))
            next = count;
        else if (tree[left] == null)
            next = right;
        else if (tree[right] == null)
            next = left;
        else if (((Comparable<T>) tree[left]).compareTo(tree[right]) < 0)
            next = left;
        else
            next = right;
        return next;
    }
}
