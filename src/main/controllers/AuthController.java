package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import constants.ResponseStatus;
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
	private final String NOTIFYERROR_VIEW = "/view/notifyError.jsp";

	private Gson gson = new Gson();
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	private String redirectView;

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
				default:
					req.setAttribute("response", gson.toJson(
							new ResponseDTO<>(
									ResponseStatus.NOT_FOUND,
									"NOT FOUND YOUR SPECIFIED PAGE!",
									null)));
					redirectView = NOTIFYERROR_VIEW;
					break;
			}
		} finally {
			req.getRequestDispatcher(redirectView).forward(req, res);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
		PrintWriter out = res.getWriter();
		try {
			switch (route) {
				case "login":
					LoginDTO loginDTO = HttpUtil.getBodyContentFromReq(req, LoginDTO.class);
					String at = AuthService.loginInternal(loginDTO);

					Cookie cookie = new Cookie("access_token", at);
					cookie.setHttpOnly(true);
					cookie.setPath("/");
					res.addCookie(cookie);

					out.write(gson
							.toJson(new ResponseDTO<>(ResponseStatus.OK,
									"Login successfully!", null)));
					break;
				case "signup":
					SignUpDTO signUpDTO = HttpUtil.getBodyContentFromReq(req, SignUpDTO.class);
					AuthService.signUpInternal(signUpDTO);
					out.write(gson
							.toJson(new ResponseDTO<>(ResponseStatus.OK,
									"Sign up successfully!", null)));
					break;
				default:
					throw new NotFoundException("Not found the page!");
			}
		} catch (AuthException | DataEmptyException | IllegalArgumentException e) {
			logger.error(e.getMessage());
			out.write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null)));
		} catch (SQLIntegrityConstraintViolationException e) {
			logger.error(e.getMessage());
			out.write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.BAD_REQUEST,
							"Username is already exist!",
							null)));
		} catch (NotFoundException e) {
			logger.error(e.getMessage());
			out.write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null)));
		} catch (SQLException | TokenException | IOException | ClassNotFoundException e) {
			logger.error(e.getMessage());
			out.write(gson
					.toJson(new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(),
							null)));
		} finally {
			out.flush();
			out.close();
		}
	}
}
