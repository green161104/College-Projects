package captureTheFlagGame.Model.Bots;

import captureTheFlagGame.Model.Algortithms.HeavyChampBotAlgorithm;
import captureTheFlagGame.Model.Map;
import captureTheFlagGame.Model.Players;

public class HeavyChampBot extends BigBot {
    private HeavyChampBotAlgorithm algorithm;

    /**
     * Constructor for heavyChampBot
     * Only difference to its parent class is that, this bot has a
     * algorithm attributed to him, in this case "HeavyChampBotAlgorithm"
     * @param owner explained in super() constructor
     * @param currentLocation owner explained in super() constructor
     * @param gamemap owner explained in super() constructor
     * @param endPoint owner explained in super() constructor
     * @param startpoint owner explained in super() constructor
     */
    public HeavyChampBot(Players owner, int currentLocation, Map gamemap, int endPoint, int startpoint) {
        super(owner, "Heavy Champ", gamemap, currentLocation, endPoint, startpoint);
        this.algorithm = new HeavyChampBotAlgorithm(gamemap, endPoint, startpoint);
    }

    /**
     * Move method for HeavyChampBot, it updates the current index in the bot, according to
     * the result of the bot algorithm.
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
     * @return HeavyChampBot as string
     */
    @Override
    public String getBotType() {
        return "HeavyChampBot";
    }
}

