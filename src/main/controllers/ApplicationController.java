package controllers;

import utils.HttpUtil;
import utils.Pressessor;
import dto.UpdateProfileDTO;

import com.google.gson.Gson;

import constants.ResponseStatus;
import constants.RoleContext;
import dto.ResponseDTO;
import dto.app.*;
import dto.crew.CreateCrewDTO;
import dto.guild.CreateGuildDTO;
import dto.role.CDPermissionDTO;
import dto.role.UpdateRoleDTO;
import dto.role.CDUserRoleDTO;
import dto.role.GetUserDTO;
import dto.role.UpdateUserRoleDTO;
import exceptions.*;
import models.Guild;
import models.UserProfile;
import repositories.users.UserAccountRepository;
import repositories.users.UserProfileRepository;
import repositories.events.EventRepository;
import repositories.RoleRepository;
import services.ApplicationService;
import services.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/app/*")
public class ApplicationController extends HttpServlet {
    private final String NOTIFYERROR_VIEW = "/view/notifyError.jsp";
    private final String NOTFOUND_VIEW = "/view/notfound.jsp";

    private final String PROFILE_VIEW = "/view/user/profile.jsp";
    private final String ROLE_VIEW = "/view/app/role.jsp";
    private final String MEMBER_VIEW = "/view/app/member.jsp";
    private final String EVENT_VIEW = "/view/app/event.jsp";
    private final String GUILD_VIEW = "/view/app/guild.jsp";
    private final String CREW_VIEW = "/view/app/crew.jsp";

    private final String prefix = "APP";

    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private String redirectView;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        Cookie[] cookies = req.getCookies();
        PrintWriter out = res.getWriter();
        boolean checked;
        try {
            String accountId = AuthService.handleCookieAndGetAccountId(cookies);
            switch (route) {
                case "profile":
                    String username = req.getParameter("name");
                    UserProfile userProfile;
                    if (username == null) {
                        userProfile = UserProfileRepository.read(accountId);
                        req.setAttribute("profile", userProfile);
                    } else {
                        userProfile = ApplicationService.readSpecifiedProfile(
                                username, accountId,
                                AuthService.checkPermissionWithContext(
                                        accountId,
                                        RoleContext.APP,
                                        "user.profile.view_other"));
                        req.setAttribute("profile", userProfile);
                    }
                    redirectView = PROFILE_VIEW;
                    break;
                case "events":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "view");
                    if (checked) {
                        List<GetEventDTO> events = EventRepository.getAllEvent();
                        req.setAttribute("events", events);
                        req.setAttribute("ade",
                                AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                                        "event.cud"));
                        redirectView = EVENT_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "members":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "view");
                    if (checked) {
                        List<String> roles = RoleRepository.getAllByPrefix(prefix);
                        req.setAttribute("roles", gson.toJson(roles));
                        req.setAttribute("ade",
                                AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                                        "member.cud"));

                        List<GetUserDTO> members = RoleRepository
                                .getUsersByPrefix("APP");
                        req.setAttribute("members", members);
                        redirectView = MEMBER_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "roles":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "role.crud");
                    if (checked) {
                        List<String> roles = RoleRepository.getAllByPrefix(prefix);
                        req.setAttribute("roles", roles);

                        List<String> permissions = RoleRepository
                                .getAllPermissionByContext(RoleContext.APP);
                        req.setAttribute("permissions", gson.toJson(permissions));
                        redirectView = ROLE_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "guilds":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "view");
                    if (checked) {
                        Boolean ade = AuthService.checkPermissionWithContext(accountId, RoleContext.APP, "guild.cud");
                        List<String> guilds = ApplicationService.getGuilds(ade, accountId);
                        req.setAttribute("guilds", guilds);
                        req.setAttribute("ade", ade);

                        redirectView = GUILD_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "crews":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "view");
                    Logger logger = LoggerFactory.getLogger(ApplicationController.class);
                    logger.debug("Bool 2: " + checked);
                    if (checked) {
                        Boolean ade = AuthService.checkPermissionWithContext(accountId, RoleContext.APP, "crew.cud");
                        List<String> crews = ApplicationService.getCrews(ade, accountId);
                        logger.debug("Bool 3: " + ade);
                        req.setAttribute("crews", crews);
                        req.setAttribute("ade", ade);

                        redirectView = CREW_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "permissions":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "role.crud");
                    if (checked) {
                        String roleName = req.getParameter("role");
                        if (roleName != null && !roleName.isBlank()) {
                            List<String> permissionsOfRole = RoleRepository.getAllPermissionOfARole(roleName);
                            res.setContentType("application/json");
                            out.write(gson.toJson(
                                    new ResponseDTO<List<String>>(ResponseStatus.OK,
                                            "Get all permissions of " + roleName,
                                            permissionsOfRole)));
                            redirectView = null;
                            break;
                        } else {
                            throw new IllegalArgumentException("Role is null or empty!");
                        }
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                default:
                    redirectView = NOTFOUND_VIEW;
                    break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                            null)));
            redirectView = NOTIFYERROR_VIEW;
        } catch (NotFoundException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(),
                            null)));
            redirectView = NOTIFYERROR_VIEW;
        } catch (AuthException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<>(ResponseStatus.UNAUTHORIZED, e.getMessage(),
                            null)));
            redirectView = NOTIFYERROR_VIEW;
        } finally {
            if (redirectView != null) {
                req.getRequestDispatcher(redirectView).forward(req, res);
            } else {
                out.flush();
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        Cookie[] cookies = req.getCookies();
        String name = req.getParameter("name");
        PrintWriter out = res.getWriter();
        boolean checked;
        try {
            String accountId = AuthService.handleCookieAndGetAccountId(cookies);
            switch (route) {
                case "events":
                    checked = AuthService.checkPermissionWithContext(accountId,
                            RoleContext.APP,
                            "event.cud");
                    if (checked) {
                        CUEventDTO dto = HttpUtil.getBodyContentFromReq(req, CUEventDTO.class);
                        ApplicationService.insertEvent(dto);
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Create event for %s successful!".formatted(name),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "members":
                    checked = AuthService.checkPermissionWithContext(accountId,
                            RoleContext.APP,
                            "member.cud");
                    if (checked) {
                        CDUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDUserRoleDTO.class);
                        RoleRepository.addDefaultForUserByPrefix(dto.getUsername(), prefix);
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Add user %s to %s guild successful!".formatted(dto.getUsername(), name),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "guilds":
                    checked = AuthService.checkPermissionWithContext(accountId,
                            RoleContext.APP,
                            "guild.cud");
                    if (checked) {
                        CreateGuildDTO dto = HttpUtil.getBodyContentFromReq(req, CreateGuildDTO.class);
                        dto.checkNullOrEmpty();
                        ApplicationService.createGuild(dto.getGuildName(), dto.getGuildCode(), dto.getUsername());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Created guild successful!".formatted(dto.getUsername(), name),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "crews":
                    checked = AuthService.checkPermissionWithContext(accountId,
                            RoleContext.APP,
                            "crew.cud");
                    Logger logger = LoggerFactory.getLogger(ApplicationController.class);
                    logger.debug("Bool 1: " + checked);
                    if (checked) {
                        CreateCrewDTO dto = HttpUtil.getBodyContentFromReq(req, CreateCrewDTO.class);
                        dto.checkNullOrEmpty();
                        ApplicationService.createCrew(dto.getCrewName(), dto.getCrewCode(), dto.getUsername());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Created crew successful!".formatted(dto.getUsername(), name),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "permissions":
                    checked = AuthService.checkPermissionWithContext(accountId,
                            RoleContext.APP,
                            "role.crud");
                    if (checked) {
                        CDPermissionDTO dto = HttpUtil.getBodyContentFromReq(req, CDPermissionDTO.class);
                        RoleRepository.insertPermissionRole(
                                "APP_" + dto.getRoleName(),
                                dto.getPermissions());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Add permission to %s successfully!".formatted(dto.getRoleName()),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                default:
                    throw new NotFoundException("Not found the page!");
            }
        } catch (AuthException e) {
            out.write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.UNAUTHORIZED, e.getMessage(),
                            null)));
        } catch (SQLException | ClassNotFoundException | IOException e) {
            logger.info(e.getStackTrace().toString());
            out.write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                            null)));
        } catch (NotFoundException | IllegalArgumentException e) {
            out.write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(),
                            null)));
        } finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        Cookie[] cookies = req.getCookies();
        PrintWriter out = res.getWriter();
        boolean checked;
        try {
            String accountId = AuthService.handleCookieAndGetAccountId(cookies);
            switch (route) {
                case "profile":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "user.profile.update");
                    if (checked) {
                        UpdateProfileDTO data = HttpUtil.getBodyContentFromReq(req,
                                UpdateProfileDTO.class);
                        ApplicationService.updateUserProfile(data, accountId);
                        res.getWriter().write(gson.toJson(
                                new ResponseDTO<UserProfile>(ResponseStatus.OK,
                                        "Your profile is updated!",
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "events":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "event.cud");
                    if (checked) {
                        CUEventDTO dto = HttpUtil.getBodyContentFromReq(req, CUEventDTO.class);
                        ApplicationService.updateEvent(dto);
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Update event successfully!",
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "members":
                    checked = AuthService.checkPermissionWithContext(accountId,
                            RoleContext.APP,
                            "member.cud");
                    if (checked) {
                        UpdateUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateUserRoleDTO.class);
                        RoleRepository.updateSpecifiedForUserWithPrefix(
                                "hehe",
                                dto.getUsername(),
                                prefix + "_" + Pressessor.validateRoleName(dto.getRoleName()));
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK, "Update user successful!",
                                        null)));
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                    break;
                case "roles":
                    checked = AuthService.checkPermissionWithContext(accountId,
                            RoleContext.APP,
                            "role.crud");
                    if (checked) {
                        UpdateRoleDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateRoleDTO.class);
                        RoleRepository.update(dto.getRoleName(), dto.getNewRoleName());
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                    break;
                default:
                    req.getRequestDispatcher(NOTFOUND_VIEW).forward(req, res);
                    break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getStackTrace().toString());
            res.getWriter().write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                            null)));
        } catch (TokenException | NotFoundException | IllegalArgumentException e) {
            logger.error(e.getStackTrace().toString());
            res.getWriter().write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(),
                            null)));
        } catch (AuthException e) {
            logger.error(e.getStackTrace().toString());
            res.getWriter().write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.UNAUTHORIZED, e.getMessage(),
                            null)));
        } finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        Cookie[] cookies = req.getCookies();
        String name = req.getParameter("name");
        PrintWriter out = res.getWriter();
        boolean checked;
        try {
            String accountId = AuthService.handleCookieAndGetAccountId(cookies);
            switch (route) {
                case "roles":
                    // TODO: delete role with is_default is false
                    break;
                case "events":
                    checked = RoleRepository.existPermissionWithContext(RoleContext.APP,
                            Arrays.asList("event.cud"),
                            accountId);
                    if (checked) {
                        DeleteEventDTO dto = HttpUtil.getBodyContentFromReq(req, DeleteEventDTO.class);
                        EventRepository.delete(dto.getEventId());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Delete event successfully!",
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "members":
                    checked = RoleRepository.existPermissionWithContext(RoleContext.APP,
                            Arrays.asList("member.cud"),
                            accountId);
                    if (checked) {
                        CDUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDUserRoleDTO.class);
                        UserAccountRepository.delete(dto.getUsername());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Delete %s successfully!".formatted(dto.getUsername()),
                                        null)));
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "guilds":
                    // TODO: Guild delete
                    break;
                case "permission":
                    checked = RoleRepository.existPermissionWithContext(RoleContext.APP,
                            Arrays.asList("role.cud"),
                            accountId);
                    if (checked) {
                        CDPermissionDTO dto = HttpUtil.getBodyContentFromReq(req, CDPermissionDTO.class);
                        RoleRepository.deletePermission(
                                dto.getRoleName(),
                                dto.getPermissions());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Delete permission to %s successfully!".formatted(dto.getRoleName()),
                                        null)));
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                    break;
                default:
                    throw new NotFoundException("Not found the page!");
            }
        } catch (AuthException e) {
            out.write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.UNAUTHORIZED, e.getMessage(),
                            null)));
        } catch (SQLException | ClassNotFoundException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
            out.write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                            null)));
        } catch (NotFoundException e) {
            out.write(gson.toJson(
                    new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(),
                            null)));
        } finally {
            out.flush();
            out.close();
        }
    }
}
