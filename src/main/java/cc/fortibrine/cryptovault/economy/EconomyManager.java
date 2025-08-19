package cc.fortibrine.cryptovault.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {
    private Economy economy = null;

    public boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getLogger().severe("Vault plugin not found! Economy features will not work.");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getLogger().severe("No economy provider found! Economy features will not work.");
            return false;
        }

        economy = rsp.getProvider();
        Bukkit.getLogger().info("Successfully hooked into Vault economy: " + economy.getName());
        return economy != null;
    }

    public boolean isEconomyAvailable() {
        return economy != null;
    }

    public double getBalance(OfflinePlayer player) {
        if (!isEconomyAvailable() || player == null) {
            return 0.0;
        }
        return economy.getBalance(player);
    }

    public boolean deposit(OfflinePlayer player, double amount) {
        if (!isEconomyAvailable() || player == null || amount <= 0) {
            return false;
        }
        return economy.depositPlayer(player, amount).transactionSuccess();
    }

    public boolean withdraw(OfflinePlayer player, double amount) {
        if (!isEconomyAvailable() || player == null || amount <= 0) {
            return false;
        }
        if (!hasEnough(player, amount)) {
            return false;
        }
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    public boolean hasEnough(OfflinePlayer player, double amount) {
        if (!isEconomyAvailable() || player == null) {
            return false;
        }
        return economy.has(player, amount);
    }

}