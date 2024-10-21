package views;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import services.AuthService;

import data.SignUpData;

public class AuthView {
    private TextIO textIO = TextIoFactory.getTextIO();
    private  TextTerminal<?> terminal = textIO.getTextTerminal();

    public int SignUp_LogIn() {
        terminal.getProperties().setPromptColor("green");
        terminal.getProperties().setInputColor("green");
        int choice = textIO.newIntInputReader().read("Welcome to member management\n" + 
                                                     "1 : Sign up\n" + 
                                                     "2 : Log in\n" + 
                                                     "Enter your choice : ");
        return choice;                                 
    }

    public void invalidValue() {
        terminal.getProperties().setPromptColor("red");
        textIO.getTextTerminal().println("Invalid value!");
    }
    
    public static void clearScreen(TextTerminal<?> terminal) {
        terminal.print("\033[H\033[2J");  // Mã escape ANSI để xóa màn hình
        terminal.print("\033[H");         // Đặt con trỏ trở lại vị trí đầu
    }
    

    public void signUpForm(SignUpData signUpData) {
        terminal.getProperties().setPromptColor("green");
        terminal.getProperties().setInputColor("green");
        signUpData.setUserName(textIO.newStringInputReader().read("Enter your user name : "));
        String pass;
        do {
            try {
                pass = textIO.newStringInputReader().read("Please enter your password (must contain at least 1 letter, 1 digit, no spaces, and be at least 8 characters long): ");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while(true);
        signUpData.setPassword(pass);
        textIO.getTextTerminal().println("Sign up successfully!");
    }
    
    
}
