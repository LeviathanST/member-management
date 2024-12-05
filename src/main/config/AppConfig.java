package config;

import annotations.EnvVar;

public class AppConfig {
    @EnvVar("SECRET_KEY")
    private String secretKey;

    @EnvVar("ROUND_HASHING")
    private Integer roundHashing;

    @EnvVar("ADMIN_PASSWORD")
    private String adminPassword;

    @EnvVar("ADMIN_USERNAME")
    private String adminUsername;

    public AppConfig() {
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public int getRoundHashing() {
        return this.roundHashing;
    }

    public String getAdminPassword() {return this.adminPassword;}
    public String getAdminUsername() {return this.adminUsername;}
}
