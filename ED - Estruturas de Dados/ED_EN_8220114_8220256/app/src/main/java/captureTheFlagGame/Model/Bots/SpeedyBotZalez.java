package captureTheFlagGame.Model.Bots;

import captureTheFlagGame.Exceptions.NoSuchIndexException;
import captureTheFlagGame.Model.Algortithms.SpeedyBotZalezAlgorithm;
import captureTheFlagGame.Model.Map;
import captureTheFlagGame.Model.Players;

public class SpeedyBotZalez extends BigBot {

    private SpeedyBotZalezAlgorithm algorithm;

    public SpeedyBotZalez(Players owner, int currentLocation, Map gamemap, int endPoint, int startpoint) {
        super(owner, "SpeedyBotZalez", gamemap, currentLocation, endPoint, startpoint);
        this.algorithm = new SpeedyBotZalezAlgorithm(gamemap, getEndLocation(), startpoint);
    }

    /**
     * private String botName;
     * // Iterator <GameNode> iterator;
     * private Map gamemap;
     * private int currentLocation;
     * private int startingLocation; // spawn dos bots, é o unico sitio que pode ter bots a " colidir " e a allyFlag.
     * private int endLocation; //index of the enemy flag.
     */


    public void move() throws NoSuchIndexException {
        SpeedyBotZalezAlgorithm algorithm = new SpeedyBotZalezAlgorithm(this.getGamemap(), this.getEndLocation(), this.getCurrentLocation());
        int nextLocation = algorithm.iteratorShortestPathIndices(getCurrentLocation());
        if (nextLocation != -1) {
            if (this.getCurrentLocation() != this.getStartingLocation()) {
                getGamemap().getVertices()[this.getCurrentLocation()].setOccupied(false);
            }
            this.setCurrentLocation(nextLocation);
            getGamemap().getVertices()[this.getCurrentLocation()].setOccupied(true);
        }
    }

    /**
     *
     * @return SpeedyBotZalez as string
     */
    @Override
    public String getBotType() {
        return "SpeedyBotZalez";
    }
}


