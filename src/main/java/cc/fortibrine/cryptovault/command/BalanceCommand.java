package cc.fortibrine.cryptovault.command;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.coin.CoinManager;
import cc.fortibrine.cryptovault.command.argument.CoinArgument;
import cc.fortibrine.cryptovault.database.CryptoDatabase;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

@Command(name = "cryptovault", aliases = {"cv"})
public class BalanceCommand {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final CryptoDatabase cryptoDatabase = plugin.getCryptoDatabase();
    private final CoinManager coinManager = plugin.getCoinManager();

    @Async
    @Execute(name = "balance")
    @Permission("cryptovault.balance")
    public void execute(@Context Player player, @Async @Arg(CoinArgument.KEY) String coin) {
        double amountCoins = cryptoDatabase.getBalance(player.getUniqueId(), coin);
        double balance = amountCoins * coinManager.getCoinPrice(coin);

        plugin.getMessageManager().sendMessages("success.balance", player,
                Placeholder.unparsed("crypto_balance", String.valueOf(amountCoins)),
                Placeholder.unparsed("currency_balance", String.valueOf(balance)));
    }

}