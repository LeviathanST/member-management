package data;

import java.util.Date;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

// TODO: Refresh token
public class TokenPairData {
	private String accessToken;
	private String refreshToken;

	public TokenPairData(TokenPairData data) {
		this.accessToken = data.accessToken;
		this.refreshToken = data.refreshToken;
	}

	public TokenPairData(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public static TokenPairData GenerateNew(ClaimsData data) {
		String secretKey = Optional.ofNullable(System.getenv("SECRET_KEY")).orElse("huyngu");
		Algorithm algo = Algorithm.HMAC384(secretKey);

		String accessToken = JWT
				.create()
				.withIssuedAt(new Date())
				.withClaim("username", data.getUsername())
				.withClaim("user_role_id", data.getUserRoleId())
				.withClaim("user_guild_role_ids", data.getUserGuildRoleId())
				.withClaim("user_crew_role_ids", data.getUserCrewRoleId())
				.sign(algo);

		String refreshToken = JWT.create().withIssuedAt(new Date()).sign(algo);
		TokenPairData tokenPairData = new TokenPairData(accessToken, refreshToken);

		return tokenPairData;
	}

	public static DecodedJWT Verify(String accessToken) {
		String secretKey = Optional.ofNullable(System.getenv("SECRET_KEY")).orElse("huyngu");

		Algorithm algo = Algorithm.HMAC384(secretKey);
		JWTVerifier verifyer = JWT.require(algo).build();
		DecodedJWT jwt = verifyer.verify(accessToken);

		return jwt;
	}
}
