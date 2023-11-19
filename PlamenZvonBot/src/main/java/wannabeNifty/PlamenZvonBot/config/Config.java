package wannabeNifty.PlamenZvonBot.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import wannabeNifty.PlamenZvonBot.Main;

import java.io.File;

public class Config {
    public static String Token;
    public static String GuildId;
    public static String CreateConfigPath() {
        String CurrentWorkingDir = System.getProperty("user.dir");
        String ConfigFilePath = CurrentWorkingDir + File.separator + "config.json";
        return ConfigFilePath;
    }
    public static boolean isConfigFileExists() {
        String ConfigPath = CreateConfigPath();
        File ConfigFile = new File(ConfigPath);
        if (ConfigFile.exists()) {
            Main.logger.info("Success the config file exists");
            return true;
        }
        else {
            Main.logger.error("Error could not find config file");
            return false;
        }
    }
    public static void LoadConfigFile(String Path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode RootNode = mapper.readTree(new File(Path));
            String Token = RootNode.path("Token").asText();
            String GuildId = RootNode.path("GuildId").asText();
            // Other properties soon
        }
        catch (Exception e) {
            Main.logger.error("An error occured" + e.getMessage());
        }
    }
    public static void Config() {
        if (isConfigFileExists()) {
            LoadConfigFile(CreateConfigPath());
        }
        else {
            Main.logger.error("Error while loading config file");
        }
    }
}
