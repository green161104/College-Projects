package Server;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
public class CommunicationProtocol {

    /*
    Message parsing: It parses incoming messages from clients and splits them into commands and data.
    Handling commands: The protocol defines how to process commands such as user registration and request sending.
    Formatting responses: It formats responses to be sent back to clients.



     */

    public static final String REGISTER_COMMAND = "REGISTER";
    public static final String SEND_REQUEST_COMMAND = "SEND_REQUEST";

    // Parses the message received from the client and returns a command
    public static String[] parseMessage(String message) {
        return message.split(":", 2); // Assumes "COMMAND: content"
    }

    // Format a response message to the client
    public static String formatResponse(String response) {
        return "RESPONSE: " + response;
    }

    // Handle user registration command
    public static void handleRegistration(String message, Socket socket) {
        // Parse and register the user (implement in server)
        String[] parts = parseMessage(message);
        String username = parts[1].split(",")[0];  // Extract username
        String role = parts[1].split(",")[1];      // Extract role
        //TCPServer.Server.registerUser(username, role);
    }

    // Handle send request command
    public static void handleSendRequest(String message, Socket socket) {
        // Parse and process the send request (implement in server)
        String[] parts = parseMessage(message);
        String fromUser = parts[1].split(",")[0];  // Extract fromUser
        String toUser = parts[1].split(",")[1];    // Extract toUser
        String requestMessage = parts[1].split(",")[2]; // Extract message content
        //TCPServer.requests.processRequest(fromUser, toUser, requestMessage);
    }

    // Handles UDP messages (you can add more commands here)
    public static void handleUDPMessage(String message, DatagramSocket udpSocket, DatagramPacket receivePacket) {
        // Handle and process UDP messages here
    }

}
