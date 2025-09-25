package cc.fortibrine.cryptovault.coin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Level;

public class CoinManager {
    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final Map<String, Double> coinCosts = new HashMap<>();

    public CoinManager() {

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
        for (var entry : plugin.getConfigManager().getMainConfig().coins.apiUnits.entrySet()) {
            String humanizedCoinName = entry.getKey();
            String apiCoinName = entry.getValue();

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.binance.com/api/v3/ticker/price?symbol=" + apiCoinName))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                    double price = json.get("price").getAsDouble();
                    coinCosts.put(humanizedCoinName, price);
                    plugin.getLogger().info("Updated price for " + humanizedCoinName + ": $" + price);
                } else {
                    plugin.getLogger().warning("Failed to fetch price for " + humanizedCoinName + ": HTTP " + response.statusCode());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error fetching price for " + humanizedCoinName, e);
            }
        }
        client.close();
    }

    public Set<String> getCoins() {
        return coinCosts.keySet();
    }

    public double getCoinPrice(String coin) {
        return coinCosts.get(coin);
    }

    public Map<String, Double> getCoinCosts() {
        return new HashMap<>(coinCosts);
    }

    public String getCoinName(String unit) {
        return plugin.getConfigManager().getMainConfig().coins.names.get(unit);
    }

    public Map<String, String> getCoinNames() {
        return plugin.getConfigManager().getMainConfig().coins.names;
    }

}