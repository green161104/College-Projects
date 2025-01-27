package Authentication;

public class RegisterResult {
    private final boolean success;
    private final String message;

    private User newUser;

    public RegisterResult(boolean success, String message, User newUser) {
        this.success = success;
        this.message = message;
        this.newUser = newUser;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    public User getNewUser() {
        return newUser;
    }
}
