package captureTheFlagGame.Model.gameCollection;

import Custom_CollectionsED.graphs.Graphs.Network;
import captureTheFlagGame.Exceptions.NoSuchIndexException;

public class GameNetwork extends Network<GameNode> {


    public GameNetwork(){
        super();
    }
    /**
     * constructor for network map.
     * @param initialSize initial size for the network for this map.
     */
    public GameNetwork(int initialSize) {
        super(initialSize);
    }

    /**
     * method to verify that all Vertices within the network are connected.
     *
     * @param connected array of booleans representing connections
     * @return true if all vertices are connected or false if not all vertices are connected.
     */
    protected boolean allVerticesConnected(boolean[] connected) {
        for (boolean vertexConnected : connected) {
            if (!vertexConnected) {
                return false;
            }
        }
        return true;
    }

    /** method to verify if two given vertices are connected.
     * @param vertex1 origin vertex
     * @param vertex2 goal vertex
     * @return true if the vertices are connected, false if otherwise.
     */
    public boolean areVerticesConnected(int vertex1, int vertex2) throws NoSuchIndexException {
        if (indexIsValid(vertex1) && indexIsValid(vertex2)) {
            return this.adjMatrix[vertex1][vertex2] != Double.POSITIVE_INFINITY || this.adjMatrix[vertex2][vertex1] != Double.POSITIVE_INFINITY;
            // if the connection value is not 0 and not infinite, then they're connected.
        }
        throw new NoSuchIndexException("Index not found");
    }

    /**
     * method to add singleEdge
     * @param vertex1 origin vertex
     * @param vertex2 goal vertex
     * @param weight weight from one to another edge
     */
    protected void addSingleEdge(int vertex1, int vertex2, double weight) {
        if (super.indexIsValid(vertex1) && super.indexIsValid(vertex2)) {
            this.adjMatrix[vertex1][vertex2] = weight;
        }
    }

    /**
     *
     * @return the adjacency Matrix that represents the entire map.
     */
    public double[][] getAdjMatrix() {
        return this.adjMatrix;
    }


    /**
     * This void method takes in two indexes, a weight and adds an edge ( a link in the adjacency Matrix )
     * with the respective weight.
     * @param index1 index from
     * @param index2 index to
     * @param weight weight of the edge to be added
     */
    protected void addEdge(int index1, int index2, double weight) {
        if (super.indexIsValid(index1) && super.indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = weight;
            this.adjMatrix[index2][index1] = weight;
        }
    }

    /**
     * checks if a directed matrix is fully connected
     * @return false if it isn't.
     */
    public boolean isFullyConnectedDirected() { //directed
       double[][] m =this.adjMatrix;
        for (int i = 0; i < m.length; i++) { //iterate over rows
            for (int j = 0; j < m[i].length; j++) //iterate over columns
                if (i != j && m[i][j] == Double.POSITIVE_INFINITY) { //if not in main diag. and not connected
                    return false;
                }
        }
        return true;
    }


    /**
     * checks if a undirected matrix is fully connected
     * @return false if it isn't.
     */
    public boolean isFullyConnectedUndirected() { //undirected
        double[][] m = this.adjMatrix;
        for (int i = 0; i < m.length; i++) { //iterate over rows
            for (int j = 0; j < m[i].length; j++) //iterate over columns
                if (i != j && m[i][j] == Double.POSITIVE_INFINITY && m[j][i] == Double.POSITIVE_INFINITY) { //if not in main diag. and not connected
                    return false;
                }
        }
        return true;
    }

    /**
     * getter for vertices
     * @return all vertices in map as gamenode array
     */
    public GameNode[] getVertices(){
        return this.vertices;
    }




}


