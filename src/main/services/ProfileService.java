package services;

import java.sql.Connection;
import java.sql.SQLException;
import models.UserProfile;
import data.ProfileData;
import exceptions.DataEmptyException;


public class ProfileService {
    public static void InsertProfileInternal(Connection con, ProfileData data)  throws SQLException, DataEmptyException{
        if (data.getFullName() == null) 
			throw new DataEmptyException("Full name is empty");
		else if (data.getSex() == null)
                throw new DataEmptyException("Sex is null");
        else if (data.getStudentCode() == null)
                throw new DataEmptyException("Student code is null");
        else if (data.getContactEmail() == null)
                throw new DataEmptyException("Contact email is null");
        else if (data.getGeneration() == null)
                throw new DataEmptyException("Generation is null");
        UserProfile.insert(con, data);
        System.out.println("Update profile successfully!");
    }
}
