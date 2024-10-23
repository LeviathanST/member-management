package views;

public class ShowMenu extends AuthView{
    public ShowMenu(){}

    public int showMenu() {
        terminal.getProperties().setPromptColor("green");
        terminal.getProperties().setInputColor("green");
        int choice = textIO.newIntInputReader().read("Welcome to member management\n" + 
                                                "1 : User profile" + 
                                                "-----------------\n" + 
                                                "Enter your choice : ");    
        return choice;                 
    }
}
