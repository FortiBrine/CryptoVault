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

@Command(name = "cryptovault", aliases = {"cv"})
public class SellCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final EconomyManager economyManager = plugin.getEconomyManager();
    private final CryptoDatabase cryptoDatabase = plugin.getCryptoDatabase();
    private final CoinManager coinManager = plugin.getCoinManager();

    @Async
    @Execute(name = "sell")
    @Permission("cryptovault.sell")
    public void execute(@Context Player player, @Async @Arg(CoinArgument.KEY) String coin, @Arg double amount) {

        if (amount <= 0) {
            plugin.getMessageManager().sendMessages("usage.sell", player);
            return;
        }

        double cost = coinManager.getCoinPrice(coin) * amount;

        if (!cryptoDatabase.withdraw(player.getUniqueId(), coin, amount)) {
            plugin.getMessageManager().sendMessages("error.insufficient_crypto", player);
            return;
        }

        economyManager.deposit(player, cost);
        plugin.getMessageManager().sendMessages("success.sell", player);

    }

}
