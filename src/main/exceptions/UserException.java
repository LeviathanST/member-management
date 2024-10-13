package exceptions;

public class UserException extends Exception{
    private String message;

    public UserException(){}
    
    public UserException(String message) {
        
        System.err.println("Exception with user name : " + message);
        printStackTrace();
    }
    public String getMessage() {return this.message;}

}
