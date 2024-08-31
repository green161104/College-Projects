package captureTheFlagGame.Interfaces;

import captureTheFlagGame.Model.Players;

/**
 * This interface details the methods that are necessary for the bots o function
 */
public interface BotInterface {

    /**
     * This method returns the bot's owner
     *
     * @return
     */
    Players getOwner();

    /**
     * This method sets the bot's owner
     *
     * @param owner
     */
    void setOwner(Players owner);

    /**
     * This method retrieves the current index of this bot on the map
     * @return an int representing the index
     */
    int getCurrentLocation();

    /**
     * This method retrieves the starting index of this bot
     * @return an int representing the index
     */
    int getStartingLocation();

    /**
     * Retrieves the end or target index for this bot
     * @return an int representing the index
     */
    int getEndLocation();

    /**
     * Sets the bot's current location
     * @param currentLocation the index corresponding to the current location
     */
    void setCurrentLocation(int currentLocation);

    /**
     * Sets the bot's starting location
     * @param start index to be set as the starting location
     */
    void setStartingLocation(int start);

    /**
     * Sets the end location (target index) for this bot
     * @param location index to be set as the target for this bot
     */
    void setEndLocation(int location);

    /**
     * Retrieves this bot's name
     * @return the bot's name
     */
    String getBotName();

}
