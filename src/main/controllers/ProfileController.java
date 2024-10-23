package controllers;

import data.ProfileData;
import services.ProfileService;


import java.sql.Connection;

public class ProfileController {
    public static void InputProfileData(Connection con, ProfileData data) {
        try {
			ProfileService.InsertProfileInternal(con, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
