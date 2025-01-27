package Authentication;

import Enums.UserProfiles;

import java.net.InetAddress;
import java.util.Objects;

public class UserInfo{

    public InetAddress address;
    public int port;
    public int notificationPort; // New field to track notification port
    public String username;
    public UserProfiles profile;
    public UserInfo(String name, UserProfiles profile, InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.username = name;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(username, userInfo.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }


    public void setNotificationPort(int port) {
        this.notificationPort = port;
    }
    public int getNotificationPort() {
        return this.notificationPort;
    }


    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserProfiles getProfile() {
        return profile;
    }

    public void setProfile(UserProfiles profile) {
        this.profile = profile;
    }
}
