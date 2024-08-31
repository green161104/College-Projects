package Custom_CollectionsED.graphs.Graphs;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.graphs.Interfaces.NetworkADT;
import Custom_CollectionsED.list.arrayLists.ArrayUnorderedList;
import Custom_CollectionsED.queue.LinkedQueue;
import Custom_CollectionsED.stack.LinkedStack;
import Custom_CollectionsED.trees.binary.heap.ArrayHeap;
import Custom_CollectionsED.trees.binary.heap.LinkedHeap;
import Custom_CollectionsED.trees.binary.interfaces.HeapADT;

import java.util.Iterator;

/**
 * This class is a representation of a network, implemented through the use of
 * an adjacency matrix.
 *
 * @param <T> The data type to be stored in this data structure
 */
public class Network<T> extends Graph<T> implements NetworkADT<T> {

    /**
     * The adjacency matrix which represents the connections and edge weights
     * in-between vertices.
     */
    protected double[][] adjMatrix;

    /**
     * Creates a new empty network with the default size.
     */
    public Network() {
        this.numVertices = 0;
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) new Object[10];
    }

    /**
     * Creates a new network according to the given size
     *
     * @param initialSize that the network will take as a starting point
     */
    public Network(int initialSize) {
        this.numVertices = 0;
        this.adjMatrix = new double[initialSize][initialSize];
        this.vertices = (T[]) new Object[initialSize];
    }

    /**
     * Expands the capacity of the network, by essentially doubling it's size.
     */
    @Override
    protected void expandCapacity() {
        T[] largerVertices = (T[]) new Object[this.vertices.length * EXPAND_FACTOR];
        double[][] largerAdjMatrix = new double[this.vertices.length * EXPAND_FACTOR][this.vertices.length * EXPAND_FACTOR];

        for (int i = 0; i < this.numVertices; ++i) {
            System.arraycopy(this.adjMatrix[i], 0, largerAdjMatrix[i], 0, this.numVertices);
            largerVertices[i] = this.vertices[i];
        }

        this.vertices = largerVertices;
        this.adjMatrix = largerAdjMatrix;
    }


    /**
     * Adds an edge to the network, with the specified weight.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @param weight  the weight this edge will have
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        this.addEdge(super.getIndex(vertex1), super.getIndex(vertex2), weight);
    }

    /**
     * Helper method to effectively add the edge, by updating the adjacency matrix
     *
     * @param index1 index of the first vertex
     * @param index2 index of the second vertex
     * @param weight that the edge between the vertices will take
     */
    private void addEdge(int index1, int index2, double weight) {
        if (super.indexIsValid(index1) && super.indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = weight;
            this.adjMatrix[index2][index1] = weight;
        }

    }

    /**
     * Adds an edge with the weight of zero between two vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        this.addEdge(super.getIndex(vertex1), super.getIndex(vertex2), 0.0);
    }

    /**
     * Adds a new vertex to the network
     *
     * @param vertex the vertex to be added to this graph
     * @throws IllegalArgumentException in case the vertex passed is the incorrect type.
     */
    @Override
    public void addVertex(T vertex) throws IllegalArgumentException {
        if (this.size() == this.vertices.length) {
            this.expandCapacity();
        }

        this.vertices[this.size()] = vertex;

        for (int i = 0; i <= this.size(); ++i) {
            this.adjMatrix[this.numVertices][i] = Double.POSITIVE_INFINITY;
            this.adjMatrix[i][this.numVertices] = Double.POSITIVE_INFINITY;
        }

        ++this.numVertices;
    }


    /**
     * Removes an edge between two vertices
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        this.removeEdge(this.getIndex(vertex1), this.getIndex(vertex2));
    }

    /**
     * Helper method to effectively remove an edge connecting two vertices
     * @param index1 index of the first vertex
     * @param index2 index of the second vertex
     */
    public void removeEdge(int index1, int index2) {
        if (this.indexIsValid(index1) && this.indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = Double.POSITIVE_INFINITY;
            this.adjMatrix[index2][index1] = Double.POSITIVE_INFINITY;
        }

    }

    /**
     * Removes a vertex from this network
     * @param vertex the vertex to be removed from this graph
     */
    @Override
    public void removeVertex(T vertex) {
        this.removeVertex(this.getIndex(vertex));
    }

    /**
     * Helper method to effectively remove a vertex, by verifying if the vertex is valid,
     * and removing it from the array of vertices, along with the adjacency matrix
     * @param index of the vertex to remove
     */
    private void removeVertex(int index) {
        if (this.indexIsValid(index)) {
            --this.numVertices;

            int i;
            for (i = index; i < this.size(); ++i) {
                this.vertices[i] = this.vertices[i + 1];
            }

            int j;
            for (i = index; i < this.size(); ++i) {
                for (j = 0; j <= this.size(); ++j) {
                    this.adjMatrix[i][j] = this.adjMatrix[i + 1][j];
                }
            }

            for (i = index; i < this.size(); ++i) {
                for (j = 0; j < this.size(); ++j) {
                    this.adjMatrix[j][i] = this.adjMatrix[j][i + 1];
                }
            }
        }

    }


    /**
     * Finds the edge with the specified weight
     * @param weight to be sought after
     * @param visited array of visited vertices
     * @return the edge with the specified weight
     */
    protected int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
        int[] edge = new int[2];

        for (int i = 0; i < this.numVertices; ++i) {
            for (int j = 0; j < this.numVertices; ++j) {
                if (this.adjMatrix[i][j] == weight && visited[i] ^ visited[j]) {
                    edge[0] = i;
                    edge[1] = j;
                    return edge;
                }
            }
        }

        edge[0] = -1;
        edge[1] = -1;
        return edge;
    }

    /**
     * Returns a BFS (breadth first) iterator for this network.
     * @param startVertex the starting vertex
     * @return the iterator with the BFS algorithm
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        return this.iteratorBFS(super.getIndex(startVertex));
    }

    /**
     * Returns an iterator for a Breadth-First Search (BFS) traversal starting from the specified vertex.
     * The BFS algorithm visits vertices level by level, exploring all neighbors of a vertex before moving on
     * to the next level.
     *
     * @param startIndex the index of the starting vertex for BFS traversal
     * @return an iterator that provides vertices in the order they are visited during BFS traversal
     */
    private Iterator<T> iteratorBFS(int startIndex) {
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        if (super.indexIsValid(startIndex)) {
            boolean[] visited = new boolean[super.size()];

            int i;
            for (i = 0; i < super.size(); ++i) {
                visited[i] = false;
            }

            traversalQueue.enqueue(startIndex);
            visited[startIndex] = true;

            while (!traversalQueue.isEmpty()) {
                try {
                    Integer x = traversalQueue.dequeue();
                    resultList.addToRear(super.vertices[x]);

                    for (i = 0; i < super.size(); ++i) {
                        if (this.adjMatrix[x][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                            traversalQueue.enqueue(i);
                            visited[i] = true;
                        }
                    }
                } catch (EmptyCollectionException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return resultList.iterator();
    }

    @Override
    /**
     * Returns a DFS (depth-first) iterator for this network
     * @param startVertex the starting vertex
     * @return the DFS iterator
     */
    public Iterator<T> iteratorDFS(T startVertex) {
        return this.iteratorDFS(super.getIndex(startVertex));
    }


    /**
     * Returns an iterator for a Depth-First Search (DFS) traversal starting from the specified vertex.
     * The DFS algorithm explores as far as possible along each branch before backtracking.
     *
     * @param startIndex the index of the starting vertex for DFS traversal
     * @return an iterator that provides vertices in the order they are visited during DFS traversal
     */
    private Iterator<T> iteratorDFS(int startIndex) {
        LinkedStack<Integer> traversalStack = new LinkedStack<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        boolean[] visited = new boolean[super.size()];
        if (super.indexIsValid(startIndex)) {
            int i;
            for (i = 0; i < super.size(); ++i) {
                visited[i] = false;
            }

            traversalStack.push(startIndex);
            resultList.addToRear(super.vertices[startIndex]);
            visited[startIndex] = true;

            while (!traversalStack.isEmpty()) {
                try {
                    Integer x = traversalStack.peek();
                    boolean found = false;

                    for (i = 0; i < super.size() && !found; ++i) {
                        if (this.adjMatrix[x][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                            traversalStack.push(i);
                            resultList.addToRear(super.vertices[i]);
                            visited[i] = true;
                            found = true;
                        }
                    }

                    if (!found && !traversalStack.isEmpty()) {
                        traversalStack.pop();
                    }
                } catch (EmptyCollectionException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return resultList.iterator();
    }

    /**
     * Returns an iterator for the shortest path between two vertices
     * @param startVertex the starting vertex
     * @param targetVertex the ending vertex
     * @return the iterator for the shortest path
     */
    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return this.iteratorShortestPath(this.getIndex(startVertex), this.getIndex(targetVertex));
    }

    /**
     * Returns an iterator for the vertices of the shortest path between the specified start and target vertices.
     *
     * @param startIndex  the index of the starting vertex
     * @param targetIndex the index of the target vertex
     * @return an iterator that provides vertices in the order of the shortest path
     */
    private Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<>();
        if (this.indexIsValid(startIndex) && this.indexIsValid(targetIndex)) {
            Iterator<Integer> it = this.iteratorShortestPathIndices(startIndex, targetIndex);

            while (it.hasNext()) {
                templist.addToRear(this.vertices[it.next()]);
            }

            return templist.iterator();
        } else {
            return templist.iterator();
        }
    }

    /**
     * Returns an iterator for the indices of the shortest path between the specified start and target vertices.
     * Uses Dijkstra's algorithm for finding the shortest path.
     *
     * @param startIndex  the index of the starting vertex
     * @param targetIndex the index of the target vertex
     * @return an iterator that provides indices in the order of the shortest path
     */
    public Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex) {
        // Initialize data structures for Dijkstra's algorithm
        int[] predecessor = new int[this.numVertices];
        HeapADT<Double> traversalMinHeap = new LinkedHeap<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();
        LinkedStack<Integer> stack = new LinkedStack<>();
        double[] pathWeight = new double[this.numVertices];

        // Initialize pathWeight to infinity for all vertices
        for (int i = 0; i < this.numVertices; ++i) {
            pathWeight[i] = Double.POSITIVE_INFINITY;
        }

        // Initialize visited array to false for all vertices
        boolean[] visited = new boolean[this.numVertices];
        int i;
        for (i = 0; i < this.numVertices; ++i) {
            visited[i] = false;
        }

        // Check if both start and target indices are valid, and if the graph is not empty
        if (this.indexIsValid(startIndex) && this.indexIsValid(targetIndex) && startIndex != targetIndex && !this.isEmpty()) {
            // Initialize pathWeight, predecessor, and visited arrays
            pathWeight[startIndex] = 0.0;
            predecessor[startIndex] = -1;
            visited[startIndex] = true;
            double weight = 0.0;

            // Update pathWeight for neighbors of the starting vertex and add them to the min heap
            for (i = 0; i < this.numVertices; ++i) {
                if (!visited[i]) {
                    pathWeight[i] = pathWeight[startIndex] + this.adjMatrix[startIndex][i];
                    predecessor[i] = startIndex;
                    traversalMinHeap.addElement(pathWeight[i]);
                }
            }

            int index;
            do {
                // Remove the vertex with the minimum path weight from the min heap
                try {
                    weight = traversalMinHeap.removeMin();
                } catch (exceptions.EmptyCollectionException e) {
                    throw new RuntimeException(e);
                }
                traversalMinHeap = new LinkedHeap<>();
                // If the weight is infinity, there is no path to the target vertex
                if (weight == Double.POSITIVE_INFINITY) {
                    return resultList.iterator();
                }

                // Get the index of the next vertex to visit
                index = this.getIndexOfAdjVertexWithWeightOf(visited, pathWeight, weight);
                visited[index] = true;

                // Update pathWeight for neighbors of the current vertex and add them to the min heap
                for (i = 0; i < this.numVertices; ++i) {
                    if (!visited[i]) {
                        if (this.adjMatrix[index][i] < Double.POSITIVE_INFINITY && pathWeight[index] + this.adjMatrix[index][i] < pathWeight[i]) {
                            pathWeight[i] = pathWeight[index] + this.adjMatrix[index][i];
                            predecessor[i] = index;
                        }

                        traversalMinHeap.addElement(pathWeight[i]);
                    }
                }
            } while (!traversalMinHeap.isEmpty() && !visited[targetIndex]);

            // Reconstruct the shortest path using the predecessor array
            index = targetIndex;
            stack.push(targetIndex);

            do {
                index = predecessor[index];
                stack.push(index);
            } while (index != startIndex);

            // Add vertices of the shortest path to the result list
            while (!stack.isEmpty()) {
                try {
                    resultList.addToRear(stack.pop());
                } catch (EmptyCollectionException exception) {
                    throw new RuntimeException(exception);
                }
            }

        }
        return resultList.iterator();
    }

    /**
     * Finds and returns the index of an adjacent vertex with a specific weight in the graph.
     * The weight is specified by the parameter 'weight', and the search is restricted to vertices
     * that have not been visited and have a path weight equal to the specified weight.
     *
     * @param visited    an array indicating whether each vertex has been visited
     * @param pathWeight an array containing the path weights for each vertex
     * @param weight     the specific weight to find in the adjacency matrix
     * @return the index of an adjacent vertex with the specified weight, or -1 if not found
     */
    protected int getIndexOfAdjVertexWithWeightOf(boolean[] visited, double[] pathWeight, double weight) {
        for (int i = 0; i < this.numVertices; ++i) {
            if (pathWeight[i] == weight && !visited[i]) {
                for (int j = 0; j < this.numVertices; ++j) {
                    if (this.adjMatrix[i][j] < Double.POSITIVE_INFINITY && visited[j]) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    /**
     * Computes and returns the Minimum Spanning Tree (MST) of the network using Prim's algorithm.
     * The MST is represented as a new Network instance.
     *
     * @return a new Network instance representing the Minimum Spanning Tree (MST) of the original network
     * @throws EmptyCollectionException if the original network is empty
     */
    public Network<T> mstNetwork() throws EmptyCollectionException {
        if (this.isEmpty()){
            throw new EmptyCollectionException("no elements");
        }
        int indexFrom, indexTo;
        int index;
        double weight;
        int[] edge = new int[2];
        ArrayHeap<Double> minHeap = new ArrayHeap<>();
        Network<T> resultGraph = new Network<>();
        if (isEmpty() || !isConnected())
            return resultGraph;
        resultGraph.adjMatrix = new double[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                resultGraph.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
        resultGraph.vertices = (T[]) (new Object[numVertices]);
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;
        edge[0] = 0;
        resultGraph.vertices[0] = this.vertices[0];
        resultGraph.numVertices++;
        visited[0] = true;

        for (int i = 0; i < numVertices; i++) {
            minHeap.addElement(adjMatrix[0][i]);
        }

        while ((resultGraph.size() < this.size()) && !minHeap.isEmpty()) {
            do {
                weight = (minHeap.removeMin());
                edge = getEdgeWithWeightOf(weight, visited);
            } while (!indexIsValid(edge[0]) || !indexIsValid(edge[1]));

            indexFrom = edge[0];
            indexTo = edge[1];
            if (!visited[indexFrom])
                index = indexFrom;
            else
                index = indexTo;

            resultGraph.vertices[index] = this.vertices[index];
            visited[index] = true;
            resultGraph.numVertices++;
            resultGraph.adjMatrix[indexFrom][indexTo] = this.adjMatrix[indexFrom][indexTo];
            resultGraph.adjMatrix[indexTo][indexFrom] = this.adjMatrix[indexTo][indexFrom];
            /** Add all edges, that are adjacent to the newly added vertex,
             to the heap */
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && (this.adjMatrix[i][index] < Double.POSITIVE_INFINITY)) {
                    edge[0] = index;
                    edge[1] = i;
                    minHeap.addElement(adjMatrix[index][i]);
                }
            }
        }
        return resultGraph;
    }


    /**
     * Calculates and returns the weight of the shortest path between the specified start and target vertices.
     *
     * @param startIndex  the index of the starting vertex
     * @param targetIndex the index of the target vertex
     * @return the weight of the shortest path between the start and target vertices
     *         or Double.POSITIVE_INFINITY if there is no valid path or indices are invalid
     */
    private double shortestPathWeight(int startIndex, int targetIndex) {
        double result = 0.0;
        if (this.indexIsValid(startIndex) && this.indexIsValid(targetIndex)) {
            Iterator<Integer> it = this.iteratorShortestPathIndices(startIndex, targetIndex);
            if (!it.hasNext()) {
                return Double.POSITIVE_INFINITY;
            } else {
                int index2;
                for (int index1 =  it.next(); it.hasNext(); index1 = index2) {
                    index2 = it.next();
                    result += this.adjMatrix[index1][index2];
                }

                return result;
            }
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        return this.shortestPathWeight(super.getIndex(vertex1), super.getIndex(vertex2));
    }
}