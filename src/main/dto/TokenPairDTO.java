package dto;

import java.util.Date;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import config.AppConfig;
import utils.EnvLoader;

// TODO: Refresh token
public class TokenPairDTO {
	private String accessToken;
	private String refreshToken;

	public TokenPairDTO(TokenPairDTO data) {
		this.accessToken = data.accessToken;
		this.refreshToken = data.refreshToken;
	}

	public TokenPairDTO(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public static TokenPairDTO GenerateNew(ClaimsDTO data) {
		AppConfig appConfig = EnvLoader.load(AppConfig.class);
		Algorithm algo = Algorithm.HMAC384(appConfig.getSecretKey());

		System.out.println(data.getAccountId());

		String accessToken = JWT
				.create()
				.withIssuedAt(new Date())
				.withClaim("account_id", data.getAccountId())
				.withClaim("user_role_id", data.getUserRoleId())
				.sign(algo);

		String refreshToken = JWT.create().withIssuedAt(new Date()).sign(algo);
		TokenPairDTO tokenPairDTO = new TokenPairDTO(accessToken, refreshToken);

		return tokenPairDTO;
	}

	public static DecodedJWT Verify(String accessToken) throws JWTVerificationException {
		AppConfig appConfig = EnvLoader.load(AppConfig.class);

		Algorithm algo = Algorithm.HMAC384(appConfig.getSecretKey());
		JWTVerifier verifyer = JWT.require(algo).build();
		DecodedJWT jwt = verifyer.verify(accessToken);

		return jwt;
	}
}
