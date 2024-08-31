package captureTheFlagGame;

import Custom_CollectionsED.exceptions.EmptyCollectionException;
import Custom_CollectionsED.queue.LinkedQueue;
import Custom_CollectionsED.queue.QueueADT;
import captureTheFlagGame.Exceptions.NoSuchIndexException;
import captureTheFlagGame.Interfaces.GameFlowInterface;
import captureTheFlagGame.Model.Bots.BigBot;
import captureTheFlagGame.Model.Flag;
import captureTheFlagGame.Model.Map;
import captureTheFlagGame.Model.Players;
import captureTheFlagGame.Model.gameCollection.GameNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameFlow implements GameFlowInterface {


    private Map gameMap;
    private Players player1, player2;
    private int turnCounter;
    private boolean gameOver;


    public GameFlow() {
        turnCounter = 0;
        this.gameOver = false;
    }


    /**
     * starting the game, positioning the bots,
     * printing map preview ( might be matrix ),
     * keep game going until one bot reaches the goal.
     */
    public void startGame() throws EmptyCollectionException, IOException, NoSuchIndexException, InterruptedException {

        turnCounter = 0;
        gameOver = false;

        System.out.println("WELCOME LADIES AND GENTLEMEN TO ANOTHER GAME OF CAAAAAAPTURE THEEEEE FLAAAAAG");
        System.out.println("please introduce the following information : ");

        playerCreation();

        //play rounds until game end

        while (!gameOver) {

            System.out.println("playing next round in 3 seconds");

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            playRound();
        }

        handleAfterGame();
    }

    /**
     * method to handle what happens after a game is run through and finished.
     * @throws EmptyCollectionException
     * @throws IOException
     * @throws NoSuchIndexException
     * @throws InterruptedException
     */
    public void handleAfterGame() throws EmptyCollectionException, IOException, NoSuchIndexException, InterruptedException {

        int option;
        Scanner scanner = new Scanner(System.in);
        System.out.println("would you like to play a new game?\n 1 - yes\n 2 - no\n 3 - Export Map");
        option = scanner.nextInt();
        switch (option) {
            case 1 -> startGame();
            case 2 -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            case 3 -> {
                System.out.println("Please provide a name for your map!");
                scanner.nextLine();
                String mapName = scanner.nextLine();
                this.gameMap.exportMap(mapName);
                System.out.println("ok byyyyyeeeeeeee");
            }

            default -> {
                System.out.println("Invalid option. Exiting...");
                System.exit(1);
            }
        }
    }


    /**
     * Method used to assign bots to players, depending on map size.
     * if the map has 15 or less vertices, each player is allowed 2 bots,
     * otherwise they're both allowed 3 bots
     */
    public void AssignBots() {
        int numberOfBots = 2; //default
        if (gameMap.size() > 15) { //for maps with more than 15 vertices, each player is allowed 3 bots
            numberOfBots = 3;

        } else if (gameMap.size() <= 15) { //each player is allowed 2 bots for maps with 10-15 vertices.
            System.out.println("Only two bots for each side are allowed, proceeding with selection.");
        }
        player1.setUniquePlayerBots(numberOfBots);
        player2.setUniquePlayerBots(numberOfBots);
    }


    /**
     * method for creating the map object depending on each decision from the players
     * @return Map type object
     */
    public Map createMap() {

        int numberOfVertices;
        int density;
        int option;
        boolean bidirectional = true;
        Scanner scanner = new Scanner(System.in);


        System.out.println("Would you like to import a map?\n 1- yes \n 2- no");
        option = scanner.nextInt();
        while (option != 1 && option != 2) {
            System.out.println("1 or 2!");
            option = scanner.nextInt();
        }
        switch (option) {

            case 1:

                System.out.println("Please provide the path for your map.");
                String filepath;
                scanner.nextLine();
                filepath = scanner.nextLine();
                return importMap(filepath);

            case 2:
                break;
        }

        System.out.println("Number of vertices in the map (max of 30, minimum of 10)");
        numberOfVertices = scanner.nextInt();
        while (numberOfVertices < 10 || numberOfVertices > 30) {
            System.out.println("Between 10 and 30 vertices please!");
            numberOfVertices = scanner.nextInt();
        }

        System.out.println("Please introduce the density for the map (minimum of 40, maximum of 70)");
        density = scanner.nextInt();
        while (density < 40 || density > 70) {
            System.out.println("Between 40 and 70 you dingus!!");
            density = scanner.nextInt();
        }

        System.out.println("Do you want the map to be bidirectional? 1 - yes, 2 - no.");
        option = scanner.nextInt();
        while (option != 1 && option != 2) {
            System.out.println("Either option 1 or 2");
            option = scanner.nextInt();
        }

        if (option == 1) {
            bidirectional = true;
        }
        if (option == 2) {
            bidirectional = false;
        }

        return new Map(numberOfVertices, density, bidirectional);
    }


    /**
     * method that decides which player moves.
     *
     * @return 1 or 2
     */
    private int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(1) + 1;
    }


    /**
     * method that handles the movement of bots per rounds, one bot will move sequentially one after
     * the other.
     *
     * @throws EmptyCollectionException if there's no vertices
     * @throws NoSuchIndexException     if there's no bots
     */
    public void playRound() throws EmptyCollectionException, NoSuchIndexException, InterruptedException {
        int playerToMove = getRandomNumber(); // decides which player will move first
        QueueADT<Players> playerQueue = new LinkedQueue<>();
        switch (playerToMove) {
            case 1 -> {
                playerQueue.enqueue(player1);
                playerQueue.enqueue(player2);
            }
            case 2 -> {
                playerQueue.enqueue(player2);
                playerQueue.enqueue(player1);
            }
        }

        while (!gameOver) {
            Players currentPlayer = playerQueue.dequeue();
            BigBot currentPlayerBot = currentPlayer.getNextBot();
            currentPlayerBot.move();
            System.out.println((currentPlayer.getPlayerName() + "'s " + currentPlayerBot.getBotName() + " has moved to index "
                    + currentPlayer.getCurrentBotIndex() + "\n"));
            handleGameOver(player1, player2);
            playerQueue.enqueue(currentPlayer);
            TimeUnit.SECONDS.sleep(2);
        }

    }



    /**
     * method to print out who won and change gameover status
     *
     * @param player1
     * @param player2
     * @throws EmptyCollectionException
     */
    private void handleGameOver(Players player1, Players player2) throws EmptyCollectionException {
        if (player1.checkCapturedFlag(player1.getCurrentBotIndex())) {
            gameOver = true;
            System.out.println("Player " + player1.getPlayerName() + " wins.");
        } else if (player2.checkCapturedFlag(player2.getCurrentBotIndex())) {
            gameOver = true;
            System.out.println("Player " + player2.getPlayerName() + " wins.");
        }
    }

    /**
     * void method to create two players for each game instance
     */
    private void playerCreation() throws NoSuchIndexException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Player 1, please enter your name.");
        String player1Name = scanner.nextLine();
        System.out.println("Player 2, please enter your name.");
        String player2Name = scanner.nextLine();

        CreateMapInGameFlow();


        System.out.println("Please pick a vertex for your flag," + player1Name + " !");
        Flag flag1 = new Flag(scanner.nextInt());
        System.out.println("Please pick a vertex for your flag," + player2Name + " !");
        Flag flag2 = new Flag(scanner.nextInt());

        player1 = new Players(player1Name, gameMap, flag1, flag2);
        player2 = new Players(player2Name, gameMap, flag2, flag1);

        AssignBots();
    }

    /**
     * method to print the map in the game
     */
    public void CreateMapInGameFlow() throws NoSuchIndexException {
        this.gameMap = createMap();
        if (this.gameMap.getDensity() != 0) {
            gameMap.createMap();
        }

        System.out.println("Here's your map!\n");
        gameMap.printMap();
    }

    /**
     * method to import the map from file to the game
     * @param filepath filepath to the file where the map information is in.
     * @return map object
     */
    public Map importMap(String filepath) {
        GameNode[] nodes = new GameNode[0];
        int maxVertices = 0;
        double[][] adjacencyMatrix = new double[0][];
        try {
            Scanner scanner = new Scanner(new File(filepath));
            int numRows = 0;
            int numCols = 0;

            // Count the number of rows and columns in the matrix
            while (scanner.hasNextLine()) {
                numRows++;
                String[] row = scanner.nextLine().trim().split("\\s+");
                numCols = Math.max(numCols, row.length);
            }

            // Reset the scanner to the beginning of the file
            scanner.close();
            scanner = new Scanner(new File(filepath));

            // Create the adjacency matrix
            adjacencyMatrix = new double[numRows][numCols];

            // Populate the matrix
            for (int i = 0; i < numRows; i++) {
                String[] row = scanner.nextLine().trim().split("\\s+");
                for (int j = 0; j < numCols; j++) {
                    if (row[j].equalsIgnoreCase("Infinity")) {
                        adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
                    } else {
                        adjacencyMatrix[i][j] = Double.parseDouble(row[j]);
                    }
                }
            }

            maxVertices = numRows;
            nodes = new GameNode[maxVertices];
            for (int i = 0; i < maxVertices; i++) {
                GameNode node = new GameNode(i);
                nodes[i] = node;
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return new Map(maxVertices, adjacencyMatrix, nodes);
    }

}
