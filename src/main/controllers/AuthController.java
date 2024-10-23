package controllers;

import java.sql.Connection;
import java.sql.SQLException;

import constants.ResponseStatus;
import data.LoginData;
import data.ResponseData;
import data.SignUpData;
import exceptions.AuthException;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import exceptions.TokenException;
import services.AuthService;

public class AuthController {
	public static ResponseData signUp(Connection con, SignUpData data) {
		try {
			AuthService.signUpInternal(con, data);
			return new ResponseData(ResponseStatus.OK, "Sign up successfully!", null);
		} catch (SQLException e) {
			return new ResponseData(ResponseStatus.INTERNAL_SERVER_ERROR,
					"Server have some troubles, please comback again!", null);
		} catch (NotFoundException e) {
			return new ResponseData(ResponseStatus.NOT_FOUND, e.getMessage(), null);
		} catch (DataEmptyException | AuthException e) {
			return new ResponseData(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
		}
	}

	public static ResponseData login(Connection con, LoginData data) {
		try {
			AuthService.loginInternal(con, data);
			return new ResponseData(ResponseStatus.OK, "Login successfully!", null);
		} catch (AuthException e) {
			return new ResponseData(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
		} catch (NotFoundException e) {
			return new ResponseData(ResponseStatus.NOT_FOUND, e.getMessage(), null);
		} catch (SQLException e) {
			return new ResponseData(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch (TokenException e) {
			return new ResponseData(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}

	}
}
