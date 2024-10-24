package config;

import annotations.EnvVar;

public class DbConfig {
    @EnvVar("DB_URL")
    private String dbUrl;
    @EnvVar("DB_PASSWORD")
    private String dbPassword;

    public DbConfig() {
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
