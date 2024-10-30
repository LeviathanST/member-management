package controllers;

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
	public static ResponseDTO<Object> signUp(Connection con, SignUpDTO data) {
		try {
			AuthService.signUpInternal(con, data);
			return new ResponseDTO<>(ResponseStatus.OK, "Sign up successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR,
					"Server have some troubles, please comback again!", null);
		} catch (NotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
		} catch (DataEmptyException | AuthException | IllegalArgumentException e) {
			return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
		} 
	}

	public static ResponseDTO<Object> login(Connection con, LoginDTO data) {
		try {

			AuthService.loginInternal(con, data);
			return new ResponseDTO<>(ResponseStatus.OK, "Login successfully!", null);
		} catch (AuthException e) {
			return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
		} catch (NotFoundException e) {
			return new ResponseDTO<>(ResponseStatus.NOT_FOUND, e.getMessage(), null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch (TokenException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}

	}


	
}
