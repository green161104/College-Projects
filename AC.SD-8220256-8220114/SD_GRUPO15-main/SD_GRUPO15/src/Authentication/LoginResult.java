package Authentication;

public class LoginResult {
    private final boolean success;
    private final User user;

    public LoginResult(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() { return success; }
    public User getUser() { return user; }
}