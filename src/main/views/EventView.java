package views;

import java.sql.Connection;
import java.util.List;

import constants.ResponseStatus;
import controllers.ApplicationController;
import dto.EventDto;
import dto.ResponseDTO;

public class EventView extends View{
    public EventView(Connection con) {
        super(con);
    }

    public List<EventDto> show() {
        clearScreen();
        ResponseDTO<List<EventDto>> response = ApplicationController.getAllEvent();
        if(response.getStatus() != ResponseStatus.OK) {
            printError(response.getMessage());
        } else {
            for(EventDto i : response.getData()) {
                textIO.getTextTerminal().println("ID: " + i.getId());
                textIO.getTextTerminal().println("Title: " + i.getTitle());
                textIO.getTextTerminal().println("Description: " + i.getDescription());
                textIO.getTextTerminal().println("Generatio : " + i.getGeneration());
                textIO.getTextTerminal().println("Start At: " + i.getStartAt());
                textIO.getTextTerminal().println("End At: " + i.getEndAt());
                textIO.getTextTerminal().println("Type: " + i.getType());
                textIO.getTextTerminal().println();
            }
        }
        return response.getData();
    }

    public void create() {
        clearScreen();
        String title = textIO.newStringInputReader().read("Enter title's event : ");
        String description = textIO.newStringInputReader().read("Enter description's event : ");
        String type = textIO.newStringInputReader().read("Enter type's event : ");
        int generation = textIO.newIntInputReader().read("Enter generation : ");
        String start = textIO.newStringInputReader().read("Enter date start (dd-MM-yyyy) : ");
        String end = textIO.newStringInputReader().read("Enter date end (dd-MM-yyyy) : ");
        EventDto event = new EventDto(generation, title, description, type);
        ResponseDTO<Object> response1 = ApplicationController.addEvent( event, start, end);
        if(response1.getStatus() != ResponseStatus.OK) {
            printError(response1.getMessage());
        } else textIO.getTextTerminal().println(response1.getMessage());
    }

    public void update() {
        clearScreen();
        List<EventDto> list = show();
        int id = textIO.newIntInputReader().read("Enter id to update : ");
        if(id <= 0 || id > list.size()) {
            printError("Invalid value.");
        } else {
            String title = textIO.newStringInputReader().read("Enter new title's event : ");
            String description = textIO.newStringInputReader().read("Enter new description's event : ");
            String type = textIO.newStringInputReader().read("Enter new type's event : ");
            int generation = textIO.newIntInputReader().read("Enter generation : ");
            String start = textIO.newStringInputReader().read("Enter new date start (dd-MM-yyyy) : ");
            String end = textIO.newStringInputReader().read("Enter new date end (dd-MM-yyyy) : ");
            EventDto event = new EventDto(generation, title, description, type);
            ResponseDTO<Object> response = ApplicationController.updateEvent(event, id, start, end);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            }
        }
    }

    public void delete() {
        clearScreen();
        List<EventDto> list = show();
        int id = textIO.newIntInputReader().read("Enter id to update : ");
        if(id <= 0 || id > list.size()) {
            printError("Invalid value.");
        } else {
            ResponseDTO<Object> response = ApplicationController.deleteEvent( id);
            if(response.getStatus() != ResponseStatus.OK) {
                printError(response.getMessage());
            }
        }
    }
}
