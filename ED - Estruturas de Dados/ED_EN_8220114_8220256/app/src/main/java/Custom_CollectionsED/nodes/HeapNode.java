package Custom_CollectionsED.nodes;

import Custom_CollectionsED.nodes.BinaryTreeNode;

public class HeapNode<T> extends BinaryTreeNode<T> {
    private HeapNode<T> parent;

    /**
     * Creates a new heap node with the specified data.
     *
     * @param element the data to be contained within
     *            the new heap node
     */
   public HeapNode(T element) {
        super(element);
        parent = null;
    }

    public HeapNode<T> getParent(){
       return this.parent;
    }

    public void setParent(HeapNode<T> parent){

       this.parent = parent;
    }
}
