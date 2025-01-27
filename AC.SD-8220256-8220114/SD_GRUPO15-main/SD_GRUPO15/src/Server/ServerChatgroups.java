package Server;

import Authentication.AuthSystem;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerChatgroups {

    private final Map<Integer, UDPGroupChat> chatGroups = new ConcurrentHashMap<>();
    private final AuthSystem authSystem = new AuthSystem();
    public ServerChatgroups(int maxGroups) throws IOException {
        initializeChatGroups(maxGroups);
    }

    private void initializeChatGroups(int maxGroups) throws IOException {
        for (int i = 0; i < maxGroups; i++) {
            InetAddress groupAddress = InetAddress.getByName("224.0.0." + (i + 1));
            UDPGroupChat groupChat = new UDPGroupChat(i, groupAddress);
            chatGroups.put(i, groupChat);
        }
    }

    public Map<Integer, UDPGroupChat> getChatGroups() {
        return chatGroups;
    }

    public AuthSystem getAuthSystem(){
        return authSystem;
    }
}
