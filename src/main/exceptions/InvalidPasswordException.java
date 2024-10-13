package exceptions;

public class InvalidPasswordException extends Exception{
    private String message;

    public InvalidPasswordException(){}

    public InvalidPasswordException(String message) {
        this.message = message;
        System.err.println("Exception with password : " + message);
        printStackTrace();
    }
}
