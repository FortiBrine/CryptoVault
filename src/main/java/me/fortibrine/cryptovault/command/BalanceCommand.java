package me.fortibrine.cryptovault.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.fortibrine.cryptovault.CryptoVaultPlugin;
import me.fortibrine.cryptovault.coin.CoinManager;
import me.fortibrine.cryptovault.command.argument.CoinArgument;
import me.fortibrine.cryptovault.config.ConfigManager;
import me.fortibrine.cryptovault.database.CryptoDatabase;
import me.fortibrine.cryptovault.dialog.PluginDialog;
import me.fortibrine.cryptovault.dialog.ShowBalanceDialog;
import me.fortibrine.cryptovault.util.BalanceFormatter;
import me.fortibrine.cryptovault.util.MiniMessageDeserializer;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Command(name = "cryptovault", aliases = {"cv"})
public class BalanceCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final CryptoDatabase cryptoDatabase = plugin.getCryptoDatabase();
    private final CoinManager coinManager = plugin.getCoinManager();
    private final ConfigManager configManager = plugin.getConfigManager();

    @Async
    @Execute(name = "balance")
    @Permission("cryptovault.balance")
    public void execute(@Context Player player) {
        PluginDialog pluginDialog = new ShowBalanceDialog(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                pluginDialog.show();
            }
        }.runTask(plugin);
    }

    @Async
    @Execute(name = "balance")
    @Permission("cryptovault.balance")
    public void execute(@Context Player player, @Async @Arg(CoinArgument.KEY) String coin) {
        double amountCoins = cryptoDatabase.getBalance(player.getUniqueId(), coin);
        double balance = amountCoins * coinManager.getCoinPrice(coin);

        player.sendMessage(MiniMessageDeserializer.deserializePlayer(
                configManager.getMessageConfig().success.balance,
                player,
                Placeholder.unparsed("crypto_balance", BalanceFormatter.format(amountCoins)),
                Placeholder.unparsed("currency_balance", BalanceFormatter.format(balance)),
                Placeholder.unparsed("coin", coinManager.getCoinName(coin)
        )));
    }

}