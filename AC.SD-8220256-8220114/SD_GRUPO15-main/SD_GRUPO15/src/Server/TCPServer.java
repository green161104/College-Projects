package Server;


import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Authentication.User;
import Authentication.UserInfo;
import Enums.UserProfiles;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class TCPServer {
    private static final int TCP_PORT = 4447;

    private final ServerChatgroups context;
    private ExecutorService executorService;
    private Map<String, PendingRequest> pendingRequests;


    public TCPServer(ServerChatgroups context) {
        this.context = context;
        this.executorService = Executors.newCachedThreadPool();
        this.pendingRequests = new ConcurrentHashMap<>();
    }


    /**
     * Starts the TCP server to handle incoming client connections.
     * This method sets up a server socket on the specified TCP port and listens
     * for incoming client connections. For each client, it accepts the connection
     * and delegates the handling of the client to a separate thread using an
     * {@link ExecutorService}.
     *
     * @throws IOException if an I/O error occurs while opening the server socket
     *                     or handling client connections.
     *
     * Workflow:
     *     Create a {@link ServerSocket} bound to the specified TCP port.
     *     Log server startup details.
     *     >Enter an infinite loop to accept and handle client connections.
     *     >For each client connection:
     *             Accept the client socket.
     *             Log the connection details, including the client's IP address,
     *                 port, and socket state.
     *             Submit a task to the executor service to handle the client
     *                 connection asynchronously.
     *.
     * The server remains active until it is manually stopped. Graceful handling
     * of client disconnections and unexpected errors is implemented to ensure
     * server stability.
     *
     * Key Details:
     *     Uses an {@link ExecutorService} for managing client handling threads,
     *         ensuring scalable and efficient handling of multiple concurrent
     *         clients.</li>
     *    Logs socket details, such as connection state, input/output state,
     *         and client information.</li>
     *     Ensures proper cleanup of resources in case of exceptions.

     */
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("[TCP] Request Server started on port " + TCP_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[TCP] New client connection from: " +
                        clientSocket.getInetAddress().getHostAddress() +
                        ":" + clientSocket.getPort());

                // Log initial socket details
                System.out.println("[TCP] Socket details:");
                System.out.println("  - Is Connected: " + clientSocket.isConnected());
                System.out.println("  - Is Input Shutdown: " + clientSocket.isInputShutdown());
                System.out.println("  - Is Output Shutdown: " + clientSocket.isOutputShutdown());

                executorService.submit(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (Exception e) {
                        System.err.println("[TCP] Error handling client: " + e.getMessage());
                        e.printStackTrace();
                        try {
                            clientSocket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private UserInfo findUserInfo(String usernameID) {
        for (UDPGroupChat group : context.getChatGroups().values()) {
            for (UserInfo user : group.getLoggedUsers()) {
                if (user.getUsername().equals(usernameID)) {
                    return user;
                }
            }
        }
        System.out.println("no user found in finduserinfo");
        return null;
    }
    /**
     * Handles the finding or creating a logged in user.
     * it'll take in the in userID, the groupID where he is and a client socket, verify if the user is in the context,
     * and if it isn't, it'll then add the user to the context.
     */
    private UserInfo findOrCreateUserInfo(String userID, String groupId, Socket clientSocket) throws IOException {
        // First, try to find existing user
        UserInfo existingUser = findUserInfo(userID);
        if (existingUser != null) {
            return existingUser;
        }

        int groupIdInt = Integer.parseInt(groupId); // converto de string para integer. TESTAR again
        System.out.println("no metodo findorcreateuserinfo tenho o groupID " + groupIdInt);
        // If user not found, create a new UserInfo
        try {
            // Use the client's socket information to create UserInfo
            InetAddress clientAddress = clientSocket.getInetAddress();
            int clientPort = clientSocket.getPort();

            // Retrieve user profile (you might need to adjust this based on your authentication system)
            System.out.println(userID + " this is usernameID in findorcreateuserinfo");
            UserProfiles profile = context.getAuthSystem().getUserProfile(userID);

            // Create new UserInfo
            UserInfo newUser = new UserInfo(userID, profile, clientAddress, clientPort);

            // Add user to the specified group
            UDPGroupChat group = context.getChatGroups().get(groupIdInt);
            group.addUser(newUser);
            System.out.println(group.getLoggedUsers() + " in group " + groupIdInt);

            System.out.println("New user added to group " + groupId + ": " + userID);

            return newUser;
        } catch (Exception e) {
            System.err.println("Error creating user info: " + e.getMessage());
            return null;
        }
    }

    private List<String> loadGroupChatHistory(String groupId) {
        List<String> chatHistory = new ArrayList<>();

        try {

            Path historyFile = Paths.get("group_" + groupId + "_logs" + ".txt");

            if (Files.exists(historyFile)) {
                // Read the last N messages (e.g., last 50)
                List<String> allLines = Files.readAllLines(historyFile);
                int startIndex = Math.max(0, allLines.size() - 50); // Get last 50 messages

                for (int i = startIndex; i < allLines.size(); i++) {
                    chatHistory.add(allLines.get(i));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading chat history for group " + groupId);
            e.printStackTrace();
        }

        return chatHistory;
    }

    /**
     * Handles communication with a single client connected via a TCP socket.
     *
     * This method reads and processes the client's request, validates the format,
     * and executes appropriate commands based on the request details. It ensures
     * proper resource management and sends structured responses to the client.
     *
     * @param clientSocket the socket associated with the connected client,
     *                     used to read requests and send responses.
     *
     * Main Workflow:
     * 1. Configures the client socket with a timeout to prevent blocking indefinitely.
     * 2. Initializes input and output streams for client communication.
     * 3. Reads the client's request and checks for validity.
     * 4. Parses the request into parts and validates its structure.
     * 5. Executes specific commands based on the request:
     *    - "SET_NOTIFICATION_PORT": Updates the user's notification port.
     *    - "EMERGENCY_COMS": Handles emergency communication tasks.
     *    - "EMERGENCY_RESOURCES": Manages emergency resource requests.
     *    - "MASS_EVACUATION": Executes mass evacuation commands.
     *    - Any other command is treated as unknown and the client is notified.
     * 6. Logs relevant debug and operational information during the process.
     *
     * Error Handling:
     * - Catches and logs any I/O exceptions that occur during request handling.
     * - Ensures the client socket is closed in the `finally` block to release resources.
     *
     * Example Commands:
     * - "username:group:SET_NOTIFICATION_PORT:port": Updates notification port.
     * - "username:group:EMERGENCY_COMS": Handles emergency communications.
     *
     * Notes:
     * - The method assumes the request string is colon-delimited with specific formats.
     * - Unexpected input formats or commands are handled gracefully with appropriate responses.
     *
     * Exceptions:
     * - IOException: If there is an error in reading input or writing output, or when closing the socket.
     */
    private void handleClient(Socket clientSocket) {

        System.out.println("[TCP] Entering handleClient method");
        try {

            clientSocket.setSoTimeout(5000); // 5-second timeout
            InputStream rawInput = clientSocket.getInputStream();       // this is raw directly first

            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            System.out.println("[TCP] Streams created, waiting for input");

            System.out.println("DEBUG: Input stream available: " + input.ready());


            // Read the request
            String request = input.readLine();
            System.out.println("DEBUG: Received raw request: " + request);

            if (request == null || request.trim().isEmpty()) {
                System.out.println("DEBUG: no request received?");
                output.println("No request received");
                return;
            }

            String[] parts = request.split(":"); // must be 3 parts joao:group:command:

            if (parts.length >= 4 && parts[2].equals("SET_NOTIFICATION_PORT")) {
                String username = parts[0];
                int notificationPort = Integer.parseInt(parts[3]);

                // Find and update the user's notification port
                System.out.println("this is what I'm sending to findorcreateuserinfo method");
                UserInfo user = findOrCreateUserInfo(username, parts[1], clientSocket);
                user.setNotificationPort(notificationPort);

                output.println("Notification port updated successfully");
                return;
            }

            if (parts.length > 3) {
                output.println("Invalid request format");
                return;
            }

            System.out.println("the request IS valid in handleClient. ");
            System.out.println("the request came from groupchat number " + parts[1]);
            String fromUsernameId = parts[0]; //username making the request
            String command = parts[2]; //command in the request
            // String toUsername = parts[3]; //username of the person we're making the request to

            // Find the user's UserInfo
            UserInfo requester = findOrCreateUserInfo(fromUsernameId, parts[1], clientSocket);

            System.out.println("[TCP] Found requester: " + (requester != null ? requester.getUsername() : "null"));

            switch (command) {
                case "EMERGENCY_COMS":
                    System.out.println("I got here!");
                    handleEmergencyComs(fromUsernameId, output, requester);
                    break;
                case "EMERGENCY_RESOURCES":
                    handleEmergencyResources(fromUsernameId, output, requester);
                    break;
                case "MASS_EVACUATION":
                    handleMassEvacuation(fromUsernameId, output, requester);
                    break;
                default:
                    output.println("Unknown command");
            }
        } catch (IOException e) {
            System.err.println("[TCP] Exception in handleClient: " + e.getClass().getName());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("[TCP] Error closing socket");
                e.printStackTrace();
            }
        }
    }

    // only HIGH profiles can do this, otherwise I must make a request.

    /**
     * Handles MASS_EVACUATION Requests.
     *
     *
     * @param fromUsernameId ID of the responsible user for the request.
     * @param output printWriter to write the response in.
     * @param requester info of the user requesting the command
     *
     * Main Workflow:
     * 1. verifies the users profile to ensure the user has enough authority for doing the command.
     * 2. if the user doesn't have enough permissions, it'll then print out the command to groupchat HIGH as a request.
     * 3. if the user does have enough premissions ( HIGH userprofile ) it'll then make use of the notificationSocket to send the warning to all users.
     *
     *
     * Notes:
     * - The method assumes the request string is colon-delimited with specific formats.
     * - Unexpected input formats or commands are handled gracefully with appropriate responses.
     *
     * Exceptions:
     * - IOException: If there is an error in reading input or writing output, or when closing the socket.
     */
    private void handleMassEvacuation(String fromUsernameId, PrintWriter output, UserInfo requester) {

        if (requester.getProfile() == UserProfiles.LOW || requester.getProfile() == UserProfiles.MEDIUM) {
            output.println("You do not have permission to use this command, however a request was made to the high groupchat.");
            System.out.println(requester.getProfile() + "this is your userprofile.");

            // Só quero mandar para o grupo 2 ( high )
            int targetGroupId = 3;

            for (Map.Entry<Integer, UDPGroupChat> entry : context.getChatGroups().entrySet()) {
                if (entry.getKey() != targetGroupId) { // if key not 3
                    continue; // dou skip a não high chats.
                }

                UDPGroupChat group = entry.getValue(); // pego no grupo HIGH com grupoID 3
                for (UserInfo user : group.getLoggedUsers()) { // users online no grupo 3

                    if (user.getAddress() == null || user.getPort() <= 0) {
                        System.err.println("Invalid address or port for user: " + user.getUsername());
                        continue; // Skip this user
                    }
                    try {
                        Socket notificationSocket = new Socket(); // create a socket
                        notificationSocket.connect(new InetSocketAddress(user.getAddress(), user.notificationPort), 5000); // 5-second timeout

                        try (PrintWriter userOut = new PrintWriter(notificationSocket.getOutputStream(), true)) {
                            userOut.println("MASS_EVACUATION_REQUESTED_BY:" + fromUsernameId + ":Group:" + entry.getKey());
                        } finally {
                            notificationSocket.close();
                        }

                    } catch (ConnectException e) {
                        System.err.println("Connection refused when trying to send emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    } catch (SocketTimeoutException e) {
                        System.err.println("Connection timeout for user " + user.getUsername());
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.err.println("IO Error sending emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    }
                }
            }
        }

        if (requester.getProfile() == UserProfiles.HIGH) {

            output.println("MASS EVACUATION HAS BEEN ISSUED.");

            // Broadcast emergency coms to all groups
            for (Map.Entry<Integer, UDPGroupChat> entry : context.getChatGroups().entrySet()) {  // for all 0-3 groups, I'm gonna
                UDPGroupChat group = entry.getValue(); // get the group ID
                for (UserInfo user : group.getLoggedUsers()) { //get all users online in that group

                    if (user.getAddress() == null || user.getPort() <= 0) {
                        System.err.println("Invalid address or port for user: " + user.getUsername());
                        continue; // Skip this user
                    }

                    try {
                        Socket notificationSocket = new Socket(); // create a socket
                        notificationSocket.connect(new InetSocketAddress(user.getAddress(), user.notificationPort), 5000); // 5-second timeout

                        try (PrintWriter userOut = new PrintWriter(notificationSocket.getOutputStream(), true)) {
                            userOut.println("MASS_EVACUATION_ACTIVATED_BY:" + fromUsernameId + ":Group" + entry.getKey());
                        } finally {
                            notificationSocket.close();
                        }

                    } catch (ConnectException e) {
                        System.err.println("Connection refused when trying to send emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    } catch (SocketTimeoutException e) {
                        System.err.println("Connection timeout for user " + user.getUsername());
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.err.println("IO Error sending emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * Handles the "EMERGENCY_RESOURCES" command issued by the user based on their profile.
     * - If the user's profile is LOW, the system only notifies the mid and high group chats (group 2 and 3).
     * - If the user's profile is MEDIUM or HIGH, emergency resources are broadcast to all groups.
     *
     * The method ensures that:
     * - Users with LOW profile do not directly distribute resources but send a request to the mid group chat.
     * - Users with MEDIUM or HIGH profiles actively distribute emergency resources to all groups, notifying users within those groups.
     *
     * @param fromUsernameId The username of the user initiating the request.
     * @param output The PrintWriter object used to send responses back to the user.
     * @param requester The UserInfo object containing information about the requesting user.
     */
    private void handleEmergencyResources(String fromUsernameId, PrintWriter output, UserInfo requester) {

        if (requester.getProfile() == UserProfiles.LOW) {
            output.println("You do not have permission to use this command, however a request was made to the mid groupchat.");
            System.out.println(requester.getProfile() + "this is your userprofile.");

            // Só quero mandar para o grupo 2 e 3 ( mid )
            int targetGroupIdMid = 2;
            int targetGroupIdHigh = 3;

            for (Map.Entry<Integer, UDPGroupChat> entry : context.getChatGroups().entrySet()) {
                if (entry.getKey() != targetGroupIdMid && entry.getKey() != targetGroupIdHigh) {
                    System.out.println("this is the amount of logged users in chat" + entry.getKey() + entry.getValue().getLoggedUsers());// aqui estou a ter 0 users logados, not sure why.
                    continue; // dou skip a não mid chats.
                }

                System.out.println("this is the amount of logged users in chat - outside of the if statement" + entry.getKey() + entry.getValue().getLoggedUsers());

                UDPGroupChat group = entry.getValue(); // get the group for group ID 2
                for (UserInfo user : group.getLoggedUsers()) { // get users online in group 2

                    if (user.getAddress() == null || user.getPort() <= 0) {
                        System.err.println("Invalid address or port for user: " + user.getUsername());
                        continue; // Skip this user
                    }
                    try {
                        Socket notificationSocket = new Socket(); // create a socket
                        notificationSocket.connect(new InetSocketAddress(user.getAddress(), user.notificationPort), 5000); // 5-second timeout

                        try (PrintWriter userOut = new PrintWriter(notificationSocket.getOutputStream(), true)) {
                            userOut.println("EMERGENCY_RESOURCES_REQUESTED_BY:" + fromUsernameId + ":Group:" + entry.getKey());
                            group.writeMessageToLog(fromUsernameId, "EMERGENCY_RESOURCES_REQUESTED_BY:" + fromUsernameId + ":Group:" + entry.getKey());
                        } finally {
                            notificationSocket.close();
                        }

                    } catch (ConnectException e) {
                        System.err.println("Connection refused when trying to send emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    } catch (SocketTimeoutException e) {
                        System.err.println("Connection timeout for user " + user.getUsername());
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.err.println("IO Error sending emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    }
                }
            }
        }

        if (requester.getProfile() == UserProfiles.MEDIUM || requester.getProfile() == UserProfiles.HIGH) {

            output.println("Emergency Resources Have Been Distributed.");

            // Broadcast emergency coms to all groups
            for (Map.Entry<Integer, UDPGroupChat> entry : context.getChatGroups().entrySet()) {  // for all 0-3 groups, I'm gonna
                UDPGroupChat group = entry.getValue(); // get the group ID
                for (UserInfo user : group.getLoggedUsers()) { //get all users online in that group

                    if (user.getAddress() == null || user.getPort() <= 0) {
                        System.err.println("Invalid address or port for user: " + user.getUsername());
                        continue; // Skip this user
                    }

                    try {
                        Socket notificationSocket = new Socket(); // create a socket
                        notificationSocket.connect(new InetSocketAddress(user.getAddress(), user.notificationPort), 5000); // 5-second timeout

                        try (PrintWriter userOut = new PrintWriter(notificationSocket.getOutputStream(), true)) {
                            userOut.println("EMERGENCY_RESOURCES_ACTIVATED_BY:" + fromUsernameId + ":Group" + entry.getKey());
                            group.writeMessageToLog(user.getUsername(), "EMERGENCY_RESOURCES_ACTIVATED_BY:" + fromUsernameId + ":Group" + entry.getKey());
                        } finally {
                            notificationSocket.close();
                        }

                    } catch (ConnectException e) {
                        System.err.println("Connection refused when trying to send emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    } catch (SocketTimeoutException e) {
                        System.err.println("Connection timeout for user " + user.getUsername());
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.err.println("IO Error sending emergency coms to " + user.getUsername());
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * Handles the "EMERGENCY_COMS" command issued by the user based on their profile.
     * - If the user's profile is NULL, the system doesn't create the command.
     * - If the user's profile is of ANY level, emergency coms are broadcast to all groups.
     *
     *
     * @param username The username of the user initiating the request.
     * @param out The PrintWriter object used to send responses back to the user.
     * @param requester The UserInfo object containing information about the requesting user.
     */
    private void handleEmergencyComs(String username, PrintWriter out, UserInfo requester) {

        // Emergency coms can only be activated in group 1 (low priority)
        if (requester.getProfile() == null) { // check to see if emergencycoms has a userprofile ( role )
            out.println("Emergency coms can only be activated in low priority group");
            System.out.println(requester.getProfile() + "this is the userprofile");
        }
        out.println("Emergency coms activated");
        // Broadcast emergency coms to all groups
        for (Map.Entry<Integer, UDPGroupChat> entry : context.getChatGroups().entrySet()) {  // for all 0-3 groups, I'm gonna
            UDPGroupChat group = entry.getValue(); // get the group ID
            for (UserInfo user : group.getLoggedUsers()) { //get all users online in that group

                if (user.getAddress() == null || user.getPort() <= 0) {
                    System.err.println("Invalid address or port for user: " + user.getUsername());
                    continue; // Skip this user
                }

                try {
                    Socket notificationSocket = new Socket(); // create a socket
                    notificationSocket.connect(new InetSocketAddress(user.getAddress(), user.notificationPort), 5000); // 5-second timeout

                    try (PrintWriter userOut = new PrintWriter(notificationSocket.getOutputStream(), true)) {
                        userOut.println("EMERGENCY_COMS_ACTIVATED_BY:" + username + ":Group" + entry.getKey());
                        group.writeMessageToLog(user.getUsername(), "EMERGENCY_COMS_ACTIVATED_BY:" + username + ":Group" + entry.getKey()); //escrevo para ficheiro que foi ativado.
                    } finally {
                        notificationSocket.close();
                    }

                } catch (ConnectException e) {
                    System.err.println("Connection refused when trying to send emergency coms to " + user.getUsername());
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    System.err.println("Connection timeout for user " + user.getUsername());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("IO Error sending emergency coms to " + user.getUsername());
                    e.printStackTrace();
                }
            }
        }

    }

    // Inner class to track pending requests
    private static class PendingRequest {
        String fromUsername;
        int fromGroupId;
        String toUsername;
        int toGroupId;

        public PendingRequest(String fromUsername, int fromGroupId, String toUsername, int toGroupId) {
            this.fromUsername = fromUsername;
            this.fromGroupId = fromGroupId;
            this.toUsername = toUsername;
            this.toGroupId = toGroupId;
        }
    }


}



