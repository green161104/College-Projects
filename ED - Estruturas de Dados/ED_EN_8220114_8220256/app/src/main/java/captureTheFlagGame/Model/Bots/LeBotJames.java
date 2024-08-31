package captureTheFlagGame.Model.Bots;

import captureTheFlagGame.Model.Algortithms.LebotJamesAlgorithm;
import captureTheFlagGame.Model.Map;
import captureTheFlagGame.Model.Players;


public class LeBotJames extends BigBot {
    int currentPosition;
    LebotJamesAlgorithm algorithm;


    /**
     * Constructor for LeBotJames bot, this bot traverses the map completely at random.
     *
     * @param owner           owner of this bot
     * @param currentPosition current location in the map of the bot
     * @param gamemap         map where the game is played
     * @param endlocation     location of the enemy flag
     * @param startPosition   location of ally flag
     */
    public LeBotJames(Players owner, int currentPosition, Map gamemap, int endlocation, int startPosition) {
        super(owner, "LeBotJames", gamemap, currentPosition, endlocation, startPosition);
         algorithm = new LebotJamesAlgorithm(this.getGamemap(), this.getEndLocation(), this.getCurrentLocation());
    }

    /*
  ensure methods for position ( current position, start position, end position all work in the super class)
   */


    /**
     * void method to traverse the bot through the map, creates a queue with the calculated steps to reach
     * the enemy flag, after that sets the vertex previously occupied by the bot has not occupied ( false )
     * and the current index where the bot stands as occupied ( true ).
     */
    public void move() {

        int nextLocation = algorithm.next();
        if (nextLocation != -1) {
            {
                if (this.getCurrentLocation() != this.getStartingLocation()) {
                    getGamemap().getVertices()[this.getCurrentLocation()].setOccupied(false);
                }
                this.setCurrentLocation(nextLocation);
                getGamemap().getVertices()[this.getCurrentLocation()].setOccupied(true);
            }
        }

    }

    /**
     *
     * @return LeBotJames as string
     */
    @Override
    public String getBotType() {
        return "LeBotJames";
    }
}


