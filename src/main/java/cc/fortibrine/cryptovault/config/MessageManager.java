package cc.fortibrine.cryptovault.config;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MessageManager {

    private final File configFile;
    private final YamlConfiguration config;

    public MessageManager() {
        Plugin plugin = CryptoVaultPlugin.getInstance();
        configFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!configFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void sendMessages(String path, CommandSender sender, TagResolver... resolvers) {
        if (!config.contains(path)) return;
        if (!config.isList(path)) return;

        config.getStringList(path)
                .stream()
                .map(message -> MiniMessage.miniMessage().deserialize(message,
                        TagResolver.builder()
                                .resolver(Placeholder.unparsed("player", sender.getName()))
                                .resolvers(resolvers)
                                .build()
                        ))
                .forEach(sender::sendMessage);

    }

    public void reload() {
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

}
