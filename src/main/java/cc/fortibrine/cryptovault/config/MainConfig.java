package cc.fortibrine.cryptovault.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
public class MainConfig {

    public DatabaseSection database = new DatabaseSection();

    @ConfigSerializable
    public static class DatabaseSection {
        public String type = "sqlite";
        public String path = "plugins/CryptoVault/crypto.db";
    }

    public CoinsSection coins = new CoinsSection();

    @ConfigSerializable
    public static class CoinsSection {

        public Map<String, String> units = Map.of(
                "BTCUSDT", "btc.",
                "ETHUSDT", "eth.",
                "USDCUSDT", "usdt."
        );

        public long updateTime = 600;

    }

}
