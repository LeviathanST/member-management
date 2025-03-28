package controllers;

import dto.*;
import dto.guild.GetGuildEventDTO;
import dto.guild.GuildInfoDTO;
import dto.guild.UpdateGuildDTO;
import dto.guild.CUGuildEventDTO;
import dto.guild.DeleteGuildEventDTO;
import dto.role.CDPermissionDTO;
import dto.role.GetUserDTO;
import dto.role.CDRoleDTO;
import dto.role.UpdateRoleDTO;
import dto.role.CDUserRoleDTO;
import dto.role.UpdateUserRoleDTO;
import exceptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Guild;
import models.GuildEvent;
import models.UserProfile;
import repositories.GenerationRepository;
import repositories.GuildRepository;
import repositories.events.GuildEventRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import constants.ResponseStatus;
import constants.RoleContext;
import repositories.RoleRepository;
import services.ApplicationService;
import services.AuthService;
import services.GuildService;
import utils.HttpUtil;

@WebServlet("/guild/*")
public class GuildController extends HttpServlet {
    private final String NOTIFYERROR_VIEW = "/view/notifyError.jsp";
    private final String ROLE_VIEW = "/view/guild/role.jsp";
    private final String MEMBER_VIEW = "/view/guild/member.jsp";
    private final String EVENT_VIEW = "/view/guild/event.jsp";
    private final String INFO_VIEW = "/view/guild/info.jsp";

    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(GuildController.class);
    private String redirectView;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        Cookie[] cookies = req.getCookies();
        String name = req.getParameter("name");
        PrintWriter out = res.getWriter();
        boolean checked;

        try {
            String accountId = AuthService.handleCookieAndGetAccountId(cookies);
            switch (route) {
                case "info":
                    checked = AuthService.checkPermissionWithContext(accountId, RoleContext.APP,
                            "view");
                    if (checked) {
                        GuildInfoDTO info = GuildRepository.getInfo(name);
                        req.setAttribute("info", info);
                        req.setAttribute("ade",
                                AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                                        "event.cud"));
                        redirectView = INFO_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "events":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                            "view");
                    if (checked) {
                        List<GetGuildEventDTO> guildEvents = GuildEventRepository.getAllGuildEvent(name);
                        req.setAttribute("guildEvents", guildEvents);
                        req.setAttribute("ade",
                                AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                                        "event.cud"));
                        redirectView = EVENT_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "members":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                            "view");

                    if (checked) {
                        List<String> roles = RoleRepository.getAllByPrefix(GuildRepository.getCodeByName(name));
                        req.setAttribute("roles", gson.toJson(roles));
                        req.setAttribute("ade",
                                AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                                        "event.cud"));

                        List<GetUserDTO> members = RoleRepository
                                .getUsersByPrefix(GuildRepository.getCodeByName(name));
                        req.setAttribute("members", members);
                        redirectView = MEMBER_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "roles":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        List<String> roles = GuildService.getAllRolesByGuildName(name);
                        req.setAttribute("roles", roles);

                        List<String> permissions = RoleRepository
                                .getAllPermissionByContext(RoleContext.GUILD);
                        req.setAttribute("permissions", gson.toJson(permissions));
                        redirectView = ROLE_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "permissions":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        String roleName = req.getParameter("role");
                        if (roleName != null && !roleName.isBlank()) {
                            List<String> permissionsOfRole = RoleRepository
                                    .getAllPermissionOfARole(GuildRepository.getCodeByName(name) + "_" + roleName);
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
                    throw new NotFoundException("A page is not found!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
                            null)));
            redirectView = NOTIFYERROR_VIEW;
        } catch (AuthException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<>(ResponseStatus.UNAUTHORIZED, e.getMessage(),
                            null)));
            redirectView = NOTIFYERROR_VIEW;
        } catch (NotFoundException e) {
            logger.error(e.getStackTrace().toString());
            req.setAttribute("response", gson.toJson(
                    new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(),
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
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "event.cud");
                    if (checked) {
                        CUGuildEventDTO dto = HttpUtil.getBodyContentFromReq(req, CUGuildEventDTO.class);
                        GuildService.insertGuildEvent(name, dto);
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Create event for %s successful!".formatted(name),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "members":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "member.cud");
                    if (checked) {
                        CDUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDUserRoleDTO.class);
                        RoleRepository.addDefaultForUserByPrefix(dto.getUsername(),
                                GuildRepository.getCodeByName(name));
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Add user %s to %s guild successful!".formatted(dto.getUsername(), name),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "roles":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        CDRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDRoleDTO.class);
                        dto.checkNullOrEmpty();
                        RoleRepository.create(
                                GuildRepository.getCodeByName(name) + "_" + dto.getRoleName());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Created role %s successfully!".formatted(dto.getRoleName()),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "permissions":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        CDPermissionDTO dto = HttpUtil.getBodyContentFromReq(req, CDPermissionDTO.class);
                        RoleRepository.insertPermissionRole(
                                GuildRepository.getCodeByName(name) + "_" + dto.getRoleName(),
                                dto.getPermissions(), RoleContext.GUILD);
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
        String name = req.getParameter("name");
        PrintWriter out = res.getWriter();
        boolean checked;
        try {
            String accountId = AuthService.handleCookieAndGetAccountId(cookies);
            switch (route) {
                case "info":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "update");
                    if (checked) {
                        UpdateGuildDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateGuildDTO.class);
                        GuildRepository.update(name, dto.getNewGuildName());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Update guild info successfully!",
                                        null)));
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                    break;
                case "roles":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        String prefix = GuildRepository.getCodeByName(name);
                        UpdateRoleDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateRoleDTO.class);
                        RoleRepository.update(
                                prefix + "_" + dto.getRoleName(),
                                prefix + "_" + dto.getNewRoleName());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Update role successfully!",
                                        null)));
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                    break;
                case "events":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "event.cud");
                    if (checked) {
                        CUGuildEventDTO dto = HttpUtil.getBodyContentFromReq(req, CUGuildEventDTO.class);
                        GuildService.updateGuildEvent(name, dto);
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Update event successfully!",
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "members":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "member.crud");
                    if (checked) {
                        UpdateUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateUserRoleDTO.class);
                        RoleRepository.updateSpecifiedForUserWithPrefix(
                                GuildRepository.getCodeByName(name),
                                dto.getUsername(),
                                dto.getRoleName());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK, "Update user successful!",
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
        } catch (SQLException | ClassNotFoundException | IOException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
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
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        CDRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDRoleDTO.class);
                        dto.checkNullOrEmpty();
                        RoleRepository.delete(GuildRepository.getCodeByName(name), dto.getRoleName());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Delete role successfully!",
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "events":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "event.cud");
                    if (checked) {
                        DeleteGuildEventDTO dto = HttpUtil.getBodyContentFromReq(req, DeleteGuildEventDTO.class);
                        GuildEventRepository.delete(dto.getEventId());
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Delete event successfully!",
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "members":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "member.crud");
                    if (checked) {
                        CDUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDUserRoleDTO.class);
                        RoleRepository.deleteUserFromPrefix(dto.getUsername(),
                                GuildRepository.getCodeByName(name));
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Delete %s from %s successfully!".formatted(dto.getUsername(), name),
                                        null)));
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "permission":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.GUILD,
                            "role.crud");
                    if (checked) {
                        CDPermissionDTO dto = HttpUtil.getBodyContentFromReq(req, CDPermissionDTO.class);
                        RoleRepository.deletePermission(
                                GuildRepository.getCodeByName(name) + "_" + dto.getRoleName(),
                                dto.getPermissions(), RoleContext.GUILD);
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
