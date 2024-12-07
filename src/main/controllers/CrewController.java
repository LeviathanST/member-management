package controllers;

import constants.ResponseStatus;
import dto.*;
import exceptions.*;
import models.Crew;
import models.CrewEvent;
import models.CrewPermission;
import models.CrewRole;
import models.UserCrewRole;
import repositories.CrewRepository;
import repositories.GenerationRepository;
import repositories.permissions.CrewPermissionRepository;
import repositories.roles.CrewRoleRepository;
import services.CrewService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CrewController {
    // TODO: CRUD Crew
    public static ResponseDTO<Object> addCrew(Crew crewDTO) {
        try {
            CrewService.create(crewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew %s to database successfully!", crewDTO.getName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteCrew(Crew crewDTO) {
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
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrew(Crew crewDTO, Crew newCrewDTO) {
        try {
            CrewService.update(crewDTO, newCrewDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", crewDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
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
            List<String> data = CrewRepository.getAllNameToList();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crews successfully!", data);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }

    // TODO: CRUD Guild Role
    public static ResponseDTO<Object> addCrewRole(CrewRole data) {
        try {
            CrewService.insertCrewRole(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew role %s successfully!", data.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrewRole(CrewRole crewRoleDTO, CrewRole newCrewRoleDTO) {
        try {
            CrewService.updateCrewRole(crewRoleDTO, newCrewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update crew role %s successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteCrewRole(CrewRole crewRoleDTO) {
        try {
            CrewService.deleteCrewRole(crewRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete role %s from crew successfully!", crewRoleDTO.getCrewName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<CrewRole>> getAllCrewRoles(String crew) {
        try {
            int crewId = CrewRepository.getIdByName(crew);
            List<CrewRole> data = CrewRoleRepository.getAllByCrewId(crewId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found Guild ID!", null);
        }
    }

    // TODO: CRUD User Guild Role
    public static ResponseDTO<Object> addUserCrewRole(UserCrewRole userCrewRoleDto) {
        try {
            CrewService.addUserToCrew(userCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add user %s crew role %s to crew %s successfully!",
                            userCrewRoleDto.getUsername(), userCrewRoleDto.getRole(), userCrewRoleDto.getCrew()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateUserCrewRole(UserCrewRole userCrewRoleDto,
            UserCrewRole newUserCrewRoleDto) {
        try {
            CrewService.updateUserToCrew(userCrewRoleDto, newUserCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update user %s role %s in crew %s successfully!",
                            userCrewRoleDto.getUsername(), userCrewRoleDto.getRole(), userCrewRoleDto.getCrew()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteUserCrewRole(UserCrewRole userCrewRoleDto) {
        try {
            CrewService.deleteUserInCrew(userCrewRoleDto);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete user %s role %s in crew %s successfully!",
                            userCrewRoleDto.getUsername(), userCrewRoleDto.getRole(), userCrewRoleDto.getCrew()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<UserCrewRole>> getAllUserCrewRolesByCrewID(String crew) {
        try {
            List<UserCrewRole> data = CrewService.getAllUserCrewRolesByCrewID(crew);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found Guild ID!", null);
        } catch (IndexOutOfBoundsException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException | NotHavePermission | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    // TODO: CRUD Crew Permission
    public static ResponseDTO<Object> addCrewPermission(String data) {
        try {
            CrewService.addCrewPermission(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew permission %s to database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteCrewPermission(String data) {
        try {
            CrewService.deleteGuildPermission(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete crew permission %s from database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrewPermission(String data, String newData) {
        try {
            CrewService.updateCrewPermission(data, newData);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update crew permission %s successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllCrewPermissions() {
        try {
            List<String> data = CrewPermissionRepository.getAllCrewPermission();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crews successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: CRUD Permission In Guild Role
    public static ResponseDTO<Object> addPermissionToCrewRole(CrewRole crewRole, String permission) {
        try {

            CrewService.addPermissionToCrewRole(crewRole, permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add permission %s to crew role %s successfully!", permission,
                            crewRole.getCrewName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deletePermissionInCrewRole(CrewRole crewRole, String permission) {
        try {
            CrewService.deletePermissionInCrewRole(crewRole, permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete permission %s in crew role %s successfully!", permission,
                            crewRole.getCrewName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updatePermissionInCrewRole(CrewRole crewRole, String permission,
            String newPermission) {
        try {
            CrewService.updatePermissionInCrewRole(crewRole, permission, newPermission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update permission %s in crew role %s successfully!", permission,
                            crewRole.getCrewName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllPermissionByCrewId(String crew, String role) {
        try {
            List<String> data = CrewService.getAllPermissionByCrewId(crew, role);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NullPointerException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<CrewPermission>> getAllPermissionByAccountId(String crew,
            String userName) {
        try {
            List<CrewPermission> data = CrewService.getAllPermissionByAccountId(crew, userName);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found crew permission!", null);
        } catch (DataEmptyException | InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException | NotHavePermission | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    // TODO: Crew Event
    public static ResponseDTO<Object> addCrewEvent(CrewEvent crewEventDto, String dateStart, String dateEnd) {
        try {
            CrewService.insertCrewEvent(crewEventDto, dateStart, dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add crew event %s successfully!", crewEventDto.getTitle()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteCrewEvent(int crewEventId, String crew) {
        try {
            CrewService.deleteCrewEvent(crewEventId, crew);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete crew event %s successfully!", crewEventId), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateCrewEvent(CrewEvent crewEventDto, int crewEventId, String dateStart,
            String dateEnd) {
        try {
            CrewService.updateCrewEvent(crewEventDto, crewEventId, dateStart, dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    "Update crew event successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission | IOException
                | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<CrewEvent>> getAllCrewEvent() {
        try {
            List<CrewEvent> data = CrewService.getAllEvent();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all crew event successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found crew event!", null);
        } catch (DataEmptyException | InvalidDataException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGeneration() {
        try {
            List<String> data = GenerationRepository.getAllGenerations();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all generation successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found generation!", null);
        }
    }
}
