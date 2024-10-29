package views;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import java.lang.Thread;
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
        textIO.getTextTerminal().println("| " + data + " |");
    }

    public String AskContinueOrGoBack(){
        String continueOrBack = textIO.newStringInputReader()
                .withNumberedPossibleValues("Continue", "Back", "Back To Menu")
                .read("");
        return continueOrBack;
    }
}
