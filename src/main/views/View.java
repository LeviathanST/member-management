package views;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import java.sql.Connection;

public class View {
    protected TextIO textIO = TextIoFactory.getTextIO();
    protected TextTerminal<?> terminal = textIO.getTextTerminal();
    protected Connection con;

    public View(Connection con) {
        this.con = con;
        textIO.getTextTerminal().getProperties().setInputColor("green");
        terminal.getProperties().setPromptColor("green");
    }

    public  void printError(String error) {
        terminal.getProperties().setPromptColor("red");
        textIO.getTextTerminal().println(error);
        terminal.getProperties().setPromptColor("green");
    }

    public void waitTime(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }
    public void viewTitle(String data, TextIO textIO) {
        for(int i = 0; i < data.length(); i++)
            textIO.getTextTerminal().print("-");
        textIO.getTextTerminal().println();
        textIO.getTextTerminal().println(data);
        for(int i = 0; i < data.length(); i++)
        textIO.getTextTerminal().print("-");
        textIO.getTextTerminal().println();
    }
    public void waitTimeByMessage(String message) {
        textIO.newStringInputReader().withMinLength(0).read(message);
    }

    public  void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public String AskContinueOrGoBack(){
        String continueOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("Continue", "Back", "Back To Menu")
                .read("");
        return continueOrBack;
    }
}
