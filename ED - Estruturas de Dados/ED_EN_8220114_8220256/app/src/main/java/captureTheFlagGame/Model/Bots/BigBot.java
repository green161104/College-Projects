package captureTheFlagGame.Model.Bots;

import captureTheFlagGame.Exceptions.NoSuchIndexException;
import captureTheFlagGame.Interfaces.BotInterface;
import captureTheFlagGame.Model.Map;
import captureTheFlagGame.Model.Players;

public abstract class BigBot implements BotInterface {

    private String botName;
    // Iterator <GameNode> iterator;
    private Players owner;
    private Map gamemap;
    private int currentLocation;
    private int startingLocation; // spawn dos bots, é o unico sitio que pode ter bots a " colidir " e a allyFlag.
    private int endLocation; //index of the enemy flag.


    /**
     * Constructor for BigBot, the parent class for all other bots.
     * @param owner player whose this bot beliongs to
     * @param nome bot name
     * @param gamemap map the bot is gonna be moving in
     * @param current current index for bot
     * @param endLocation end location, meaning enemy flag vertex location
     * @param start start location, meaning ally flag location, meaning spawn point
     */
    public BigBot(Players owner, String nome,Map gamemap, int current, int endLocation, int start) {
        this.owner = owner;
        this.botName = nome;
        this.gamemap = gamemap;
        this.currentLocation = current;
        this.endLocation = endLocation;
        this.startingLocation = start;
    }

    /**
     * method for all bots to move
     * @throws NoSuchIndexException the index returned to where the bot wants to move isn't valid.
     */
    public abstract void move() throws NoSuchIndexException;

    /**
     * getter for map the game is being played in
     * @return map object
     */
    public Map getGamemap(){return this.gamemap;}

    /**
     * setter for map, is not used in the program.
     * @param gamemap map for game.
     */
    public void setGamemap(Map gamemap){ this.gamemap = gamemap;}

    /**
     * getter for owner of bot
     * @return player object who owns this bot
     */
    public Players getOwner(){return this.owner;}

    /**
     * setter for owner of this bot
     * @param owner Player object who this bot should belong to
     */
    public void setOwner(Players owner){ this.owner = owner;}

    /**
     * getter for current index of bot
     * @return current index of bot
     */
    public int getCurrentLocation() {
        return this.currentLocation;
    }

    /**
     * returns starting index of bot
     * @return ally flag index
     */
    public int getStartingLocation() {
        return this.startingLocation;
    }

    /**
     * getter of enemy flag index
     * @return enemy flag index
     */
    public int getEndLocation() {
        return this.endLocation;
    }

    /**
     * setter for current index of the bot.
     * @param currentLocation the index corresponding to the current location
     */
    public void setCurrentLocation(int currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * setter of spawn index for bot
     * @param start index to be set as the starting location
     */
    public void setStartingLocation(int start) {
        this.startingLocation = start;
    }

    /**
     *
     * @param location index to be set as the target for this bot
     */
    public void setEndLocation(int location) {
        this.endLocation = location;
    }

    /**
     *  getter of bot name
     * @return bot name
     */
    public String getBotName() {
        return this.botName;
    }

    /**
     *
     * @param name desired name for bot
     */
    public void setBotName(String name) {
        this.botName = name;
    }

    /**
     * getter for type of bot
     * @return type of bot, there are 3
     */
    public abstract String getBotType();

    /**
     * boolean method that compares bots
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        // Compare based on bot type
        return getBotType().equals(((BigBot) obj).getBotType());
    }


}
