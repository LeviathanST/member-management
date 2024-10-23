package views;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import data.LoginData;
import data.SignUpData;


public class AuthView {
    protected TextIO textIO = TextIoFactory.getTextIO();
    protected  TextTerminal<?> terminal = textIO.getTextTerminal();

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
    
    public void clearScreen() {
        this.terminal.print("\033[H\033[2J");  // Mã escape ANSI để xóa màn hình
        this.terminal.print("\033[H");         // Đặt con trỏ trở lại vị trí đầu
    }

    public void clearScreen(String mess) {
        String tmp ="1";
        tmp += textIO.newStringInputReader().read(mess);
        this.terminal.print("\033[H\033[2J");  
        this.terminal.print("\033[H"); 
    }
    

    public void signUpForm(SignUpData signUpData) {
        terminal.getProperties().setPromptColor("green");
        terminal.getProperties().setInputColor("green");
        signUpData.setUserName(textIO.newStringInputReader().read("Enter your user name : "));
        signUpData.setPassword(textIO.newStringInputReader().read("Enter your password" + 
                                                                         "(must contains at least one character," +  
                                                                         "one digit, one speacial," +  
                                                                         "one upper case and must not contain space) : "));
    }

    public void LogInForm(LoginData loginData) {
        terminal.getProperties().setPromptColor("green");
        terminal.getProperties().setInputColor("green");
        loginData.setUsername(textIO.newStringInputReader().read("Enter your user name : "));
        loginData.setPassword(textIO.newStringInputReader().read("Enter your password : "));
    }
    
    
}
