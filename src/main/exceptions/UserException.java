package exceptions;

public class UserException extends Exception{
    private String message;

    public UserException(){}

    public UserException(String message) {
        this.message = message;
        System.err.println("Exception with user name : " + message);
        printStackTrace();
    }

}
