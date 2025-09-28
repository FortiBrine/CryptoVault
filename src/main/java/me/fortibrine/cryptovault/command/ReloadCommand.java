package me.fortibrine.cryptovault.command;

import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.fortibrine.cryptovault.CryptoVaultPlugin;
import me.fortibrine.cryptovault.config.ConfigManager;
import me.fortibrine.cryptovault.util.MiniMessageDeserializer;
import org.bukkit.entity.Player;

import java.io.IOException;

@Command(name = "cryptovault", aliases = {"cv"})
public class ReloadCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final ConfigManager configManager = plugin.getConfigManager();

    @Async
    @Execute(name = "reload")
    @Permission("cryptovault.reload")
    public void execute(@Context Player player) {
        try {
            plugin.getConfigManager().load();

            player.sendMessage(MiniMessageDeserializer.deserializePlayer(
                    configManager.getMessageConfig().success.reload,
                    player
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
