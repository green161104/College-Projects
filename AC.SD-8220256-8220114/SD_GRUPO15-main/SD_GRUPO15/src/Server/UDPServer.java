package Server;

import Authentication.AuthSystem;
import Authentication.User;
import Authentication.UserInfo;
import Enums.UserProfiles;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UDPServer {



    /**
     * we'll be using UDP for sending messages through groupchats since there's no need to
     * guarantee a message is sent or not unless it is a request for an alert, or an alert.
     */

    private static final int UDP_PORT = 4446;
    private static final int MAX_GROUPS = 4; //0 1 2 3 , Para contar usar max +1
    private final ServerChatgroups context;
    private AuthSystem auth = new AuthSystem();

    /** 0 to 3 groupchats
     *  0 - all
     *  1 - low
     *  2 - medium
     *  3 - high
     */
    private Map<Integer, List<UserInfo>> chatGroupClients;
    
    /**
     * Guardo users num hashmap onde
     *  int - numero do grupo, User - user q logou
     */
    public UDPServer(ServerChatgroups context) throws IOException {
        this.context = context;
    }

/**
 * Starts the UDP server to handle real-time alerts from clients.
 *
 * This method initializes a {@link DatagramSocket} to listen on the specified
 * UDP port for incoming messages. It processes messages that are not commands,
 * ensuring efficient communication and proper resource management.
 *
 * @throws IOException if an I/O error occurs while creating or using the UDP socket.
 */

public void start() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(UDP_PORT)) {
            System.out.println("[UDP] Real-time Alert Server started on port " + UDP_PORT);

            byte[] buffer = new byte[3000];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); //espera aqui

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                String messageFromClient = new String(packet.getData(), 0, packet.getLength());
                System.out.println("I received " + messageFromClient);
                processClientMessage(socket, messageFromClient, clientAddress, clientPort);

                // Reset buffer
                buffer = new byte[3000];
            }
        }
    }

/**
 * Processes a message from a client and handles group chat communication.
 *
 * This method parses the client's message, validates its format, and adds the user
 * to the appropriate group chat if necessary. It also broadcasts the message to
 * all members of the group, excluding the sender.
 *
 * @param socket            the {@link DatagramSocket} used to send messages back to clients.
 * @param messageFromClient the raw message received from the client.
 * @param clientAddress     the {@link InetAddress} of the client sending the message.
 * @param clientPort        the port number of the client sending the message.
 *
 * @throws IOException if an error occurs while broadcasting messages.
 */
    private void processClientMessage(DatagramSocket socket, String messageFromClient, InetAddress clientAddress, int clientPort) throws IOException {

        String[] parts = messageFromClient.split(":", 3);
        if (parts.length < 3) {
            System.out.println("Invalid message format");
            return;
        }

        String username = parts[0];
        int groupId;
        String message;
        try {
            groupId = Integer.parseInt(parts[1]);
            message = parts[2];
        } catch (NumberFormatException e) {
            System.out.println("Invalid group ID");
            return;
        }

        if (groupId < 0 || groupId >= MAX_GROUPS) {
            System.out.println("Invalid group ID: " + groupId);
            return;
        }

        UDPGroupChat groupChat = context.getChatGroups().get(groupId);
        UserProfiles profile = auth.getUserProfile(username);
        UserInfo newClient = new UserInfo(username, profile, clientAddress, clientPort);

       // IF NEW CLIENT NOT IN GROUPCHAT ADD IT
        // Add user to group if not already present
        if (!groupChat.containsUser(newClient)) {
            groupChat.addUser(newClient);
            System.out.println("New client added to group " + groupId + ": " + username);
        }

        // Handle special messages
        if (message.equals("has joined the all chat!")) {
            System.out.println(username + " joined the all chat");
        } else if (message.equals("left the chat")) {
            System.out.println(username + " left the all chat");
            groupChat.removeUser(newClient);
        }

        // Broadcast message to group
        multicastMessage(socket, groupChat, username, message);
    }


    /**
     *  Handles broadcasting messages to all members of a group.
     *
     *  This method sends a given message to all users in the group, excluding the sender.
     *
     *   @param socket      the {@link DatagramSocket} used to send messages.
     *   @param groupChat   the {@link UDPGroupChat} representing the group to which the message is sent.
     *   @param sender      the username of the sender.
     *   @param message     the message to be broadcasted.
     *
     *   @throws IOException if an error occurs during packet transmission.
     * @param socket
     * @param groupChat
     * @param sender
     * @param message
     * @throws IOException
     */
    private void multicastMessage(DatagramSocket socket, UDPGroupChat groupChat, String sender, String message) throws IOException {


        List<UserInfo> groupUsers = groupChat.getLoggedUsers();

        String broadcastMessage = sender + ":" + message;
        byte[] sendData = broadcastMessage.getBytes();

        groupChat.writeMessageToLog(sender, message); // escrevo para aqui.

        for (UserInfo client : groupUsers) {
            // Don't send the message back to the sender
            DatagramPacket sendPacket = new DatagramPacket(
                    sendData,
                    sendData.length,
                    client.getAddress(),
                    client.getPort()
            );
            socket.send(sendPacket);
        }

        System.out.println("Multicasted to group " + groupChat.getGroupCode() + ": " + message);
    }



}

