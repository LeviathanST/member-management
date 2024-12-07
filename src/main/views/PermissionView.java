package views;

import java.sql.Connection;
import java.util.List;

import controllers.ApplicationController;
import dto.ResponseDTO;
import models.Permission;
import constants.ResponseStatus;

public class PermissionView extends View {
    public PermissionView(Connection con) {
        super(con);
    }

    public void getAllPermission() {
        clearScreen();
        viewTitle("| GET ALL PERMISSIONS |", textIO);
        List<Permission> list = ApplicationController.getAllPermissions().getData();
        textIO.getTextTerminal().println("There are " + list.size() + " permissions.");
        for (Permission i : list)
            textIO.getTextTerminal().println(i.getId() + " : " + i.getName());
    }

    public void create(ResponseDTO<Object> response) {
        clearScreen();
        viewTitle("| CREATE PERMISSION |", textIO);
        String permission = textIO.newStringInputReader().read("Enter permission's name : ");
        response = ApplicationController.createPermission(permission);
        if (response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else
            textIO.getTextTerminal().println(response.getMessage());
    }

    public void update(ResponseDTO<Object> response) {
        viewTitle("| UPDATE PERMISSION |", textIO);
        getAllPermission();
        int permissionId = textIO.newIntInputReader().read("Enter permission's id : ");
        String newPermission = textIO.newStringInputReader().read("Enter new permission name : ");
        response = ApplicationController.updatePermission(permissionId, newPermission);
        if (response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else
            textIO.getTextTerminal().println(response.getMessage());
    }

    public void delete(ResponseDTO<Object> response) {
        viewTitle("| DELETE PERMISSION |", textIO);
        getAllPermission();
        int permissionId = textIO.newIntInputReader().read("Enter permission's id to delete : ");
        response = ApplicationController.deletePermission(permissionId);
        if (response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else
            textIO.getTextTerminal().println(response.getMessage());
    }
}
