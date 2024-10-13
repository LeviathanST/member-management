package exceptions;

public class PasswordException extends Exception{
    private String message;

    public PasswordException(){}

    public PasswordException(String message) {
        this.message = message;
        System.err.println("Exception with password : " + message);
        printStackTrace();
    }
}
