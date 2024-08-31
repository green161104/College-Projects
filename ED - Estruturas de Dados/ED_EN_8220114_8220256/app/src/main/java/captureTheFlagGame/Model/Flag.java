package captureTheFlagGame.Model;

public class Flag {

    private int home;

    /**
     * Constructor of flag
     * @param homeNode index where the flag is going to be planted
     */
    public Flag(int homeNode){
        this.home = homeNode;
    }

    /**
     * setter for flag index
     * @param node index of node
     */
    public void setHome(int node){
        this.home = node;
    }

    /**
     * getter for flag home index
     * @return index of flag
     */
    public int getHome(){
        return this.home;
    }
}
