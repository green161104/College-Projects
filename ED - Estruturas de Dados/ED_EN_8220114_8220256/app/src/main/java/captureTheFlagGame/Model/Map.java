package captureTheFlagGame.Model;

import captureTheFlagGame.Exceptions.NoSuchIndexException;
import captureTheFlagGame.Interfaces.MapInterface;
import captureTheFlagGame.Model.gameCollection.GameList;
import captureTheFlagGame.Model.gameCollection.GameNetwork;
import captureTheFlagGame.Model.gameCollection.GameNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Map extends GameNetwork implements MapInterface {

    private int density = 0;
    private int maxVertices;

    /**
     * construcor for map
     */
    public Map(int maxVertices, double[][] adjMatrix, GameNode[] nodes) {
        super();
        this.vertices = new GameNode[maxVertices];
        this.numVertices = maxVertices;
        this.maxVertices = maxVertices;
        this.adjMatrix = adjMatrix;
        this.vertices = nodes;
    }

    /**
     * map constructor for when map is being generated
     * @param vertices vertices in map
     * @param density density of links between vertices in map ( in percentage
     * @param bidirectional boolean flag that decides if the map will be bidirectional or not
     */
    public Map(int vertices, int density, boolean bidirectional) {
        super(vertices);
        this.density = density;
        this.maxVertices = vertices;
        this.vertices = new GameNode[maxVertices];
        this.isDirected = !bidirectional;
    }

    /**
     * constructor for map when being imported
     * @param vertices vertices in map
     * @param adjMatrix adjacency matrix of the map
     */
    public Map(GameNode[] vertices, double[][] adjMatrix) {
        this.vertices = vertices;
        this.adjMatrix = adjMatrix;
    }


    /**
     * @return density for this map
     */
    public int getDensity(){
        return this.density;
    }

    /**
     * method used to create gameMap with values from 1-15 when it comes to weight in the edges.
     */
    @Override
    public void createMap() throws NoSuchIndexException {
        Random random = new Random();

        // Create vertices
        for (int i = 0; i < maxVertices; i++) {
            GameNode node = new GameNode(i);
            this.addVertex(node);
        }

        int maxEdges = (int) Math.round((maxVertices * (maxVertices - 1)) * (density / 100.0));
        int numEdges = 0;

        for (int i = 0; i < maxEdges && numEdges <= maxEdges; i++) {
            int vertex1 = random.nextInt(maxVertices);
            int vertex2 = random.nextInt(maxVertices);

            if (vertex1 != vertex2 && !areVerticesConnected(vertex1, vertex2)) {
                if (isDirected) {
                    this.addSingleEdge(vertex1, vertex2, (random.nextInt(15)) + 1);
                    numEdges++;
                    this.addSingleEdge(vertex2, random.nextInt(maxVertices), (random.nextInt(15)) + 1);
                    numEdges++;
                }

                if (!isDirected) {
                    this.addEdge(this.vertices[vertex1], this.vertices[vertex2], (random.nextInt(15)) + 1);
                    numEdges++;
                }
            }
        }
    }

    //resolvido

    /**
     * method that takes in a vertex and returns the neighbors of said vertex.
     * starts a list, checks if there's a connection between the two vertices,
     * if that vertex is not occupied or the end vertex, adds it to the neighbors list.
     * @param vertex
     * @param endIndex
     * @return
     * @throws NoSuchIndexException
     */
    public int[] getNeighbors(int vertex, int endIndex) throws NoSuchIndexException {
        // Check if the vertex index is valid
        if (!indexIsValid(vertex)) {
            throw new NoSuchIndexException("Invalid vertex index");
        }
        // Initialize a list to store neighbor indices
        GameList neighborsList = new GameList();

        // Iterate through the vertices to find neighbors
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[vertex][i] != Double.POSITIVE_INFINITY) {
                if(vertices[i].isOccupied() && i != endIndex)
                    continue;
                // If there is an edge, add the neighbor index to the list
                neighborsList.addToRear(vertices[i]);
            }
        }

        // Convert the list to an array and return
        return neighborsList.toArray();
    }


    /**
     * void method to print the adjacency matrix representing the map
     */
    public void printMap() {
        System.out.println("Here's the adjacency matrix: ");

        int numCols = adjMatrix[0].length;

        for (double[] doubles : adjMatrix) {
            for (int j = 0; j < numCols; j++) {
                if (doubles[j] == Double.POSITIVE_INFINITY) {
                    System.out.print("0");
                } else {
                    System.out.print(doubles[j]);
                }
                System.out.print(", ");
            }
            System.out.println();
        }
    }

    /**
     * Exports the map by storing the adjacency matrix in a file located in app/src/maps
     * with the provided name
     * @param mapName the name to be given to the map
     * @throws IOException if there is an error writing it to file
     */
    @Override
    public void exportMap(String mapName) throws IOException {
        String filePath = "app/src/maps/" + mapName + ".txt";

        try (FileWriter writer = new FileWriter(filePath)) {

            for (double[] matrix : this.adjMatrix) {
                for (double v : matrix) {
                    writer.write(v + " ");
                }
                writer.write("\n");
            }
        }
    }

    /**
     * Returns the weight of the edge connecting two vertices
     * @param from first vertex
     * @param to second vertex
     * @return the weight of that edge
     */
    public double getWeight(int from, int to) {
        if(!this.getVertices()[to].isOccupied()) {
            return this.getAdjMatrix()[from][to];
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the weight of the edge connecting two vertices
     * keeping the heavyChamp logic in mind.
     * @param from first vertex
     * @param to second vertex
     * @return the weight of that edge
     */
    public double getWeightForHeavyChamp(int from, int to) {
        if(!this.getVertices()[to].isOccupied()) {
            return this.getAdjMatrix()[from][to];
        }
        return Double.NEGATIVE_INFINITY;
    }
}
