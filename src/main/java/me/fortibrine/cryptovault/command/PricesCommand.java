package me.fortibrine.cryptovault.command;

import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.fortibrine.cryptovault.CryptoVaultPlugin;
import me.fortibrine.cryptovault.dialog.PluginDialog;
import me.fortibrine.cryptovault.dialog.ShowPricesDialog;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Command(name = "cryptovault", aliases = {"cv"})
public class PricesCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();

    @Async
    @Execute(name = "prices")
    @Permission("cryptovault.prices")
    public void execute(@Context Player player) {
        PluginDialog pluginDialog = new ShowPricesDialog(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                pluginDialog.show();
            }
        }.runTask(plugin);
    }

}
