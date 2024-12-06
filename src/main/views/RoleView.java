package views;


import java.sql.Connection;
import java.util.List;
import controllers.ApplicationController;
import dto.ResponseDTO;
import constants.ResponseStatus;
import models.roles.Role;


public class RoleView extends View{
    public RoleView(Connection con) {
        super(con);
    }

    public void getAllRoles() {
        clearScreen();
        viewTitle("| GET ALL ROLES |", textIO);
                List<Role> list = ApplicationController.getAllRoles().getData();
                textIO.getTextTerminal().println("There are " + list.size() + " roles.");
                for(Role i : list)
                    textIO.getTextTerminal().println(i.getId() + " : " + i.getName());
    }

    public void create(ResponseDTO<Object> response) {
        clearScreen();
        viewTitle("| CREATE ROLE |", textIO);
                String name = textIO.newStringInputReader().read("Enter role : ");
                response = ApplicationController.createRole(name);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void update(ResponseDTO<Object> response) {
        viewTitle("| UPDATE ROLE |", textIO);
                getAllRoles();
                int roleId = textIO.newIntInputReader().read("Enter role's id to update : ");
                String newName = textIO.newStringInputReader().read("Enter new name to role : ");
                response = ApplicationController.updateRole(roleId, newName);
                if(response.getStatus() != ResponseStatus.OK) {
                    printError(response.getMessage());
                } else textIO.getTextTerminal().println(response.getMessage());
    }

    public void delete(ResponseDTO<Object> response) {
        clearScreen();
        viewTitle("| DELETE ROLE |", textIO);
        getAllRoles();
        int roleId = textIO.newIntInputReader().read("Enter role's id to delete : ");
        response = ApplicationController.deleteRole(roleId);
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else textIO.getTextTerminal().println(response.getMessage());
    }

    
}
