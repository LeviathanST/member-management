package controllers;

import dto.*;
import dto.role.DeletePermissionDTO;
import exceptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Guild;
import models.GuildEvent;
import models.GuildPermission;
import models.GuildRole;
import models.UserGuildRole;
import models.UserProfile;
import repositories.GenerationRepository;
import repositories.GuildRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import constants.ResponseStatus;
import constants.RoleContext;
import repositories.permissions.GuildPermissionRepository;
import repositories.roles.GuildRoleRepository;
import repositories.roles.RoleRepository;
import services.AuthService;
import services.GuildService;
import utils.HttpUtil;

@WebServlet("/guild/*")
public class GuildController extends HttpServlet {
    private final String NOTIFYERROR_VIEW = "/view/notifyError.jsp";
    private final String ROLE_VIEW = "/view/guild/role.jsp";

    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private String redirectView;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        try {
            switch (route) {
                case "roles":
                    Cookie[] cookies = req.getCookies();
                    String name = req.getParameter("name");
                    String accountId = AuthService.handleCookieAndGetAccountId(cookies);
                    boolean checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        List<String> roles = RoleRepository.getAllByPrefix(req.getParameter("name"));
                        req.setAttribute("roles", roles);

                        List<String> permissions = RoleRepository
                                .getAllPermissionByContext(RoleContext.GUILD);
                        req.setAttribute("permissions", permissions);
                        redirectView = ROLE_VIEW;
                    } else {
                        req.setAttribute("response", gson.toJson(
                                new ResponseDTO<UserProfile>(ResponseStatus.UNAUTHORIZED,
                                        "FORBIDDEN",
                                        null)));
                        redirectView = NOTIFYERROR_VIEW;
                    }
                    break;
                default:
                    req.setAttribute("response", gson.toJson(
                            new ResponseDTO<UserProfile>(ResponseStatus.NOT_FOUND,
                                    "NOT FOUND!",
                                    null)));
                    redirectView = NOTIFYERROR_VIEW;
                    break;
            }
        } catch (SQLException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<UserProfile>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                            null)));
            redirectView = NOTIFYERROR_VIEW;
        } catch (AuthException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<UserProfile>(ResponseStatus.UNAUTHORIZED, e.getMessage(),
                            null)));
            redirectView = NOTIFYERROR_VIEW;
        } finally {
            req.getRequestDispatcher(redirectView).forward(req, res);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        try {
            switch (route) {
                case "roles":
                    Cookie[] cookies = req.getCookies();
                    String name = req.getParameter("name");
                    String accountId = AuthService.handleCookieAndGetAccountId(cookies);
                    boolean checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        DeletePermissionDTO dto = HttpUtil.getBodyContentFromReq(req, DeletePermissionDTO.class);
                        RoleRepository.deletePermission(dto.getRoleName(), dto.getPermissions());
                    } else {
                        res.getWriter().write(gson.toJson(
                                new ResponseDTO<UserProfile>(ResponseStatus.UNAUTHORIZED,
                                        "FORBIDDEN",
                                        null)));
                    }
                    break;
                default:
                    res.getWriter().write(gson.toJson(
                            new ResponseDTO<UserProfile>(ResponseStatus.NOT_FOUND,
                                    "NOT FOUND!",
                                    null)));
                    break;
            }
        } catch (AuthException e) {
            res.getWriter().write(gson.toJson(
                    new ResponseDTO<UserProfile>(ResponseStatus.UNAUTHORIZED, e.getMessage(),
                            null)));
        } catch (SQLException | ClassNotFoundException e) {
            logger.info(e.getStackTrace().toString());
            res.getWriter().write(gson.toJson(
                    new ResponseDTO<UserProfile>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                            null)));
        }
    }

    // TODO: CRUD Guild
    public static ResponseDTO<Object> addGuild(Guild guildDTO) {
        try {
            GuildService.create(guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild %s to database successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteGuild(Guild guildDTO) {
        try {
            GuildService.delete(guildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete %s from database successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuild(Guild guildDTO, Guild newGuildDTO) {
        try {
            GuildService.update(guildDTO, newGuildDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update %s successfully!", guildDTO.getName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGuilds() {
        try {
            List<String> data = GuildRepository.getAllNameToList();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        }
    }

    public static ResponseDTO<List<String>> getMemberInGuild(String guild) {
        try {
            List<String> data = GuildService.getMemberInGuild(guild);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all member in guild successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (DataEmptyException e) {
            throw new RuntimeException(e);
        } catch (TokenException | NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<UserProfile> getUserProfile(String username) {
        try {
            UserProfile data = GuildService.getUserProfile(username);
            return new ResponseDTO<>(ResponseStatus.OK, "Get user profile successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (DataEmptyException e) {
            throw new RuntimeException(e);
        } catch (TokenException | NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    // TODO: CRUD Guild Role
    public static ResponseDTO<Object> addGuildRole(GuildRole guildRoleDTO) {
        try {
            GuildService.insertGuildRole(guildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild role %s successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuildRole(GuildRole data, GuildRole newData) {
        try {
            GuildService.updateGuildRole(data, newData);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update guild role %s successfully!", data.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteGuildRole(GuildRole guildRoleDTO) {
        try {
            GuildService.deleteGuildRole(guildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete role %s from guild successfully!", guildRoleDTO.getGuildName()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, cannot delete this role because related data still exists", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<GuildRole>> getAllGuildRoles(String guild) {
        try {
            int guildId = GuildRepository.getIdByName(guild);
            List<GuildRole> data = GuildRoleRepository.getAllByGuildId(guildId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found Guild ID!", null);
        }
    }

    // TODO: CRUD User Guild Role
    public static ResponseDTO<Object> addUserGuildRole(UserGuildRole data) {
        try {
            GuildService.addUserToGuild(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add user %s guild role %s to guild %s successfully!",
                            data.getUsername(), data.getRole(), data.getGuild()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateUserGuildRole(UserGuildRole userGuildRoleDTO,
            UserGuildRole newUserGuildRoleDTO) {
        try {
            GuildService.updateUserInGuild(userGuildRoleDTO, newUserGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update user %s role %s in guild %s successfully!",
                            userGuildRoleDTO.getUsername(), userGuildRoleDTO.getRole(), userGuildRoleDTO.getGuild()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteUserGuildRole(UserGuildRole userGuildRoleDTO) {
        try {
            GuildService.deleteUserInGuild(userGuildRoleDTO);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete user %s role %s in guild %s successfully!",
                            userGuildRoleDTO.getUsername(), userGuildRoleDTO.getRole(), userGuildRoleDTO.getGuild()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<UserGuildRole>> getAllUserGuildRolesByGuildID(String guild) {
        try {

            List<UserGuildRole> data = GuildService.getAllUserGuildRolesByGuildID(guild);

            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild roles successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found Guild ID!", null);
        } catch (NullPointerException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found data!", null);
        } catch (TokenException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    // TODO: CRUD Guild Permission
    public static ResponseDTO<Object> addGuildPermission(String data) {
        try {
            GuildService.addGuildPermission(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild permission %s to database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteGuildPermission(String data) {
        try {
            GuildService.deleteGuildPermission(data);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete guild permission %s from database successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuildPermission(String data, String newData) {
        try {
            GuildService.updateGuildPermission(data, newData);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update guild permission %s successfully!", data), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllGuildPermissions() {
        try {
            List<String> data = GuildPermissionRepository.getAllGuildPermission();
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guilds successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: CRUD Permission In Guild Role
    public static ResponseDTO<Object> addPermissionToGuildRole(GuildRole guildRole, String permission) {
        try {
            GuildService.addPermissionToGuildRole(guildRole, permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add permission %s to guild role %s successfully!", permission,
                            guildRole.getGuildName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deletePermissionInGuildRole(GuildRole guildRole, String permission) {
        try {
            GuildService.deletePermissionInGuildRole(guildRole, permission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete permission %s in guild role %s successfully!", permission,
                            guildRole.getGuildName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updatePermissionInGuildRole(GuildRole guildRole, String permission,
            String newPermission) {
        try {
            GuildService.updatePermissionInGuildRole(guildRole, permission, newPermission);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Update permission %s in guild role %s successfully!", permission,
                            guildRole.getGuildName()),
                    null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<String>> getAllPermissionByGuildId(String guild, String role) {
        try {
            int guildId = GuildRepository.getIdByName(guild);
            int guildRoleId = GuildRoleRepository.getIdByName(guildId, role);
            List<String> listData = GuildPermissionRepository.getAllByGuildRoleId(guildRoleId);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild permission successfully!", listData);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found guild permission!", null);
        }
    }

    public static ResponseDTO<List<GuildPermission>> getAllPermissionByAccountId(String guild,
            String userName) {
        try {
            List<GuildPermission> data = GuildService.getAllPermissionByAccountId(guild, userName);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild permission successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found guild permission!", null);
        } catch (DataEmptyException | InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    // TODO: Guild Event
    public static ResponseDTO<Object> addGuildEvent(GuildEvent guildEventDto, String dateStart, String dateEnd) {
        try {
            GuildService.insertGuildEvent(guildEventDto, dateStart, dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Add guild event %s successfully!", guildEventDto.getTitle()), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException | IOException | ClassNotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> deleteGuildEvent(int guildEventId, String guild) {
        try {
            GuildService.deleteGuildEvent(guildEventId, guild);
            return new ResponseDTO<>(ResponseStatus.OK,
                    String.format("Delete guild event %s successfully!", guildEventId), null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> updateGuildEvent(GuildEvent guildEventDto, int guildEventId, String dateStart,
            String dateEnd) {
        try {
            GuildService.updateGuildEvent(guildEventDto, guildEventId, dateStart, dateEnd);
            return new ResponseDTO<>(ResponseStatus.OK,
                    "Update guild event successfully!", null);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!" + e, null);
        } catch (NotFoundException | TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
        } catch (DataEmptyException | InvalidDataException | NotHavePermission e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<List<GuildEvent>> getAllGuildEvent() {
        try {
            List<GuildEvent> data = GuildService.getAllEvent();

            return new ResponseDTO<>(ResponseStatus.OK, "Get all guild event successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found guild event!", null);
        } catch (DataEmptyException | InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
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

    public static ResponseDTO<List<UserProfile>> findByUsername(String username) {
        try {
            List<UserProfile> data = GuildService.findByUsername(username);
            return new ResponseDTO<>(ResponseStatus.OK, "Get all user successfully!", data);
        } catch (SQLException e) {
            return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
                    "Error occurs when querying, please try again!", null);
        } catch (NotFoundException | DataEmptyException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "Not found username!", null);
        } catch (TokenException e) {
            return new ResponseDTO<>(ResponseStatus.NOT_FOUND, "You don't have permission", null);
        } catch (InvalidDataException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, "Not found username!", null);
        }
    }

}
