package cc.fortibrine.cryptovault.coin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Level;

public class CoinManager {
    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final Map<String, Component> coinNames = new HashMap<>();
    private final Map<String, Double> coinCosts = new HashMap<>();

    public CoinManager() {

    }

    public void reloadNames() {
        coinNames.clear();

        plugin.getConfigManager().getMainConfig().coins.units.forEach((binanceCoin, name) -> {
            coinNames.put(binanceCoin, MiniMessage.miniMessage().deserialize(name));
        });
    }

    public void startPriceUpdateTask() {
        long updateTime = plugin.getConfigManager().getMainConfig().coins.updateTime * 20;

        new BukkitRunnable() {
            @Override
            public void run() {
                updatePrices();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, updateTime);
    }

    public void updatePrices() {
        HttpClient client = HttpClient.newHttpClient();
        for (String symbol : coinNames.keySet()) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.binance.com/api/v3/ticker/price?symbol=" + symbol))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                    double price = json.get("price").getAsDouble();
                    coinCosts.put(symbol, price);
                    plugin.getLogger().info("Updated price for " + symbol + ": $" + price);
                } else {
                    plugin.getLogger().warning("Failed to fetch price for " + symbol + ": HTTP " + response.statusCode());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error fetching price for " + symbol, e);
            }
        }
    }

    public double getCoinPrice(String coin) {
        return coinCosts.get(coin);
    }

    public Component getCoinName(String coin) {
        return coinNames.get(coin);
    }

    public Set<String> getCoinNames() {
        return new HashSet<>(coinNames.keySet());
    }

    public Map<String, Double> getCoinCosts() {
        return new HashMap<>(coinCosts);
    }

}