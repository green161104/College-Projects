package captureTheFlagGame.Interfaces;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import captureTheFlagGame.Exceptions.NoSuchIndexException;

import java.io.IOException;

/**
 * This interface serves as communication from the API to the functional main class of the game
 */
public interface GameFlowInterface {

    /**
     * This method sets up and gathers all the necessary information in order for the game to be started.
     * @throws EmptyCollectionException in case some of the supporting colections are empty
     * @throws IOException in case there is an error reading or writing files, specifically importing and exporting the map
     * @throws NoSuchIndexException if there is an attempt at accessing an unavailable index or node
     */
    public void startGame() throws EmptyCollectionException, IOException, NoSuchIndexException, InterruptedException;
}
