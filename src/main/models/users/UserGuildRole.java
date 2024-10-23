package models.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import exceptions.NotFoundException;

public class UserGuildRole {
	public static List<Integer> getIdByAccountId(Connection con, String account_id)
			throws SQLException, NotFoundException {
		String query = """
				SELECT id FROM user_guild_role WHERE account_id = ?
				""";
		List<Integer> list = new ArrayList<>();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, account_id);

		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			list.add(rs.getInt("id"));
		}
		return list;
	}
}
