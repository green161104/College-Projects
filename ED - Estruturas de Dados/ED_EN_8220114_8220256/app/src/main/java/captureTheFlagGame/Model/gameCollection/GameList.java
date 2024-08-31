package captureTheFlagGame.Model.gameCollection;


import Custom_CollectionsED.list.singlyLinkedLists.SinglyLinkedUnorderedList;
import Custom_CollectionsED.nodes.LinearNode;

public class GameList extends SinglyLinkedUnorderedList<GameNode> {


    //fixed

    /**
     *  iterates through the list of nodes and adds them to nodes array.
     *  This is useful for the algorithms ( receiving the indexes as an array )
     * @return an array of ints with all the indexes in the map
     */
    public int[] toArray(){
        int[] nodes = new int[this.size()];
            LinearNode<GameNode> currentNode = this.head;
            int i = 0;
            while(currentNode != null){
                nodes[i] = currentNode.getElement().getIndex();
                i++;
                currentNode = currentNode.getNext();
            }
        return nodes;
    }
}
