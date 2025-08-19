package cc.fortibrine.cryptovault.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.coin.CoinManager;
import cc.fortibrine.cryptovault.command.argument.CoinArgument;
import cc.fortibrine.cryptovault.database.CryptoDatabase;
import cc.fortibrine.cryptovault.economy.EconomyManager;
import org.bukkit.entity.Player;

@Command(name = "cryptovault", aliases = {"cv"})
public class SellCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final EconomyManager economyManager = plugin.getEconomyManager();
    private final CryptoDatabase cryptoDatabase = plugin.getCryptoDatabase();
    private final CoinManager coinManager = plugin.getCoinManager();

    @Execute(name = "sell")
    public void execute(@Context Player player, @Arg(CoinArgument.KEY) String coin, @Arg double amount) {
        double cost = coinManager.getCoinPrice(coin) * amount;

        if (!cryptoDatabase.withdraw(player.getUniqueId(), coin, amount)) {

            return;
        }

        economyManager.deposit(player, cost);

    }

}
