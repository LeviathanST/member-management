package config;

import annotations.EnvVar;
import lombok.Getter;

@Getter
public class DbConfig {
    /// Sample: localhost:3306
    @EnvVar("DB_URL")
    @Getter
    private String dbUrl;
    /// Sample: databaseName
    @EnvVar("DB_DATABASE")
    @Getter
    private String dbDatabase;
    /// Sample: thisPasswordIsSecure
    @EnvVar("DB_PASSWORD")
    @Getter
    private String dbPassword;

    public DbConfig() {
    }
}
