package controllers;

import services.AuthService;
import services.CrewService;
import services.ApplicationService;
import dto.role.GetUserDTO;
import dto.ResponseDTO;
import dto.role.UpdateRoleDTO;
import dto.crew.UpdateCrewDTO;
import dto.role.CDUserRoleDTO;
import dto.role.CDPermissionDTO;
import dto.role.UpdateUserRoleDTO;
import repositories.events.CrewEventRepository;
import repositories.CrewRepository;
import repositories.RoleRepository;
import dto.crew.DeleteCrewEventDTO;
import dto.crew.GetCrewEventDTO;
import dto.crew.CUCrewEventDTO;
import dto.crew.CrewInfoDTO;
import constants.RoleContext;
import constants.ResponseStatus;
import exceptions.AuthException;
import exceptions.NotFoundException;
import utils.HttpUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/crew/*")
public class CrewController extends HttpServlet {
    private final String NOTIFYERROR_VIEW = "/view/notifyError.jsp";
    private final String ROLE_VIEW = "/view/crew/role.jsp";
    private final String MEMBER_VIEW = "/view/crew/member.jsp";
    private final String EVENT_VIEW = "/view/crew/event.jsp";
    private final String INFO_VIEW = "/view/crew/info.jsp";

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
                        CrewInfoDTO info = CrewRepository.getInfo(name);
                        req.setAttribute("info", info);
                        req.setAttribute("ade",
                                AuthService.checkRoleAndPermission(accountId, name, RoleContext.CREW,
                                        "event.cud"));
                        redirectView = INFO_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "events":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.CREW,
                            "view");
                    if (checked) {
                        List<GetCrewEventDTO> crewEvents = CrewEventRepository.getAllCrewEvent(name);
                        req.setAttribute("crewEvents", crewEvents);
                        req.setAttribute("ade",
                                AuthService.checkRoleAndPermission(accountId, name, RoleContext.CREW,
                                        "event.cud"));
                        redirectView = EVENT_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "members":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.CREW,
                            "view");
                    if (checked) {
                        String code = CrewRepository.getCodeByName(name);
                        List<String> roles = RoleRepository.getAllByPrefix(code);
                        req.setAttribute("roles", gson.toJson(roles));
                        req.setAttribute("ade",
                                AuthService.checkRoleAndPermission(accountId, name, RoleContext.CREW,
                                        "member.cud"));

                        List<GetUserDTO> members = RoleRepository
                                .getUsersByPrefix(code);
                        req.setAttribute("members", members);
                        redirectView = MEMBER_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "roles":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.CREW,
                            "role.crud");
                    if (checked) {
                        List<String> roles = CrewService.getAllRolesByCrewName(name);
                        req.setAttribute("roles", roles);

                        List<String> permissions = RoleRepository
                                .getAllPermissionByContext(RoleContext.CREW);
                        req.setAttribute("permissions", gson.toJson(permissions));
                        redirectView = ROLE_VIEW;
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN!");
                    }
                case "permissions":
                    checked = AuthService.checkRoleAndPermission(accountId, name, RoleContext.CREW,
                            "role.crud");
                    if (checked) {
                        String roleName = req.getParameter("role");
                        if (roleName != null && !roleName.isBlank()) {
                            List<String> permissionsOfRole = RoleRepository
                                    .getAllPermissionOfARole(CrewRepository.getCodeByName(name) + "_" + roleName);
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
                            RoleContext.CREW,
                            "event.cud");
                    if (checked) {
                        CUCrewEventDTO dto = HttpUtil.getBodyContentFromReq(req, CUCrewEventDTO.class);
                        CrewService.insertEvent(name, dto);
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
                            RoleContext.CREW,
                            "member.cud");
                    if (checked) {
                        CDUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDUserRoleDTO.class);
                        RoleRepository.addDefaultForUserByPrefix(dto.getUsername(),
                                CrewRepository.getCodeByName(name));
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Add user %s to %s guild successful!".formatted(dto.getUsername(), name),
                                        null)));
                        break;
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "permissions":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.CREW,
                            "role.crud");
                    if (checked) {
                        CDPermissionDTO dto = HttpUtil.getBodyContentFromReq(req, CDPermissionDTO.class);
                        RoleRepository.insertPermissionRole(
                                CrewRepository.getCodeByName(name) + "_" + dto.getRoleName(),
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
            logger.error("ERROR");
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
                            RoleContext.CREW,
                            "update");
                    if (checked) {
                        UpdateCrewDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateCrewDTO.class);
                        CrewRepository.update(name, dto.getNewCrewName());
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
                            RoleContext.CREW,
                            "role.crud");
                    if (checked) {
                        String prefix = CrewRepository.getCodeByName(name);
                        UpdateRoleDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateRoleDTO.class);
                        RoleRepository.update(
                                prefix + "_" + dto.getRoleName(),
                                prefix + "_" + dto.getNewRoleName());
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                    break;
                case "events":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.CREW,
                            "event.cud");
                    if (checked) {
                        CUCrewEventDTO dto = HttpUtil.getBodyContentFromReq(req, CUCrewEventDTO.class);
                        CrewService.updateEvent(name, dto);
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
                            RoleContext.CREW,
                            "member.cud");
                    if (checked) {
                        UpdateUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, UpdateUserRoleDTO.class);
                        RoleRepository.updateSpecifiedForUserWithPrefix(
                                CrewRepository.getCodeByName(name),
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
                    // TODO: delete role with is_default is false
                    break;
                case "events":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.CREW,
                            "event.cud");
                    if (checked) {
                        DeleteCrewEventDTO dto = HttpUtil.getBodyContentFromReq(req, DeleteCrewEventDTO.class);
                        CrewEventRepository.delete(dto.getEventId());
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
                            RoleContext.CREW,
                            "member.cud");
                    if (checked) {
                        CDUserRoleDTO dto = HttpUtil.getBodyContentFromReq(req, CDUserRoleDTO.class);
                        RoleRepository.deleteUserFromPrefix(dto.getUsername(),
                                CrewRepository.getCodeByName(name));
                        out.write(gson.toJson(
                                new ResponseDTO<>(ResponseStatus.OK,
                                        "Delete %s from %s successfully!".formatted(dto.getUsername(), name),
                                        null)));
                    } else {
                        throw new AuthException("FORBIDDEN");
                    }
                case "permissions":
                    checked = AuthService.checkRoleAndPermission(accountId, name,
                            RoleContext.CREW,
                            "role.crud");
                    if (checked) {
                        CDPermissionDTO dto = HttpUtil.getBodyContentFromReq(req, CDPermissionDTO.class);
                        RoleRepository.deletePermission(
                                CrewRepository.getCodeByName(name) + "_" + dto.getRoleName(),
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
