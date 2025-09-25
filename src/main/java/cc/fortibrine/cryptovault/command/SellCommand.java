package cc.fortibrine.cryptovault.command;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.coin.CoinManager;
import cc.fortibrine.cryptovault.command.argument.CoinArgument;
import cc.fortibrine.cryptovault.config.ConfigManager;
import cc.fortibrine.cryptovault.database.CryptoDatabase;
import cc.fortibrine.cryptovault.economy.EconomyManager;
import cc.fortibrine.cryptovault.util.MiniMessageDeserializer;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Command(name = "cryptovault", aliases = {"cv"})
public class SellCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final EconomyManager economyManager = plugin.getEconomyManager();
    private final CryptoDatabase cryptoDatabase = plugin.getCryptoDatabase();
    private final CoinManager coinManager = plugin.getCoinManager();
    private final ConfigManager configManager = plugin.getConfigManager();

    @Execute(name = "sell")
    @Permission("cryptovault.sell")
    public void execute(@Context Player player, @Async @Arg(CoinArgument.KEY) String coin, @Arg double amount) {

        if (amount <= 0) {
            player.sendMessage(MiniMessageDeserializer.deserializePlayer(
                    configManager.getMessageConfig().usage.all,
                    player
            ));
            return;
        }

        double cost = coinManager.getCoinPrice(coin) * amount;

        if (!cryptoDatabase.withdraw(player.getUniqueId(), coin, amount)) {
            player.sendMessage(MiniMessageDeserializer.deserializePlayer(
                    configManager.getMessageConfig().error.insufficientCrypto,
                    player
            ));
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                economyManager.deposit(player, cost);
                player.sendMessage(MiniMessageDeserializer.deserializePlayer(
                        configManager.getMessageConfig().success.sell,
                        player
                ));
            }
        }.runTaskAsynchronously(plugin);

    }

}
