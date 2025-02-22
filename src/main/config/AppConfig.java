package config;

import annotations.EnvVar;
import lombok.Getter;

public class AppConfig {
    @EnvVar("SECRET_KEY")
    @Getter
    private String secretKey;

    @EnvVar("ROUND_HASHING")
    @Getter
    private Integer roundHashing;

    public AppConfig() {
    }
}
