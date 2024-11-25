package controllers;

import constants.ResponseStatus;
import dto.*;
import exceptions.*;
import models.Crew;
import models.Generation;
import models.permissions.CrewPermission;
import models.roles.CrewRole;
import services.CrewService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CrewController {
    //TODO: CRUD Crew
    public static ResponseDTO<Object> addCrew(CrewDTO crewDTO) {
        try {
            CrewService.create(crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew %s to database successfully!", crewDTO.getName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }  catch (DataEmptyException | InvalidDataException | NotHavePermission e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteCrew(CrewDTO crewDTO)   {
        try {
            CrewService.delete(crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from database successfully!", crewDTO.getName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> updateCrew(CrewDTO crewDTO, CrewDTO newCrewDTO) {
        try {
            CrewService.update( crewDTO,newCrewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", crewDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }
    public static ResponseDTO<List<String>> getMemberCrew(String crew) {
        try {
            List<String> data = CrewService.getMemberInCrew(crew);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all member in crew successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (TokenException | NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<String>> getAllCrews() {
        try {
            List<String> data = Crew.getAllNameToList();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crews successfully!", data);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }
    //TODO: CRUD Guild Role
    public static ResponseDTO<Object> addCrewRole(CrewRoleDTO crewRoleDTO ) {
        try {
            CrewService.insertCrewRole( crewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew role %s successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateCrewRole(CrewRoleDTO crewRoleDTO, CrewRoleDTO newCrewRoleDTO ) {
        try {
            CrewService.updateCrewRole(crewRoleDTO, newCrewRoleDTO );
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update crew role %s successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteCrewRole(CrewRoleDTO crewRoleDTO ) {
        try {
            CrewService.deleteCrewRole(crewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete role %s from crew successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<CrewRole>> getAllCrewRoles(String crew) {
        try {
            int crewId = Crew.getIdByName(crew);
            List<CrewRole> data = CrewRole.getAllByCrewId(crewId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        }
    }
    // TODO: CRUD User Guild Role
    public static ResponseDTO<Object> addUserCrewRole( UserCrewRoleDto userCrewRoleDto ) {
        try {
            CrewService.addUserToCrew(userCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add user %s crew role %s to crew %s successfully!",
                            userCrewRoleDto.getUsername(),userCrewRoleDto.getRole(),userCrewRoleDto.getCrew()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> updateUserCrewRole( UserCrewRoleDto userCrewRoleDto, UserCrewRoleDto newUserCrewRoleDto ) {
        try {
            CrewService.updateUserToCrew( userCrewRoleDto, newUserCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update user %s role %s in crew %s successfully!",
                            userCrewRoleDto.getUsername(),userCrewRoleDto.getRole(),userCrewRoleDto.getCrew()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<Object> deleteUserCrewRole( UserCrewRoleDto userCrewRoleDto ) {
        try {
            CrewService.deleteUserInCrew( userCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete user %s role %s in crew %s successfully!",
                            userCrewRoleDto.getUsername(),userCrewRoleDto.getRole(),userCrewRoleDto.getCrew()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<UserCrewRoleDto>> getAllUserCrewRolesByCrewID(  String crew) {
        try {
            List<UserCrewRoleDto> data = CrewService.getAllUserCrewRolesByCrewID( crew);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found Guild ID!", null);
        } catch (IndexOutOfBoundsException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException | NotHavePermission | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    // TODO: CRUD Crew Permission
    public static ResponseDTO<Object> addCrewPermission( String data) {
        try {
            CrewService.addCrewPermission(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew permission %s to database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteCrewPermission(String data ) {
        try {
            CrewService.deleteGuildPermission(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete crew permission %s from database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrewPermission(String data, String newData ) {
        try {
            CrewService.updateCrewPermission(data, newData);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update crew permission %s successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllCrewPermissions() {
        try {
            List<String> data = CrewPermission.getAllCrewPermission();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crews successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    // TODO: CRUD Permission In Guild Role
    public static ResponseDTO<Object> addPermissionToCrewRole(CrewRoleDTO crewRole, String permission) {
        try {

            CrewService.addPermissionToCrewRole( crewRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add permission %s to crew role %s successfully!", permission,crewRole.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deletePermissionInCrewRole( CrewRoleDTO crewRole, String permission ) {
        try {
            CrewService.deletePermissionInCrewRole( crewRole,permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete permission %s in crew role %s successfully!", permission,crewRole.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updatePermissionInCrewRole( CrewRoleDTO crewRole, String permission, String newPermission ) {
        try {
            CrewService.updatePermissionInCrewRole(crewRole,permission,newPermission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update permission %s in crew role %s successfully!", permission,crewRole.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<String>> getAllPermissionByCrewId(  String crew, String role) {
        try {
            List<String> data = CrewService.getAllPermissionByCrewId(crew,role);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NullPointerException | IOException | ClassNotFoundException e ){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<CrewPermission>> getAllPermissionByAccountId( String crew, String userName) {
        try {
            List<CrewPermission> data = CrewService.getAllPermissionByAccountId(crew,userName);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found crew permission!", null);
        } catch (DataEmptyException | InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException | NotHavePermission | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    // TODO: Crew Event
    public static ResponseDTO<Object> addCrewEvent( CrewEventDto crewEventDto,String dateStart,String dateEnd) {
        try {
            CrewService.insertCrewEvent( crewEventDto,dateStart,dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew event %s successfully!", crewEventDto.getTitle()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage() , null);
        }
    }

    public static ResponseDTO<Object> deleteCrewEvent(  int crewEventId, String crew ) {
        try {
            CrewService.deleteCrewEvent( crewEventId, crew);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete crew event %s successfully!", crewEventId), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrewEvent( CrewEventDto crewEventDto, int crewEventId , String dateStart, String dateEnd) {
        try {
            CrewService.updateCrewEvent(crewEventDto,crewEventId,  dateStart,  dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    "Update crew event successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<CrewEventDto>> getAllCrewEvent() {
        try {
            List<CrewEventDto> data = CrewService.getAllEvent();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew event successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found crew event!", null);
        }catch (DataEmptyException | InvalidDataException | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
    public static ResponseDTO<List<String>> getAllGeneration(){
        try {
            List<String> data = Generation.getAllGenerations();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all generation successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e){
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND,"Not found generation!", null);
        }
    }
}
