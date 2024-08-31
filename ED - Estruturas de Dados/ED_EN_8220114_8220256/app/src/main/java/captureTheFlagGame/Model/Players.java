package captureTheFlagGame.Model;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.list.UnorderedListADT;
import Custom_CollectionsED.list.arrayLists.ArrayUnorderedList;
import captureTheFlagGame.Model.Bots.*;


import java.util.Scanner;

public class Players{

    private String playerName;

    private UnorderedListADT<BigBot> playerBots;

    private Map gamemap;

    private Flag myFlag;
    private Flag enemyFlag;

    private int wins;
    private int losses;


    /**
     * constructor for Player object
     * @param name name of player
     * @param gamemap map that the players will be sharing
     * @param myFlag ally flag
     * @param enemyFlag enemy flag
     */
    public Players(String name, Map gamemap, Flag myFlag, Flag enemyFlag){
        this.enemyFlag = enemyFlag;
        this.gamemap =gamemap;
        this.myFlag = myFlag;
        this.playerName = name;
        this.playerBots = new ArrayUnorderedList<>(); // maybe should be limited
        this.wins = 0;
        this.losses = 0;
    }

    /**
     * setter for playerNames
     * @param name desired name of the player;
     */
    public void setPlayerName(String name){this.playerName = name;}

    /**
     * getter for playerName
     * @return name of player as string
     */
    public String getPlayerName(){return  this.playerName;}


    /**
     * method to assign the bots to each player within the agreed amount of bots.
     * @param numberOfBots
     */
    public  void setPlayerBots(int numberOfBots){

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < numberOfBots; i++) {
            System.out.println("Choose a bot:");
            System.out.println("1 - HeavyChampBot - this bot picks the heavist path");
            System.out.println("2 - LeBotJames - this bot is a quirky bot");
            System.out.println("3 - SpeedyBotZalez - this is the dijkstra's bot");

            int choice = scanner.nextInt();  // Read the user's choice

            switch (choice) {
                case 1:
                    playerBots.addToFront(new HeavyChampBot(this, myFlag.getHome(), gamemap,enemyFlag.getHome(), myFlag.getHome()));
                    break;
                case 2:
                    playerBots.addToFront(new LeBotJames(this, myFlag.getHome(), gamemap, enemyFlag.getHome(), myFlag.getHome()));
                    break;
                case 3:
                    playerBots.addToFront(new SpeedyBotZalez(this,  myFlag.getHome(), gamemap, enemyFlag.getHome(), myFlag.getHome()));
                    break;
                default:
                    System.out.println("Please insert a valid number.");
                    i--;  // Decrementar sempre que input inválido
                    break;
            }
        }
    }

    /**
     * setter for player bots, with each player only being allowed one of each bot
     * @param numberOfBots number of bots to be assigned
     */
    public  void setUniquePlayerBots(int numberOfBots) {
        Scanner scanner = new Scanner(System.in);

        UnorderedListADT<BigBot> selectedBots = new ArrayUnorderedList<>();
        BigBot selectedBot = null;
        for (int i = 0; i < numberOfBots; i++) {
            System.out.println("Choose a bot, " + this.playerName + ":");
            System.out.println("1 - HeavyChampBot - this bot picks the heavist path");
            System.out.println("2 - LeBotJames - this bot is a quirky bot");
            System.out.println("3 - SpeedyBotZalez - this is the dijkstra's bot");

            int choice = scanner.nextInt();  // Read the user's choice

            switch (choice) {
                case 1:
                    selectedBot = new HeavyChampBot(this, myFlag.getHome(), gamemap, enemyFlag.getHome(), myFlag.getHome());
                    break;
                case 2:
                    selectedBot = new LeBotJames(this, myFlag.getHome(), gamemap, enemyFlag.getHome(), myFlag.getHome());
                    break;
                case 3:
                    selectedBot = new SpeedyBotZalez(this, myFlag.getHome(), gamemap, enemyFlag.getHome(), myFlag.getHome());
                    break;
                default:
                    System.out.println("Please insert a valid number.");
                    i--;  // Decrementar sempre que input inválido
                    break;
            }
            if (numberOfBots <= 3) {
                if (selectedBot != null && !selectedBots.contains(selectedBot)) {
                    selectedBots.addToFront(selectedBot);
                    playerBots.addToFront(selectedBot);
                } else if (selectedBot == null || selectedBots.contains(selectedBot)) {
                    System.out.println("This bot has already been selected. Choose a different bot.");
                    i--;  // Decrementar sempre que input inválido
                }
            }
            else {
                playerBots.addToFront(selectedBot);
            }
        }
    }


    /**
     * method used to check if the bot belonging to this player is in the enemyflag,
     * if it is, the game should end.
     * @param locationOfBotThatJustMoved
     * @return
     */
    public boolean checkCapturedFlag(int locationOfBotThatJustMoved){
        int captureTheFlag = enemyFlag.getHome();

       return locationOfBotThatJustMoved == captureTheFlag;
    }

    /**
     * method used to get the current bot that is going to move.
     * logic is remove the first bot in the queue, put it back into the end of the queue and return it.
     * @return currentBot meaning, the next bot to move.
     * @throws EmptyCollectionException if the list of bots is empty
     */
    public BigBot getNextBot() throws EmptyCollectionException {
        BigBot currenBot = this.playerBots.removeFirst();
        this.playerBots.addToRear(currenBot);
        return currenBot;
    }

    /**
     * return the index of the bot that last moved ( in the back of the list of bots )
     * @return returns index of the  bot in the back of the unorderedList!
     * @throws EmptyCollectionException if the list of bots is empty
     */
    public int getCurrentBotIndex() throws EmptyCollectionException {
        if (!this.playerBots.isEmpty()) {
            // Peek at the last bot in the queue (most recently moved)
            BigBot lastBot = this.playerBots.last();
            return lastBot.getCurrentLocation();
        } else {
            throw new EmptyCollectionException("The playerBots queue is empty.");
        }
    }


    // ? public LinkedQueue<BigBot> getPlayerBots(){ return this.playerBots; }


    /**
     * counter for amount of wins for the player.
     */
    public void WonYippee(){this.wins++;}
    public int getWins(){return this.wins;}

    /**
     * counter for amount of losses for this player.
     */
    public void LossBoohoo(){this.losses++;}
    public int getLosses(){return this.losses;}
}
