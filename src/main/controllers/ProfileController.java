package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import exceptions.ProfileException;
import constants.ResponseStatus;
import data.ProfileData;
import data.ResponseData;
import exceptions.DataEmptyException;
import exceptions.NotFoundException;
import services.ProfileService;

public class ProfileController {
    public static ResponseData<Object> CreateOne(Connection con, ProfileData data) throws NotFoundException{
        try {
			ProfileService.InsertProfileInternal(con, data);
            return new ResponseData<Object>(ResponseStatus.OK, "Update user profile successfully!", null);
		} catch (SQLException e) {
			return new ResponseData<Object>(ResponseStatus.INTERNAL_SERVER_ERROR, "Server has some problems", null);
		} catch(DataEmptyException | ProfileException e) {
            return new ResponseData<Object>(ResponseStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}