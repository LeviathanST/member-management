package exceptions;

public class UserProfileException extends Exception{
    private String message;
    public UserProfileException(String message) {
        super(message);
    }
}
