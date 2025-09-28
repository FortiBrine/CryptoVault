package me.fortibrine.cryptovault.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.fortibrine.cryptovault.database.entity.WalletEntry;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public abstract class CryptoDatabase {

    private final Dao<WalletEntry, String> walletDao;

    public CryptoDatabase(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, WalletEntry.class);
        walletDao = DaoManager.createDao(connectionSource, WalletEntry.class);
    }

    public void deposit(UUID playerId, String coin, double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }

            String compositeId = playerId.toString() + "_" + coin;
            Optional<WalletEntry> existingEntry = Optional.ofNullable(walletDao.queryForId(compositeId));

            if (existingEntry.isPresent()) {
                WalletEntry entry = existingEntry.get();
                entry.setAmount(entry.getAmount() + amount);
                walletDao.update(entry);
            } else {
                WalletEntry newEntry = new WalletEntry(playerId, coin, amount);
                walletDao.create(newEntry);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to deposit funds", e);
        }
    }

    public boolean withdraw(UUID playerId, String coin, double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive");
            }

            String compositeId = playerId.toString() + "_" + coin;
            Optional<WalletEntry> existingEntry = Optional.ofNullable(walletDao.queryForId(compositeId));

            if (existingEntry.isPresent()) {
                WalletEntry entry = existingEntry.get();
                if (entry.getAmount() >= amount) {
                    entry.setAmount(entry.getAmount() - amount);
                    walletDao.update(entry);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to withdraw funds", e);
        }
    }

    public double getBalance(UUID playerId, String coin) {
        try {
            String compositeId = playerId.toString() + "_" + coin;
            Optional<WalletEntry> existingEntry = Optional.ofNullable(walletDao.queryForId(compositeId));
            return existingEntry.map(WalletEntry::getAmount).orElse(0.0);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get balance", e);
        }
    }

}