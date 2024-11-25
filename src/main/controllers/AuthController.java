package controllers;

import java.io.IOException;
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
import services.AuthService;



public class AuthController {
	public static ResponseDTO<Object> signUp(SignUpDTO data) {
		try {
			AuthService.signUpInternal(data);
			return new ResponseDTO<>(ResponseStatus.OK, "Sign up successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
					"Server have some troubles, please comback again!", null);
		} catch (NotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
		} catch (DataEmptyException | AuthException | IllegalArgumentException | IOException | ClassNotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
		} 
	}

	public static ResponseDTO<Object> login(LoginDTO data) {
		try {

			AuthService.loginInternal(data);
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

	public static boolean checkAccessToken() {
		try {
			return AuthService.checkAccessToken();
		} catch (Exception e) {
			return false;
		}
	}

	public static ResponseDTO<Object> changeAccessToken(SignUpDTO data) {
		try {
			AuthService.changeAccessToken(data);
			return new ResponseDTO<Object>(ResponseStatus.OK, "Change access token successfully", null);
		} catch (Exception e) {
			return new ResponseDTO<Object>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}

	
}
