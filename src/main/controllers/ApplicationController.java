package controllers;

import utils.HttpUtil;
import dto.UpdateProfileDTO;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;

import constants.ResponseStatus;
import constants.RoleContext;
import dto.ResponseDTO;
import exceptions.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Event;
import models.Permission;
import models.Role;
import models.UserProfile;
import repositories.GenerationRepository;
import repositories.users.UserProfileRepository;
import services.ApplicationService;
import services.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/app/*")
public class ApplicationController extends HttpServlet {
    private final String PROFILE_VIEW = "/view/user/profile.jsp";
    private final String NOTIFYERROR_VIEW = "/view/notifyError.jsp";
    private final String NOTFOUND_VIEW = "/view/notfound.jsp";

    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private String redirectView;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        try {
            switch (route) {
                case "profile":
                    Cookie[] cookies = req.getCookies();
                    String accountId = AuthService.handleCookieAndGetAccountId(cookies);
                    UserProfile userProfile = UserProfileRepository.read(accountId);
                    req.setAttribute("profile", userProfile);
                    redirectView = PROFILE_VIEW;
                    break;
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
            req.getRequestDispatcher(redirectView).forward(req, res);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
        try {
            switch (route) {
                case "profile":
                    Cookie[] cookies = req.getCookies();
                    String accountId = AuthService.handleCookieAndGetAccountId(cookies);
                    boolean checked = AuthService.checkPermissionWithContext(
                            accountId,
                            RoleContext.APP,
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
        }
    }
}
