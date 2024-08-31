package captureTheFlagGame.Model.gameCollection;

public class GameNode {
    private boolean isOccupied;
    private int index;

    /**
     * Constructor for GameNode which is the node for this game in specific
     * @param index index corresponding to this node
     */
    public GameNode(int index){
        this.index = index;
        this.isOccupied = false;
    }

    /**
     * setter for index of node
     * @param index
     */
    public void setIndex(int index){
        this.index = index;
    }

    /**
     *
     * @return index of node as int
     */
    public int getIndex(){
        return index;
    }

    /**
     *  sets the node to occupied or not
     * @param occupied
     */
    public void setOccupied(boolean occupied){
        this.isOccupied = occupied;
    }

    /**
     * gets occupied state of one node.
     * @return
     */
    public boolean isOccupied(){
        return isOccupied;
    }

}
