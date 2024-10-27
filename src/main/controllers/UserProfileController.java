package controllers;


import java.sql.Connection;
import java.sql.SQLException;
import exceptions.UserProfileException;
import constants.ResponseStatus;
import dto.UserProfileDTO;
import dto.ResponseDTO;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import services.UserProfileService;

public class UserProfileController {
    public static ResponseDTO<Object> CreateOne(Connection con, UserProfileDTO data) throws NotFoundException, DataEmptyException, SQLException{
        try {
			UserProfileService.InsertProfileInternal(con, data);
            return new ResponseDTO<>(ResponseStatus.OK, "Update user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseDTO<>(ResponseStatus.INTERNAL_SERVER_ERROR, "Server has some problems", null);
		} catch(DataEmptyException | UserProfileException e) {
            return new ResponseDTO<>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}