package captureTheFlagGame.Interfaces;

import captureTheFlagGame.Exceptions.NoSuchIndexException;

import java.io.IOException;

/**
 * This interface details the essential methods the game map needs in order to serve as a stage for the game
 */
public interface MapInterface {

    /**
     * This method randomly generates a map with the requested number of vertices, density, and type
     * @throws NoSuchIndexException in case there is an attempt at checking the connection between unavailable vertices
     */
    public void createMap() throws NoSuchIndexException;

    /**
     * This method will export the current map, by writing the adjacency matrix into a file
     * @param mapName the name given to the map
     * @throws IOException if there is an issue writing the file
     */
    public void exportMap(String mapName) throws IOException;


}
