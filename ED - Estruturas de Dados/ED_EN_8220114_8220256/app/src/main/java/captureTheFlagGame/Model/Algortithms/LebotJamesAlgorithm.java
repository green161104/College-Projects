package captureTheFlagGame.Model.Algortithms;


import captureTheFlagGame.Exceptions.NoSuchIndexException;
import captureTheFlagGame.Model.Map;
import captureTheFlagGame.Model.gameCollection.GameList;
import captureTheFlagGame.Model.gameCollection.GameNode;

import java.util.Iterator;
import java.util.Random;

public class LebotJamesAlgorithm extends BotMovement implements Iterator<Integer> {

    private int currentIndex;
    private int previousIndex;


    /**
     * Constructor for leBotJames bot.
     * @param map map where the bot will be moving
     * @param endIndex index of the enemyMap, meaning the final location of this bot.
     * @param startIndex starting index of the bot, ally map.
     */
    public LebotJamesAlgorithm(Map map, int endIndex, int startIndex) {
        super("LeBot James", map, endIndex, startIndex);
        this.currentIndex = startIndex;
    }

    /**
     * gathers all the vertices that are neighbors to the vertex the bot is currently in,
     * checks which ones are occupied.
     * @return the next index the bot should move to.
     * @throws NoSuchIndexException
     */
    public int nextIndex() throws NoSuchIndexException {
        int[] neighbors = map.getNeighbors(currentIndex, endIndex);
        boolean[] occupied = new boolean[neighbors.length];
        for (int i = 0; i < neighbors.length; i++) {
            occupied[i] = map.getVertices()[neighbors[i]].isOccupied();
        }
        return getRandomNeighbor(occupied, neighbors);
    }

    /**
     * This method takes in the vertices and the information for which ones occupied
     * and returns one that isn't occupied.
     * @param occupied neighbor indices that are occupied
     * @param neighbors neighbor indices
     * @return a random index for the neighbor to move to
     */
    private int getRandomNeighbor(boolean[] occupied, int[] neighbors) {
        Random random = new Random();
        int neighbor;

        do {

            neighbor = random.nextInt((neighbors.length));

            if(neighbor <= -1) { neighbor = random.nextInt((neighbors.length)); } // aldrabice

            if (!occupied[neighbor]) {
                return neighbors[neighbor];
            }

        } while (occupied[neighbor]);

        System.out.println("There are no available neighbors :( we have to backtrack!");
        return previousIndex;
    }

    /**
     * Determines whether the bot can move on by checking if it has reached the final index
     * @return true aslong as currentIndex isn't endIndex
     */
    @Override
    public boolean hasNext() {
        return currentIndex != endIndex;
    }

    /**
     * Returns the next index, updating the current location and previous index
     * @return  the current index, which is now the next index the bot will move into
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

