package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import constants.ResponseStatus;
import constants.RoleContext;
import utils.HttpUtil;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import exceptions.AuthException;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import exceptions.TokenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AuthService;

@WebServlet("/auth/*")
public class AuthController extends HttpServlet {
	private final String SIGNUP_VIEW = "/view/auth/signup.jsp";
	private final String LOGIN_VIEW = "/view/auth/login.jsp";
	private final String NOTFOUND_VIEW = "/view/notfound.jsp";

	private Gson gson = new Gson();
	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
		try {
			switch (route) {
				case "signup":
					req.getRequestDispatcher(SIGNUP_VIEW).forward(req, res);
					break;
				case "login":
					req.getRequestDispatcher(LOGIN_VIEW).forward(req, res);
					break;
				// TODO: remove it
				case "ping":
					Cookie[] cookies = req.getCookies();
					if (cookies.length != 0) {
						for (Cookie cuckie : cookies) {
							if ("access_token".equals(cuckie.getName())) {
								String accessToken = cuckie.getValue();
								boolean checked = AuthService
										.CheckPermissionWithContext(
												RoleContext.APP,
												"app.role.crud",
												accessToken);
								if (checked) {
									req.setAttribute("status", "Yeh");
								} else {
									req.setAttribute("status", "Nah");
								}
							}
						}

					} else {
						req.setAttribute("status", "Cookie Not Found");
					}

					req.getRequestDispatcher("/view/ping.jsp").forward(req, res);
					break;
				default:
					req.getRequestDispatcher(NOTFOUND_VIEW).forward(req, res);
					break;
			}
		} catch (Exception e) {
			logger.error("[Line 55]: ");
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
		try {
			switch (route) {
				case "login":
					LoginDTO loginDTO = HttpUtil.getBodyContentFromReq(req, LoginDTO.class);
					String at = AuthService.loginInternal(loginDTO);

					Cookie cookie = new Cookie("access_token", at);
					cookie.setHttpOnly(true);
					cookie.setSecure(true);
					cookie.setPath("/");
					res.addCookie(cookie);

					res.getWriter().write(gson
							.toJson(new ResponseDTO<>(ResponseStatus.OK,
									"Login successfully!", null)));
					break;
				case "signup":
					SignUpDTO signUpDTO = HttpUtil.getBodyContentFromReq(req, SignUpDTO.class);
					AuthService.signUpInternal(signUpDTO);
					res.getWriter().write(gson
							.toJson(new ResponseDTO<>(ResponseStatus.OK,
									"Sign up successfully!", null)));
					break;
				default:
					req.getRequestDispatcher(NOTFOUND_VIEW).forward(req, res);
					break;
			}
		} catch (AuthException | DataEmptyException | IllegalArgumentException e) {
			logger.error(e.getMessage());
			res.getWriter().write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null)));
		} catch (SQLIntegrityConstraintViolationException e) {
			logger.error(e.getMessage());
			res.getWriter().write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.BAD_REQUEST,
							"Username is already exist!",
							null)));
		} catch (NotFoundException e) {
			logger.error(e.getMessage());
			res.getWriter().write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null)));
		} catch (SQLException | TokenException | IOException | ClassNotFoundException e) {
			logger.error(e.getMessage());
			res.getWriter().write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
							null)));
		}
	}

	public static ResponseDTO<Boolean> checkAccessToken() {
		try {
			AuthService.checkAccessToken();
			return new ResponseDTO<Boolean>(ResponseStatus.OK, "Check access token successfully.", true);
		} catch (Exception e) {
			return new ResponseDTO<Boolean>(ResponseStatus.INTERNAL_SERVER_ERROR,
					"Can not change access token", false);
		}
	}

	public static ResponseDTO<Object> changeAccessToken(SignUpDTO data) {
		try {
			AuthService.changeAccessToken(data);
			return new ResponseDTO<Object>(ResponseStatus.OK, "Change access token successfully.", null);
		} catch (Exception e) {
			return new ResponseDTO<Object>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}

}
