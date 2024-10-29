package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import exceptions.UserProfileException;
import constants.ResponseStatus;
import dto.UserProfileDTO;
import dto.LoginDTO;
import dto.ResponseDTO;
import dto.SignUpDTO;
import exceptions.NotFoundException;
import services.UserProfileService;

public class UserProfileController {
    public static ResponseDTO<Object> createOne(Connection con, UserProfileDTO data, SignUpDTO signUp)
             throws SQLException, UserProfileException, NotFoundException{
        try {
			UserProfileService.insertProfileInternal(con, data, signUp);
            return new ResponseDTO<>(ResponseStatus.OK, "Update user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(UserProfileException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    public static ResponseDTO<Object> readOne(Connection con, UserProfileDTO data, LoginDTO logIn) throws SQLException, NotFoundException {
        try {
			UserProfileService.readUserProfileInternal(con, data, logIn);
            return new ResponseDTO<>(ResponseStatus.OK, "Read user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		} catch(NotFoundException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}