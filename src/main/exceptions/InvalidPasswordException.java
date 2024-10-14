package exceptions;

public class InvalidPasswordException extends AuthException{
    private String message;
    public InvalidPasswordException(String mess) {
        super(mess);
        this.message = mess;
    }
    public String getMessage() {return this.message;}
}
