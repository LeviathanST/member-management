package views;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

public class GuildControllerView {
    public static void view(){
        TextIO textIO = TextIoFactory.getTextIO();
        String title = "Guild Controller";
        textIO.getTextTerminal().println(title);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("Add Member To Guild", "View Member In Guild", "Delete Member From Guild","Update Member From Guild")
                .read("");
        switch (options){
            case "Add Member To Guild":
                textIO.getTextTerminal().println("Add Member To Guild");
                break;
            case "View Member In Guild":
                textIO.getTextTerminal().println("View Member In Guild");
                break;
            case "Delete Member From Guild":
                textIO.getTextTerminal().println("Delete Member From Guild");
                break;
            case "Update Member From Guild":
                textIO.getTextTerminal().println("Update Member From Guild");
                break;
            default:
                textIO.getTextTerminal().println("Default");
        }
        options = textIO.newStringInputReader().read();
        textIO.dispose();
    }
    public static void viewAddMemberToGuild(){

    }
}
