package captureTheFlagGame.Model.Algortithms;

import Custom_CollectionsED.list.arrayLists.ArrayUnorderedList;
import captureTheFlagGame.Exceptions.NoSuchIndexException;
import captureTheFlagGame.Model.Map;


public class SpeedyBotZalezAlgorithm extends BotMovement {


    /**
     * Constructor for speedyBot
     * @param map map the bot moves in
     * @param endIndex index of enemyFlag, meaning final destination
     * @param startIndex index of spawn, meaning allyFlag
     */
    public SpeedyBotZalezAlgorithm(Map map, int endIndex, int startIndex) {
        super("Speedy BotZalez", map, endIndex, startIndex);

    }

    /**
     * method used to calculate the shortest path to end point.
     * keeps an array of predecessors, meaning the vertices it's visited
     * This method deals with occupied vertices by other bots, by turning the weight
     * from startIndex to occupiedIndex to positiveInfinity, making it so the bot
     * will not consider that vertex in it's path calculation.
     * @param startIndex current index the bot is in
     * @return next vertex the bot should move to
     * @throws NoSuchIndexException if index returned is not a valid vertex
     */
    public int iteratorShortestPathIndices(int startIndex) throws NoSuchIndexException {
        // Initialize data structures for Dijkstra's algorithm
        int[] predecessor = new int[map.size()];
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();
        double[] pathWeight = new double[map.size()];


        // Initialize visited array to false for all vertices
        boolean[] visited = new boolean[map.size()];

        // Check if both start and target indices are valid, and if the graph is not empty
        if (!map.indexIsValid(startIndex) || !map.indexIsValid(endIndex) || startIndex == endIndex || map.isEmpty()) {
            return -1;
        }

        // Initialize pathWeight to infinity for all vertices
        for (int i = 0; i < map.size(); ++i) {
            pathWeight[i] = Double.POSITIVE_INFINITY;
            predecessor[i] = -1;
            visited[i] = false;
        }

        pathWeight[startIndex] = 0.0;
        predecessor[startIndex] = -1;
        visited[startIndex] = true;
        int visiting = startIndex;

        while (visiting != -1) {
            visited[visiting] = true;

            int[] neighbors = map.getNeighbors(visiting, endIndex);


            for (int neighbor : neighbors) {
                double distanceToNeighbor = map.getWeight(visiting, neighbor);
                double pathWeightNoNeighbor = pathWeight[visiting] + distanceToNeighbor;
                if (pathWeightNoNeighbor < pathWeight[neighbor]) {
                    predecessor[neighbor] = visiting;
                    pathWeight[neighbor] = pathWeightNoNeighbor;
                }
            }

            visiting = -1;
            double visitingWeight = Double.POSITIVE_INFINITY;
            for (int node = 0; node < map.size(); node++) {
                if (!visited[node] && visitingWeight > pathWeight[node]) {
                    visiting = node;
                    visitingWeight = pathWeight[node];
                }
            }
        }

        // Reconstruct the shortest path using the predecessor array
        for (int i = endIndex; i != -1; i = predecessor[i]) {
            resultList.addToFront(i);
        }

        if (!resultList.isEmpty()) {
            resultList.removeFirst();
            if (!resultList.isEmpty()) {
                return resultList.first();
            }
        }
        return -1;
    }

}


