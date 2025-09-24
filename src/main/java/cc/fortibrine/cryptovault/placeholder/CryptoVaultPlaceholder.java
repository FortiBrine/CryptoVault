package cc.fortibrine.cryptovault.placeholder;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.coin.CoinManager;
import cc.fortibrine.cryptovault.database.CryptoDatabase;
import cc.fortibrine.cryptovault.util.BalanceFormatter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CryptoVaultPlaceholder extends PlaceholderExpansion {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final CryptoDatabase cryptoDatabase = plugin.getCryptoDatabase();
    private final CoinManager coinManager = plugin.getCoinManager();

    private final LoadingCache<UUID, Double> balanceCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public Double load(@NotNull UUID uuid) {
                    double balance = 0;

                    for (String coin : coinManager.getCoinNames()) {
                        double balancePerCoin = cryptoDatabase.getBalance(uuid, coin);
                        balance += balancePerCoin;
                    }

                    return balance;
                }
            });

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getPluginMeta().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        if (params.equalsIgnoreCase("balance")) {
            return BalanceFormatter.format(balanceCache.getUnchecked(player.getUniqueId()));
        }

        return null;
    }
}
