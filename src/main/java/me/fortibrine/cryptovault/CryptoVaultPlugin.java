package me.fortibrine.cryptovault;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import lombok.Getter;
import me.fortibrine.cryptovault.coin.CoinManager;
import me.fortibrine.cryptovault.command.BuyCommand;
import me.fortibrine.cryptovault.command.SellCommand;
import me.fortibrine.cryptovault.database.CryptoDatabase;
import me.fortibrine.cryptovault.database.SqliteCryptoDatabase;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public class CryptoVaultPlugin extends JavaPlugin {

    @Getter
    private static CryptoVaultPlugin instance;

    private LiteCommands<CommandSender> liteCommands;
    private CryptoDatabase cryptoDatabase;
    private CoinManager coinManager;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();

        try {
            cryptoDatabase = new SqliteCryptoDatabase(getConfig().getString("database.path"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        coinManager = new CoinManager();
    }

    @Override
    public void onEnable() {
        coinManager.startPriceUpdateTask();
        liteCommands = LiteBukkitFactory.builder(getPluginMeta().getName().toLowerCase(), this)
                .commands(
                        new BuyCommand(),
                        new SellCommand()
                )
                .build();

    }

    @Override
    public void onDisable() {
        instance = null;

        cryptoDatabase = null;
        coinManager = null;

        if (liteCommands != null) {
            liteCommands.unregister();
        }
    }

}
