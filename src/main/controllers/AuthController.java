package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import constants.ResponseStatus;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import exceptions.AuthException;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import exceptions.TokenException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AuthService;

@WebServlet("/auth/*")
public class AuthController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
		try {
			switch (route) {
				case "signup":
					res.setContentType("text/html");
					try (PrintWriter out = res.getWriter()) {
						out.print("SignUp");
					}
					break;
				case "login":
					res.setContentType("text/html");
					try (PrintWriter out = res.getWriter()) {
						out.print("Login");
					}
					break;
				default:
					res.setContentType("text/html");
					try (PrintWriter out = res.getWriter()) {
						out.print("NotFound");
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String route = (req.getPathInfo() != null) ? req.getPathInfo().substring(1) : "";
		try {
			switch (route) {
				case "signup":
					AuthService.signUpInternal(data);
					break;
				case "login":
					AuthService.loginInternal(data);
					break;
				default:
					res.setContentType("text/html");
					try (PrintWriter out = res.getWriter()) {
						out.print("NotFound");
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static ResponseDTO<Object> signUp(SignUpDTO data) {
		try {
			return new ResponseDTO<>(ResponseStatus.OK, "Sign up successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
					"Server have some troubles, please comback again!", null);
		} catch (NotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
		} catch (DataEmptyException | AuthException | IllegalArgumentException | IOException
				| ClassNotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
		}
	}

	public static ResponseDTO<Object> login(LoginDTO data) {
		try {

			return new ResponseDTO<>(ResponseStatus.OK, "Login successfully!", null);
		} catch (AuthException e) {
			return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
		} catch (NotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch (TokenException | IOException | ClassNotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
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
