package config;

import annotations.EnvVar;

public class AppConfig {
    @EnvVar("SECRET_KEY")
    private String secretKey;

    @EnvVar("ROUND_HASHING")
    private int roundHashing;

    public AppConfig() {
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public int getRoundHashing() {
        return this.roundHashing;
    }
}
