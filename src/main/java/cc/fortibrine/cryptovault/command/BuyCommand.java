package cc.fortibrine.cryptovault.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.coin.CoinManager;
import cc.fortibrine.cryptovault.command.argument.CoinArgument;
import cc.fortibrine.cryptovault.database.CryptoDatabase;
import cc.fortibrine.cryptovault.economy.EconomyManager;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Command(name = "cryptovault", aliases = {"cv"})
public class BuyCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final EconomyManager economyManager = plugin.getEconomyManager();
    private final CryptoDatabase cryptoDatabase = plugin.getCryptoDatabase();
    private final CoinManager coinManager = plugin.getCoinManager();

    @Execute(name = "buy")
    @Permission("cryptovault.buy")
    public void execute(@Context Player player, @Async @Arg(CoinArgument.KEY) String coin, @Arg double amount) {
        if (amount <= 0) {
            plugin.getMessageManager().sendMessages("usage.buy", player);
            return;
        }

        double cost = coinManager.getCoinPrice(coin) * amount;

        if (!economyManager.withdraw(player, cost)) {
            plugin.getMessageManager().sendMessages("error.insufficient_currency", player);
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                cryptoDatabase.deposit(player.getUniqueId(), coin, amount);
                plugin.getMessageManager().sendMessages("success.buy", player);
            }
        }.runTaskAsynchronously(plugin);

    }

}
