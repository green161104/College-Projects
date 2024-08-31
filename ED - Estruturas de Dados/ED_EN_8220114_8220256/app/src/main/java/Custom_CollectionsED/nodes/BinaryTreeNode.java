package Custom_CollectionsED.nodes;

/**
 * This class serves as a representation of a node present in a binary tree
 * data structure.
 * @param <T> the data type to be stored in each node
 */
public class BinaryTreeNode<T> {
    /**
     * Element stored in each node
     */
    private T element;

    /**
     * Left and right children of each node
     */
    private BinaryTreeNode<T> leftChild, rightChild;
    private int height;

    /**
     * Default constructor for a binary tree node
     */
    public BinaryTreeNode(){
        this.element = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * Creates a new node with the assigned element
     * @param element value to be stored within the node
     */
    public BinaryTreeNode(T element) {
        this.element = element;
        this.rightChild = null;
        this.leftChild = null;
    }

    /**
     * Retrieves the value stored in the node
     * @return the value stored within the node
     */
    public T getElement() {
        return element;
    }

    /**
     * Retrieves the node corresponding to the left child of this node
     * @return the left child of this node
     */
    public BinaryTreeNode<T> getLeftChild() {
        return leftChild;
    }

    /**
     * Retrieves the node corresponding to the right child of this node
     * @return the right child of this node
     */
    public BinaryTreeNode<T> getRightChild() {
        return rightChild;
    }

    /**
     * Assigns a given value to the node
     * @param element value to be assigned to the node
     */
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * @return the height of this node within the tree
     */
    public int getHeight() {
        return this.height;
    }

    /**
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    public void setLeftChild(BinaryTreeNode<T> leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(BinaryTreeNode<T> rightChild) {
        this.rightChild = rightChild;
    }

    public int numChildren() {
        /*
        int children = 0;
        if (leftChild != null)
            children = 1 + leftChild.numChildren();
        if (rightChild != null)
            children = children + 1 + rightChild.numChildren();
        return children; acumula os contadores, redundante*/

        int leftCount = (leftChild != null) ? leftChild.numChildren() + 1 : 0;
        int rightCount = (rightChild != null) ? rightChild.numChildren() + 1 : 0;

        return leftCount + rightCount;
        //dois contadores separados
    }

}


