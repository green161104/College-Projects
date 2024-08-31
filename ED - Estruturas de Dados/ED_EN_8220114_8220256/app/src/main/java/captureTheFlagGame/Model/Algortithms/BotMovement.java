package captureTheFlagGame.Model.Algortithms;

import captureTheFlagGame.Model.Map;

/**
 * This is an abstract class which defines the attributes necessary to allow a bot to move
 */
public abstract class BotMovement {

    protected String name;

    protected Map map;
    protected final int endIndex;
    protected final int startIndex;
    protected int numberOfVertices;

    /**
     * Constructor to gather information necessary for bot movement
     * @param name of the bot
     * @param map the bot moves in
     * @param endIndex target index of this bot
     * @param startIndex starting index of this bot
     */
    public BotMovement(String name, Map map, int endIndex, int startIndex){
        this.map = map;
        this.name = name;
        this.endIndex = endIndex;
        this.numberOfVertices = map.size();
        this.startIndex = startIndex;
    }

}
