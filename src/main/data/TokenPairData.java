package data;

public class TokenPairData {
	private String accessToken;
	private String refreshToken;

	public TokenPairData(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String newAccessToken) {
		this.accessToken = newAccessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshtoken(String newRefreshtoken) {
		this.refreshToken = newRefreshtoken;
	}
}
