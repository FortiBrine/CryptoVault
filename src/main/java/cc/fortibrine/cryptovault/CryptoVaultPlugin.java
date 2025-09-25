package cc.fortibrine.cryptovault;

import cc.fortibrine.cryptovault.command.BalanceCommand;
import cc.fortibrine.cryptovault.command.PricesCommand;
import cc.fortibrine.cryptovault.command.error.InvalidUsageHandlerImpl;
import cc.fortibrine.cryptovault.command.error.PermissionHandler;
import cc.fortibrine.cryptovault.config.ConfigManager;
import cc.fortibrine.cryptovault.placeholder.CryptoVaultPlaceholder;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import lombok.Getter;
import cc.fortibrine.cryptovault.coin.CoinManager;
import cc.fortibrine.cryptovault.command.BuyCommand;
import cc.fortibrine.cryptovault.command.SellCommand;
import cc.fortibrine.cryptovault.command.argument.CoinArgument;
import cc.fortibrine.cryptovault.database.CryptoDatabase;
import cc.fortibrine.cryptovault.database.SqliteCryptoDatabase;
import cc.fortibrine.cryptovault.economy.EconomyManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

@Getter
public class CryptoVaultPlugin extends JavaPlugin {

    @Getter
    private static CryptoVaultPlugin instance;

    private ConfigManager configManager;

    private LiteCommands<CommandSender> liteCommands;
    private CryptoDatabase cryptoDatabase;
    private CoinManager coinManager;
    private EconomyManager economyManager;

    @Override
    public void onLoad() {
        instance = this;

        Logger.setGlobalLogLevel(Level.OFF);

        try {
            configManager = new ConfigManager(getDataFolder());
            configManager.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            cryptoDatabase = new SqliteCryptoDatabase(configManager.getMainConfig().database.path);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        coinManager = new CoinManager();
        economyManager = new EconomyManager();

    }

    @Override
    public void onEnable() {
        coinManager.startPriceUpdateTask();
        economyManager.setupEconomy();

        liteCommands = LiteBukkitFactory.builder(getPluginMeta().getName().toLowerCase(), this)
                .commands(
                        new BuyCommand(),
                        new SellCommand(),
                        new BalanceCommand(),
                        new PricesCommand()
                )
                .invalidUsage(new InvalidUsageHandlerImpl())
                .missingPermission(new PermissionHandler())
                .argument(String.class, ArgumentKey.of(CoinArgument.KEY), new CoinArgument())
                .build();

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new CryptoVaultPlaceholder().register();
        }

    }

    @Override
    public void onDisable() {
        instance = null;

        cryptoDatabase = null;
        coinManager = null;
        configManager = null;
        economyManager = null;

        if (liteCommands != null) {
            liteCommands.unregister();
        }
    }

}
