package views;

import controllers.CrewController;
import controllers.GuildController;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrewControllerView {
    public static void view(Connection connection) throws SQLException {
        TextIO textIO = TextIoFactory.getTextIO();
        String title = "Crew Controller";
        textIO.getTextTerminal().println(title);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("Add Member To Crew", "View Member In Crew", "Delete Member From Crew","Update Member From Crew","Back","Back To Menu")
                .read("");
        switch (options){
            case "Add Member To Crew":
                viewCRUDMemberToCrew(connection,options);
                break;
            case "View Member In Crew":
                textIO.getTextTerminal().println("View Member In Guild");
                break;
            case "Delete Member From Crew":
                viewCRUDMemberToCrew(connection,options);
                break;
            case "Update Member From Crew":
                viewCRUDMemberToCrew(connection,options);
                break;
            default:
                textIO.getTextTerminal().println("Default");
        }
        options = textIO.newStringInputReader().read();
        textIO.dispose();
    }
    public static void viewCRUDMemberToCrew(Connection connection, String options) throws SQLException {
        while (true){
            List<String> crewNames = new ArrayList<>();
            crewNames = CrewController.getAllCrews(connection);
            TextIO textIO = TextIoFactory.getTextIO();
            String border = "+---------------------------------------+";
            textIO.getTextTerminal().println(border);
            textIO.getTextTerminal().println("| " + options + "          |");
            textIO.getTextTerminal().println(border);

            textIO.getTextTerminal().println(border);
            textIO.getTextTerminal().println("| Input Member's Username  |");
            textIO.getTextTerminal().println(border);

            String name = textIO.newStringInputReader()
                    .withDefaultValue(null)
                    .read("");

            textIO.getTextTerminal().println(border);
            textIO.getTextTerminal().println("| Input Crew's Name               |");
            textIO.getTextTerminal().println(border);

            String guildName = textIO.newStringInputReader()
                    .withNumberedPossibleValues(crewNames)
                    .read("");

            textIO.getTextTerminal().println(border);
            textIO.getTextTerminal().println("| Input Crew's Role               |");
            textIO.getTextTerminal().println(border);

            String guildRole = textIO.newStringInputReader()
                    .withNumberedPossibleValues(crewNames)
                    .read("");

            switch (options){
                case "Add Member To Crew":
                    break;
                case "Delete Member From Crew":
                    break;
                case "Update Member From Crew":
                    break;
            }
            textIO.getTextTerminal().println(border);
            String continueOrBack = textIO.newStringInputReader()
                    .withNumberedPossibleValues("Continue", "Back", "Back To Menu")
                    .read("");
            textIO.getTextTerminal().println(border);
            if (!continueOrBack.equals("Continue")){
                break;
            }
        }

    }


}
