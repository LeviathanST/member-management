package controllers;

import java.nio.file.Paths;
import java.sql.Connection;

import constants.RoleType;
import data.LoginData;
import data.SignUpData;
import data.TokenPairData;
import models.Guild;
import models.roles.GuildRole;
import services.AuthService;
import services.TokenService;
import utils.PermissionCache;

public class AuthController {
	public static void signUp(Connection con, SignUpData data) {
		try {
			String accessToken = TokenService.loadFromFile(Paths.get("auth.json")).getAccessToken();
			String accountId = TokenPairData.Verify(accessToken).getClaim("account_id").asString();

			int guildId = Guild.getIdByName(con, "Media");
			int guildRoleId = GuildRole.getIdByName(con, guildId, "Member");
			if (!PermissionCache.contains(RoleType.Guild, guildRoleId, "View")) {
				PermissionCache.addPermission(con, accountId, RoleType.Guild, guildRoleId);
			}

			AuthService.signUpInternal(con, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void login(Connection con, LoginData data) {
		try {
			AuthService.loginInternal(con, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
