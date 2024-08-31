package captureTheFlagGame.Model.Algortithms;

import captureTheFlagGame.Exceptions.NoSuchIndexException;
import captureTheFlagGame.Model.Map;

import java.util.Iterator;

/**
 * This class details the algorithm for the HeavyChampBot, which moves by picking the heaviest edge available, amongst
 * its neighbors.
 */
public class HeavyChampBotAlgorithm extends BotMovement implements Iterator<Integer> {

    private int currentIndex;
    private int previousIndex;

    /**
     * Constructor with the necessary information to run to algorithm
     * @param map map the bot moves in
     * @param endIndex target index of this bot
     * @param startIndex starting index of this bot
     */
    public HeavyChampBotAlgorithm(Map map, int endIndex, int startIndex) {
        super("HeavyChampBot", map, endIndex, startIndex);
    }

    /**
     * Returns the next index the bot should move into
     * @return the next index
     * @throws NoSuchIndexException in case there is an attempt at accessing an index which isn't available
     */
    public int nextIndex() throws NoSuchIndexException {
        int[] neighbors = map.getNeighbors(currentIndex, endIndex);
        boolean[] occupied = new boolean[neighbors.length];
        double[] pathWeights = new double[neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            pathWeights[i] = map.getWeightForHeavyChamp(currentIndex, neighbors[i]);
            occupied[i] = map.getVertices()[neighbors[i]].isOccupied();
        }
        return getNeighborWithHeaviestPath(occupied, neighbors, pathWeights);
    }

    /**
     * Retrieves the neighbor to which the path weighs the heaviest
     * @param occupied array containing the occupied status for each of the neighbors
     * @param neighbors array of neighbors
     * @param pathWeights array of pathweights for each neighbor
     * @return
     */
    private int getNeighborWithHeaviestPath(boolean[] occupied, int[] neighbors, double[] pathWeights) {
        int heaviestNeighbor = -1;
        double maxPathWeight = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < neighbors.length; i++) {
            if (!occupied[i] && pathWeights[i] > maxPathWeight) {
                maxPathWeight = pathWeights[i];
                heaviestNeighbor = i;
            }
        }

        if (heaviestNeighbor != -1) {
            return neighbors[heaviestNeighbor];
        } else {
            System.out.println("There are no available neighbors with a heavier path. Backtracking!");
            return previousIndex;
        }
    }

    /**
     * Determines whether the bot can move on by checking if it has reached the final index
     * @return true if it can keep moving, false otherwise
     */
    @Override
    public boolean hasNext() {
        return currentIndex != endIndex;
    }

    /**
     * Returns the next index, updating the current location and previous index
     * @return the current index, which is now the next index the bot will move into
     */
    @Override
    public Integer next() {
        previousIndex = currentIndex;
        try {
            currentIndex = nextIndex();
        } catch (NoSuchIndexException e) {
            throw new RuntimeException(e.getMessage());
        }
        return currentIndex;
    }
}
