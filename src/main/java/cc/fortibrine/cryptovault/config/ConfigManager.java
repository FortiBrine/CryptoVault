package cc.fortibrine.cryptovault.config;

import lombok.Getter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final File mainConfigFile;
    private final ConfigurationLoader<?> mainConfigLoader;

    private final File messageConfigFile;
    private final ConfigurationLoader<?> messageConfigLoader;

    @Getter
    private MainConfig mainConfig;

    @Getter
    private MessagesConfig messageConfig;

    public ConfigManager(File dataFolder) throws IOException {
        mainConfigFile = new File(dataFolder, "config.yml");
        messageConfigFile = new File(dataFolder, "messages.yml");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        mainConfigLoader = YamlConfigurationLoader.builder()
                .path(mainConfigFile.toPath())
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        messageConfigLoader = YamlConfigurationLoader.builder()
                .path(messageConfigFile.toPath())
                .nodeStyle(NodeStyle.BLOCK)
                .build();
    }

    public void load() throws IOException {
        // messages.yml
        ConfigurationNode messagesConfigRoot = messageConfigLoader.load();
        messageConfig = messagesConfigRoot.get(MessagesConfig.class, new MessagesConfig());

        if (!messageConfigFile.exists()) {
            messagesConfigRoot.set(MessagesConfig.class, messageConfig);
            messageConfigLoader.save(messagesConfigRoot);
        }

        // config.yml
        ConfigurationNode mainConfigRoot = mainConfigLoader.load();
        mainConfig = mainConfigRoot.get(MainConfig.class, new MainConfig());

        if (!mainConfigFile.exists()) {
            mainConfigRoot.set(MainConfig.class, mainConfig);
            mainConfigLoader.save(mainConfigRoot);
        }

    }

}
