package menus;

import Authentication.*;
import Enums.UserProfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private final AuthSystem authSystem = new AuthSystem();
    private boolean isRunning = true;

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.start();
    }

    /**
     * this method starts the menu tree for the client to use.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();


            switch (choice) {
                case "1" : handleRegister(scanner);break;
                case "2" : handleLogin(scanner);break;
                case "3" :
                    System.out.println("Exiting application. Goodbye!");
                    isRunning = false;
                    break;
                default : System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * This method determines the String that represents the chat commands.
     * the only thing that changes from one to another is the chat name.
     *
     * @param groupname - name of the group the commands are being printed to
     * @return
     */
    private String determineChatCommands(String groupname){
         String commands = " Welcome to " + groupname + " chat! List of commands : \n" +
                " - exit: exit to leave the chat. \n" +
                " - commands: to repeat this message. \n" +
                " - EMERGENCY_COMS : to create a EMERGENCY_COMS notification or request \n" +
                " - EMERGENCY_RESOURCES : to create a EMERGENCY_RESOURCES notification or request \n" +
                " - MASS_EVACUATION : to create a MASS_EVACUATION notification or request.";
         return commands;
    }

    /**
     * prints the commands string based on the group the client is in.
     * @param group
     */
    private void printCommands(int group) {
        switch (group) {
            case 0:
                System.out.println(determineChatCommands("all"));
                break;
            case 1:
                System.out.println(determineChatCommands("low"));
                break;
            case 2:
                System.out.println(determineChatCommands("mid"));
                break;
            case 3:
                System.out.println(determineChatCommands("high"));
                break;
        }
    }


    /** method that manages the first menu after the user succesfully logs in.
     *
     *
     * @param scanner
     * @param loggedInUser
     */
    private void loggedInMenu(Scanner scanner, User loggedInUser) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=== Welcome, " + loggedInUser.getName() + " ===");
            System.out.println("1. Choose Groupchat");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" : handleChooseGroupChat(loggedInUser); break;
                case "2" :
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default : System.out.println("Invalid choice. Please try again.");
            }
        }
    }





    /**
     * Handles the all-chat communication functionality for a logged-in user.
     * This method manages UDP-based group chat communication, including:
     * - Joining the chat
     * - Receiving and sending messages
     * - Handling special commands
     * - Managing chat session
     *
     * @param loggedInUser The user participating in the all-chat
     */
    private void handleAllChat(User loggedInUser) {

        printChatHistory("0");

        printCommands(0);
        startNotificationListener(loggedInUser, 0);


        try (DatagramSocket socket = new DatagramSocket()) {

            // Set up server connection details
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPortUDP = 4446;

            // Broadcast join message to all chat participants
            String joinMessage = loggedInUser.getName() + ":0:has joined the all chat!";
            byte[] joinData = joinMessage.getBytes();
            DatagramPacket joinPacket = new DatagramPacket(joinData, joinData.length, serverAddress, serverPortUDP);
            socket.send(joinPacket);


            // Create a separate thread to listen for incoming messages
            Thread listenerThread = new Thread(() -> {
                byte[] buffer = new byte[3000];
                while (!socket.isClosed()) {
                    try {
                        // Receive incoming messages
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        // Convert received data to string and print
                        String received = new String(packet.getData(), 0, packet.getLength()).trim();
                        System.out.println(received); // Print received messages
                    } catch (IOException e) {
                        // Handle disconnection
                        System.out.println(loggedInUser + "Disconnected from chat.");
                        break;
                    }
                }
            });
            listenerThread.start();


            // Main loop for user input
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();

                // Process user input and commands
                switch (message.toUpperCase()) {
                    case "COMMANDS":
                        printCommands(0);
                        break;
                    case "EXIT":
                        System.out.println("Leaving chat...");
                        String leaveMessage = "A user has left the chat.";
                        byte[] leaveData = leaveMessage.getBytes();
                        DatagramPacket leavePacket = new DatagramPacket(leaveData, leaveData.length, serverAddress, serverPortUDP);
                        socket.send(leavePacket);
                        socket.close();
                        return; // or break out of the method
                    case "EMERGENCY_COMS":
                        handleChatTCPRequest("EMERGENCY_COMS", "0", loggedInUser.getName());
                        break;
                    case "EMERGENCY_RESOURCES":
                        handleChatTCPRequest("EMERGENCY_RESOURCES", "0", loggedInUser.getName());
                        break;

                    case "MASS_EVACUATION":
                        handleChatTCPRequest("MASS_EVACUATION", "0", loggedInUser.getName());
                        break;

                    default:
                        String chatMessage = loggedInUser + ":0:" + message;
                        byte[] chatData = chatMessage.getBytes();
                        DatagramPacket chatPacket = new DatagramPacket(chatData, chatData.length, serverAddress, serverPortUDP);
                        socket.send(chatPacket);
                        break;
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Handles the medium-chat communication functionality for a logged-in user.
     * This method manages UDP-based group chat communication, including:
     * - Joining the chat
     * - Receiving and sending messages
     * - Handling special commands
     * - Managing chat session
     *
     * @param loggedInUser The user participating in the all-chat
     */
    private void handleMediumChat(User loggedInUser) {

        printChatHistory("2");

        printCommands(2);
        startNotificationListener(loggedInUser, 2);



        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost"); // Adjust server IP if needed
            int serverPortUDP = 4446; // estou a ouvir no UDP server

            // digo ao groupchat q me juntei
            String joinMessage = loggedInUser + " has joined the medium chat!";
            byte[] joinData = joinMessage.getBytes();
            DatagramPacket joinPacket = new DatagramPacket(joinData, joinData.length, serverAddress, serverPortUDP);
            socket.send(joinPacket);

            // Crio a thread para ouvir a mensagens
            Thread listenerThread = new Thread(() -> {
                byte[] buffer = new byte[3000];
                while (!socket.isClosed()) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String received = new String(packet.getData(), 0, packet.getLength());
                        System.out.println(received); // Print received messages
                    } catch (IOException e) {
                        System.out.println( loggedInUser + "Disconnected from chat.");
                        break;
                    }
                }
            });
            listenerThread.start();

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();

                switch (message.toUpperCase()) {
                    case "COMMANDS":
                        printCommands(0);
                        break;
                    case "EXIT":
                        System.out.println("Leaving chat...");
                        String leaveMessage = "A user has left the chat.";
                        byte[] leaveData = leaveMessage.getBytes();
                        DatagramPacket leavePacket = new DatagramPacket(leaveData, leaveData.length, serverAddress, serverPortUDP);
                        socket.send(leavePacket);
                        socket.close();
                        return; // or break out of the method
                    case "EMERGENCY_COMS":
                        handleChatTCPRequest("EMERGENCY_COMS", "2", loggedInUser.getName());
                        break;
                    case "EMERGENCY_RESOURCES":
                        handleChatTCPRequest("EMERGENCY_RESOURCES", "2", loggedInUser.getName());
                        break;

                    case "MASS_EVACUATION":
                        handleChatTCPRequest("MASS_EVACUATION", "2", loggedInUser.getName());
                        break;

                    default:
                        String chatMessage = loggedInUser + ":2:" + message;
                        byte[] chatData = chatMessage.getBytes();
                        DatagramPacket chatPacket = new DatagramPacket(chatData, chatData.length, serverAddress, serverPortUDP);
                        socket.send(chatPacket);
                        break;
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Handles the low-chat communication functionality for a logged-in user.
     * This method manages UDP-based group chat communication, including:
     * - Joining the chat
     * - Receiving and sending messages
     * - Handling special commands
     * - Managing chat session
     *
     * @param loggedInUser The user participating in the all-chat
     */
    private void handleLowChat(User loggedInUser) {



        // Load and display chat history
        printChatHistory("1");

        printCommands(1);
        startNotificationListener(loggedInUser, 1);

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost"); // Adjust server IP if needed
            int serverPortUDP = 4446; // estou a ouvir no UDP server

            // digo ao groupchat q me juntei
            String joinMessage = loggedInUser.getName() + " has joined the low chat!";
            byte[] joinData = joinMessage.getBytes();
            DatagramPacket joinPacket = new DatagramPacket(joinData, joinData.length, serverAddress, serverPortUDP);
            socket.send(joinPacket);

            // Crio a thread para ouvir a mensagens
            Thread listenerThread = new Thread(() -> {
                byte[] buffer = new byte[3000];
                while (!socket.isClosed()) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String received = new String(packet.getData(), 0, packet.getLength());
                        System.out.println(received); // Print received messages
                    } catch (IOException e) {
                        System.out.println( loggedInUser.getName() + "Disconnected from chat.");
                        break;
                    }
                }
            });
            listenerThread.start();

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();

                switch (message.toUpperCase()) {
                    case "COMMANDS":
                        printCommands(1);
                        break;
                    case "EXIT":
                        System.out.println("Leaving chat...");
                        String leaveMessage = "A user has left the chat.";
                        byte[] leaveData = leaveMessage.getBytes();
                        DatagramPacket leavePacket = new DatagramPacket(leaveData, leaveData.length, serverAddress, serverPortUDP);
                        socket.send(leavePacket);
                        socket.close();
                        return; // or break out of the method
                    case "EMERGENCY_COMS":
                        handleChatTCPRequest("EMERGENCY_COMS", "1", loggedInUser.getName());
                        break;
                    case "EMERGENCY_RESOURCES":
                        handleChatTCPRequest("EMERGENCY_RESOURCES", "1", loggedInUser.getName());
                        break;

                    case "MASS_EVACUATION":
                        handleChatTCPRequest("MASS_EVACUATION", "1", loggedInUser.getName());
                        break;

                    default:
                        String chatMessage = loggedInUser + ":1:" + message;
                        byte[] chatData = chatMessage.getBytes();
                        DatagramPacket chatPacket = new DatagramPacket(chatData, chatData.length, serverAddress, serverPortUDP);
                        socket.send(chatPacket);
                        break;
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the high-chat communication functionality for a logged-in user.
     * This method manages UDP-based group chat communication, including:
     * - Joining the chat
     * - Receiving and sending messages
     * - Handling special commands
     * - Managing chat session
     *
     * @param loggedInUser The user participating in the all-chat
     */
    private void handleHighChat(User loggedInUser) {



        printChatHistory("3");


        printCommands(3);
        startNotificationListener(loggedInUser, 3);// para receber notificaçoes do server

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost"); // Adjust server IP if needed
            int serverPortUDP = 4446; // estou a ouvir no UDP server

            // digo ao groupchat q me juntei
            String joinMessage = loggedInUser.getName() + " has joined the all chat!";
            byte[] joinData = joinMessage.getBytes();
            DatagramPacket joinPacket = new DatagramPacket(joinData, joinData.length, serverAddress, serverPortUDP);
            socket.send(joinPacket);

            // Crio a thread para ouvir a mensagens
            Thread listenerThread = new Thread(() -> {
                byte[] buffer = new byte[3000];
                while (!socket.isClosed()) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String received = new String(packet.getData(), 0, packet.getLength());
                        System.out.println(received); // Print received messages
                    } catch (IOException e) {
                        System.out.println( loggedInUser.getName() + "Disconnected from chat.");
                        break;
                    }
                }
            });
            listenerThread.start();


            while (true) {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();

                switch (message.toUpperCase()) {
                    case "COMMANDS":
                        printCommands(3);
                        break;
                    case "EXIT":
                        System.out.println("Leaving chat...");
                        String leaveMessage = "A user has left the chat.";
                        byte[] leaveData = leaveMessage.getBytes();
                        DatagramPacket leavePacket = new DatagramPacket(leaveData, leaveData.length, serverAddress, serverPortUDP);
                        socket.send(leavePacket);
                        socket.close();
                        return; // or break out of the method
                    case "EMERGENCY_COMS":
                        handleChatTCPRequest("EMERGENCY_COMS", "3", loggedInUser.getName());
                        break;
                    case "EMERGENCY_RESOURCES":
                        handleChatTCPRequest("EMERGENCY_RESOURCES", "3", loggedInUser.getName());
                        break;

                    case "MASS_EVACUATION":
                        handleChatTCPRequest("MASS_EVACUATION", "3", loggedInUser.getName());
                        break;

                    default:
                        String chatMessage = loggedInUser + ":3:" + message;
                        byte[] chatData = chatMessage.getBytes();
                        DatagramPacket chatPacket = new DatagramPacket(chatData, chatData.length, serverAddress, serverPortUDP);
                        socket.send(chatPacket);
                        break;
                }
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Notifies the server about a specific user's notification port for a given group.
     *
     * This method establishes a TCP connection to a local server on port 4447 and sends
     * a message containing the user's name, group ID, and designated notification port.
     * The method is typically used to register or update the communication port
     * for receiving notifications for a specific user and group.
     *
     * @param user The User object representing the user whose notification port is being set
     * @param group The group identifier associated with the notification port
     * @param port The port number to be used for receiving notifications
     */
    private void notifyServerOfNotificationPort(User user, int group, int port) {
        try {
            Socket socket = new Socket("localhost", 4447); // this is the TCP port
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send a special message to the server with the notification port
            out.println(user.getName() + ":" + group + ":SET_NOTIFICATION_PORT:" + port);

            String response = in.readLine();
            System.out.println("Server response to port notification: " + response);

            socket.close();
        } catch (IOException e) {
            System.err.println("Could not notify server of notification port: " + e.getMessage());
        }
    }


    /**
     * launches a thread for the loggedinuser in a specific group,
     * in a random port, and waits for the notifications from the TCPserver.
     * @param loggedInUser
     * @param group
     */
    private void startNotificationListener(User loggedInUser, int group) {
        Thread notificationListenerThread = new Thread(() -> {
            try {
                // Create a ServerSocket on an available port
                ServerSocket notificationServer = new ServerSocket(0);
                int listeningPort = notificationServer.getLocalPort();

                // Optional: Send this port to the server so it knows where to reach the client
                notifyServerOfNotificationPort(loggedInUser, group , listeningPort);

                System.out.println("Notification listener started on port: " + listeningPort);

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket clientSocket = notificationServer.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String message = in.readLine();

                        // Handle different types of notifications
                        System.out.println("NOTIFICATION RECEIVED: " + message);

                        clientSocket.close();
                    } catch (IOException e) {
                        System.err.println("Error in notification listener: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not start notification listener: " + e.getMessage());
            }
        });

        // Set as a daemon thread so it doesn't prevent application exit
        notificationListenerThread.setDaemon(true);
        notificationListenerThread.start();
    }

    /**
     * handles the TCPRequests from the clients.
     * @param command
     * @param group
     * @param name
     */
    private void handleChatTCPRequest(String command,String group, String name ) {
        try {
            // Create a socket connection to the TCP server
            Socket socket = new Socket("localhost", 4447); // host would be the IP of the server if I was running this in different computers.

            // Create input and output streams
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Construct the request string in the format: username:groupId:command:toUsername
            String request = name + ":" + group + ":" + command + ":";

            output.println(request);
            output.flush();  // Explicitly flush the output stream


            // Read the response
            String response = input.readLine();


            System.out.println("Debug - Request sent: " + request);
            System.out.println("Debug - Response received: " + response);

            if (response == null) {
                System.err.println("No response received from server");
                // Additional error handling
            }
            // Handle the response (you might want to update UI or show a message)
            switch (command) {
                case "EMERGENCY_COMS":
                    if(response.equals("Emergency coms activated")){
                        System.out.println("emergency coms have been activated!");
                    }
                    break;
                case "EMERGENCY_RESOURCES":
                    if(response.equals("Emergency Resources Have Been Distributed.")){
                        System.out.println("Emergency Resources Have Been Distributed.");
                    }
                case "MASS_EVACUATION":
                    if(response.equals("MASS EVACUATION HAS BEEN ISSUED.")){
                        System.out.println("Mass Evacuation has been succesfully generated.");
                    }
            }

            // Close the socket
            socket.close();

        } catch (IOException e) {
            // Handle connection errors
            e.printStackTrace();
            System.out.println("Failed to connect to server");
        }
    }

    /**
     * this is the method that handles the registering menu and functionality.
     * @param scanner
     */
    private void handleRegister(Scanner scanner) {
        System.out.println("\n--- Registration ---");
        System.out.print("Enter Username: ");
        String userId = scanner.nextLine();

        System.out.println("Choose profile:");
        for (UserProfiles profile : UserProfiles.values()) {
            System.out.println("- " + profile.name());
        }
        System.out.print("Enter profile: ");
        String profileInput = scanner.nextLine().toUpperCase();

        UserProfiles profile;
        try {
            profile = UserProfiles.valueOf(profileInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid profile. Registration canceled.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        RegisterResult result = authSystem.register(userId, userId, profile, password);

        if (result.isSuccess()) {
            System.out.println("Registration successful! Welcome, " + result.getNewUser().getName());
        } else {
            System.out.println("Registration failed: " + result.getMessage());
        }
    }


    /**
     * this method manages the menu for logging in and the functionality for it.
     * @param scanner
     */
    private void handleLogin(Scanner scanner) {
        System.out.println("\n--- Login ---");


        System.out.print("Enter user ID: ");
        String usernameId = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        LoginResult result = authSystem.login(usernameId, password);

        if (result.isSuccess()) {
            System.out.println("Login successful! Welcome back, " + result.getUser().getName());
            // Start the notification listener when login is successful - tiro isto daqui e movo para os groupchats.
            // startNotificationListener(result.getUser());
            loggedInMenu(scanner, result.getUser());
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    /**
     * menu responsible for letting the user navigate to different menus.
     * @param loggedInUser
     */
    private void handleChooseGroupChat(User loggedInUser) {
        Scanner groupscanner = new Scanner(System.in);

        System.out.println("Please enter The Groupchat you wish to enter, keep in mind \n" +
                "1- all chat \n" +
                "2 - low chat \n" +
                "3 - medium chat \n" +
                "4 - high chat " +
                "all other numbers will be invalid.");

        String choice = groupscanner.nextLine();

        switch (choice) {
            case "1" : handleAllChat(loggedInUser);
            case "2" : handleLowChat(loggedInUser);
            case "3" : handleMediumChat(loggedInUser);
            case "4" : handleHighChat(loggedInUser);
            default : handleChooseGroupChat(loggedInUser);

        }
    }

    /**
     * method responsible for printing last 15 messages from the log files.
     * @param file - name of the file we want to represent.
     */
    private void printChatHistory(String file){
        try {
            Path historyFile = Paths.get("group_"+file+"_log.txt");
            if (Files.exists(historyFile)) {
                System.out.println("\n--- Chat History ---");
                List<String> chatHistory = Files.readAllLines(historyFile);

                // Display last 15 messages
                int startIndex = Math.max(0, chatHistory.size() - 15);
                for (int i = startIndex; i < chatHistory.size(); i++) {
                    System.out.println(chatHistory.get(i));
                }
                System.out.println("--- End of Chat History ---\n");
            } else {
                System.out.println("No chat history found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading chat history: " + e.getMessage());
        }
    }

}
