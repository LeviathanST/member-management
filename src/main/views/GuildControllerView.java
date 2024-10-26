package views;
import dto.GuildDTO;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import controllers.GuildController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuildControllerView {
    public static void view(Connection connection) throws SQLException {
        TextIO textIO = TextIoFactory.getTextIO();
        String title = "Guild Controller";
        textIO.getTextTerminal().println(title);
        String options = textIO.newStringInputReader()
                .withNumberedPossibleValues("Add Member To Guild", "View Member In Guild", "Delete Member From Guild","Update Member From Guild","Back","Back To Menu")
                .read("");
        switch (options){
            case "Add Member To Guild":
                viewCRUDMemberToGuild(connection,options);
                break;
            case "View Member In Guild":
                textIO.getTextTerminal().println("View Member In Guild");
                break;
            case "Delete Member From Guild":
                viewCRUDMemberToGuild(connection,options);
                break;
            case "Update Member From Guild":
                viewCRUDMemberToGuild(connection,options);
                break;
            default:
                textIO.getTextTerminal().println("Default");
        }
        options = textIO.newStringInputReader().read();
        textIO.dispose();
    }
    public static void viewCRUDMemberToGuild(Connection connection, String options) throws SQLException {
        while (true){
            List<String> guildNames = new ArrayList<>();
            guildNames = (List<String>) GuildController.getAllGuilds(connection);
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
            textIO.getTextTerminal().println("| Input Guild's Name               |");
            textIO.getTextTerminal().println(border);

            String guildName = textIO.newStringInputReader()
                    .withNumberedPossibleValues(guildNames)
                    .read("");

            textIO.getTextTerminal().println(border);
            textIO.getTextTerminal().println("| Input Guild's Role               |");
            textIO.getTextTerminal().println(border);

            String guildRole = textIO.newStringInputReader()
                    .withNumberedPossibleValues(guildNames)
                    .read("");
            GuildDTO guildDTO = new GuildDTO(name, guildName, guildRole);
            switch (options){
                case "Add Member To Guild":
                    GuildController.add(connection, guildDTO);
                    break;
                case "Delete Member From Guild":
                    GuildController.delete(connection, guildDTO);
                    break;
                case "Update Member From Guild":
                    GuildController.update(connection, guildDTO);
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
