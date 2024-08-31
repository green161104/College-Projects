package Custom_CollectionsED.graphs.Graphs;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.graphs.Interfaces.GraphADT;
import Custom_CollectionsED.list.arrayLists.ArrayUnorderedList;
import Custom_CollectionsED.queue.LinkedQueue;
import Custom_CollectionsED.stack.LinkedStack;

import java.util.Iterator;

public class Graph<T> implements GraphADT<T> {

    protected final int DEFAULT_CAPACITY = 10;
    protected final int EXPAND_FACTOR = 2;
    protected int numVertices; // number of vertices in the graph
    protected boolean[][] adjMatrix; // adjacency matrix
    protected T[] vertices; // values of vertices
    protected boolean isDirected;

    /**
     * Creates an empty graph.
     */
    public Graph() {
        numVertices = 0;
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) new Object[DEFAULT_CAPACITY];
    }


    /**
     * Adds a vertex to the graph, expanding the capacity of the graph
     * if necessary. It also associates an object with the vertex.
     *
     * @param vertex the vertex to add to the graph
     */
    @Override
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();
        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
        numVertices++;
    }

    /**
     * Expands the graph's size, by essentially doubling its capacity anytime that it is necessary
     */
    protected void expandCapacity() {
        T[] newVertices = (T[]) new Object[this.vertices.length * EXPAND_FACTOR];
        System.arraycopy(this.vertices, 0, newVertices, 0, this.vertices.length);
        boolean[][] newAdjMatrix = new boolean[this.adjMatrix.length * EXPAND_FACTOR][this.adjMatrix.length * EXPAND_FACTOR];

        for (int j = 0; j < this.adjMatrix.length; ++j) {
            System.arraycopy(this.adjMatrix[j], 0, newAdjMatrix[j], 0, this.adjMatrix.length);
        }

        this.vertices = newVertices;
        this.adjMatrix = newAdjMatrix;
    }

    /**
     * Retrieves the index of a specified vertex
     *
     * @param vertexToBeFound vertex whose index is to be sought after
     * @return the index of the specified vertex, or -1 if it is not found.
     */
    public int getIndex(T vertexToBeFound) {
        for (int i = 0; i < this.numVertices; i++) {
            if (vertices[i].equals(vertexToBeFound)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifies whether the index passed is valid
     *
     * @param index to verify
     * @return true if it is valid; false otherwise.
     */
    public boolean indexIsValid(int index) {
        return index <= numVertices && index >= 0;
    }

    /**
     * Removes a vertex from the graph, by updating the vertex array, along with the adjacency matrix
     *
     * @param vertex the vertex to be removed from this graph
     */
    @Override
    public void removeVertex(T vertex) {
        int index = getIndex(vertex);

        if (indexIsValid(index)) {
            // Remove edges incident to the vertex
            for (int i = 0; i < numVertices; i++) {
                adjMatrix[index][i] = false;
                adjMatrix[i][index] = false;
            }

            // Shift vertices to fill the gap
            for (int i = index; i < numVertices - 1; i++) {
                vertices[i] = vertices[i + 1];
            }

            // Shift the adjacency matrix
            for (int i = index; i < numVertices - 1; i++) {
                for (int j = 0; j < numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i + 1][j];
                }
            }

            for (int j = index; j < numVertices - 1; j++) {
                for (int i = 0; i < numVertices; i++) {
                    adjMatrix[i][j] = adjMatrix[i][j + 1];
                }
            }

            // Update the number of vertices
            numVertices--;
        }
    }

    /**
     * Helper method which adds an edge between two vertices, given their indexes
     *
     * @param index1 index of the first vertex
     * @param index2 index of the second vertex
     */
    private void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            adjMatrix[index2][index1] = true;
        }
    }

    /**
     * Adds an edge between two vertices, given their value
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Removes an edge between two vertices, given their values
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = false;
            adjMatrix[index2][index1] = false;
        }
    }


    /**
     * Returns an iterator for a Breadth-First Search (BFS) traversal starting from the specified vertex.
     * The BFS algorithm visits vertices level by level, exploring all neighbors of a vertex before moving on
     * to the next level.
     *
     * @param startVertex the index of the starting vertex for BFS traversal
     * @return an iterator that provides vertices in the order they are visited during BFS traversal
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        if (!indexIsValid(startIndex))
            return resultList.iterator();
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;
        while (!traversalQueue.isEmpty()) {
            try {
                x = traversalQueue.dequeue();
            } catch (EmptyCollectionException e) {
                throw new RuntimeException(e);
            }
            resultList.addToRear(vertices[x.intValue()]);
            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    traversalQueue.enqueue((i));
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }


    /**
     * Returns an iterator for a Depth-First Search (DFS) traversal starting from the specified vertex.
     * The DFS algorithm explores as far as possible along each branch before backtracking.
     *
     * @param startVertex the index of the starting vertex for DFS traversal
     * @return an iterator that provides vertices in the order they are visited during DFS traversal
     */
    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        int startIndex = getIndex(startVertex);
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[numVertices];
        if (!indexIsValid(startIndex))
            return resultList.iterator();
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;
        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;
        while (!traversalStack.isEmpty()) {
            try {
                x = traversalStack.peek();

                found = false;

                for (int i = 0; (i < numVertices) && !found; i++) {
                    if (adjMatrix[x.intValue()][i] && !visited[i]) {
                        traversalStack.push(i);
                        resultList.addToRear(vertices[i]);
                        visited[i] = true;
                        found = true;
                    }
                }
                if (!found && !traversalStack.isEmpty())
                    traversalStack.pop();
            } catch (EmptyCollectionException e) {
                throw new RuntimeException(e);
            }
        }
        return resultList.iterator();

    }

    /**
     * Returns an iterator representing the vertices of the shortest path between the specified start and target vertices.
     *
     * @param startVertex  the starting vertex
     * @param targetVertex the target vertex
     * @return an iterator representing the vertices of the shortest path, or an empty iterator if no path exists
     */
    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return this.iteratorShortestPath(this.getIndex(startVertex), this.getIndex(targetVertex));
    }


    /**
     * Finds and returns an iterator representing the vertices of the shortest path between
     * the specified start and target vertices using Breadth-First Search (BFS) algorithm.
     *
     * @param startIndex  the index of the starting vertex
     * @param targetIndex the index of the target vertex
     * @return an iterator representing the vertices of the shortest path, or an empty iterator if no path exists
     */
    private Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        if (this.indexIsValid(startIndex) && this.indexIsValid(targetIndex)) {
            Iterator<Integer> iterator = this.iteratorShortestPathIndexes(startIndex, targetIndex);

            while (iterator.hasNext()) {
                resultList.addToRear(this.vertices[(Integer) iterator.next()]);
            }

        }
        return resultList.iterator();
    }

    /**
     * Finds and returns an iterator representing the indexes of the shortest path between
     * the specified start and target vertices using Breadth-First Search (BFS) algorithm.
     *
     * @param startIndex  the index of the starting vertex
     * @param targetIndex the index of the target vertex
     * @return an iterator representing the indices of the shortest path, or an empty iterator if no path exists
     */
    private Iterator<Integer> iteratorShortestPathIndexes(int startIndex, int targetIndex) {
        int index = startIndex;
        int[] pathLength = new int[this.size()];
        int[] predecessor = new int[this.size()];
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();
        if (this.indexIsValid(startIndex) && this.indexIsValid(targetIndex) && startIndex != targetIndex) {
            boolean[] visited = new boolean[this.size()];

            for (int i = 0; i < this.size(); ++i) {
                visited[i] = false;
            }

            traversalQueue.enqueue(startIndex);
            visited[startIndex] = true;
            pathLength[startIndex] = 0;
            predecessor[startIndex] = -1;

            while (!traversalQueue.isEmpty() && index != targetIndex) {
                try {
                    index = (Integer) traversalQueue.dequeue();

                    for (int i = 0; i < this.size(); ++i) {
                        if (this.adjMatrix[index][i] && !visited[i]) {
                            pathLength[i] = pathLength[index] + 1;
                            predecessor[i] = index;
                            traversalQueue.enqueue(i);
                            visited[i] = true;
                        }
                    }
                } catch (EmptyCollectionException e) {
                    throw new RuntimeException(e);
                }
            }

            if (index == targetIndex) {
                LinkedStack<Integer> stack = new LinkedStack<>();
                stack.push(index);
                do {
                    index = predecessor[index];
                    stack.push(index);
                } while (index != startIndex);

                while (!stack.isEmpty()) {
                    try {
                        resultList.addToRear((Integer) stack.pop());
                    } catch (EmptyCollectionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return resultList.iterator();
        } else {
            return resultList.iterator();
        }
    }


    /**
     * determines whether the graph is empty by checking the number of vertices
     * @return true if empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

    /**
     * Determines if the graph is connected
     * @return
     */
    @Override
    public boolean isConnected() {
        boolean isConnected = true;
        for (boolean[] matrix : this.adjMatrix) {
            for (int j = 0; j < this.adjMatrix.length; j++) {
                if (!matrix[j]) {
                    isConnected = false;
                    break;
                }
            }
        }
        return isConnected;
    }

    /**
     * returns the size of this graph, in other words, the number of vertices
     * @return the number of vertices
     */
    @Override
    public int size() {
        return numVertices;
    }
}
