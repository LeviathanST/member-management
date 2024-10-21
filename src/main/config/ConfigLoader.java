package config;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private Properties properties;

    public ConfigLoader(String configFilePath) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load config file: " + configFilePath, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
