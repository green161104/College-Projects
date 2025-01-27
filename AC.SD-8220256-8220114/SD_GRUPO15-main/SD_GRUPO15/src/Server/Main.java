package Server;

import java.io.IOException;

public class Main {

    private static final int MAX_GROUPS = 4;

    public static void main(String[] args) {
        try {

            ServerChatgroups context = new ServerChatgroups(MAX_GROUPS);

            // Start UDP Server
            new Thread(() -> {
                try {
                    UDPServer udpServer = new UDPServer(context);
                    udpServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Start TCP Server
            new Thread(() -> {
                try {
                    TCPServer tcpServer = new TCPServer(context);
                    tcpServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
