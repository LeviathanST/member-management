package config;

import annotations.EnvVar;
import lombok.Getter;

@Getter
public class DbConfig {
    /// Sample: localhost:3306
    @EnvVar("DB_URL")
    private String dbUrl;
    /// Sample: databaseName
    @EnvVar("DB_DATABASE")
    private String dbDatabase;
    /// Sample: thisPasswordIsSecure
    @EnvVar("DB_PASSWORD")
    private String dbPassword;

    public DbConfig() {
    }
}
