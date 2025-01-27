package Authentication;

import Enums.UserProfiles;

public class User {
    private String name;
    private UserProfiles profile;
    private String password;
    private String usernameId;


    public User(String usernameId, String name, UserProfiles profile, String password) {
        this.name = name;
        this.profile = profile;
        this.password = password;
        this.usernameId = usernameId;
    }

    // Getters
    public String getName() { return name; }
    public UserProfiles getProfile() { return profile; }
    public String getPassword() { return password; }

    public String getUsernameId() {
        return usernameId;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', profile=" + profile + '}';
    }

}

