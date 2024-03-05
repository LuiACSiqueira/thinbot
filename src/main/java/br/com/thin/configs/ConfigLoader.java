package br.com.thin.configs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Loader for the properties on /app.properties file
 * */
public class ConfigLoader {
    public static String getDiscordAppToken() throws IOException {
        return getProperty("discordAppToken");
    }

    public static String getOpenAiApiKey() throws IOException {
        return getProperty("openAiApiKey");
    }

    private static String getProperty(String property) throws IOException {
        FileReader fileReader = new FileReader("app.properties");
        Properties properties = new Properties();
        properties.load(fileReader);

        return properties.getProperty(property);
    }
}
