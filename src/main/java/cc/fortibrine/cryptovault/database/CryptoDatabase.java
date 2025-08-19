package cc.fortibrine.cryptovault.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import cc.fortibrine.cryptovault.database.entity.WalletEntry;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public abstract class CryptoDatabase {

    private final Dao<WalletEntry, ?> walletDao;

    public CryptoDatabase(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, WalletEntry.class);
        walletDao = DaoManager.createDao(connectionSource, WalletEntry.class);
    }

    public void deposit(UUID uniqueId, String coin, double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }

            Optional<WalletEntry> existingEntry = Optional.ofNullable(walletDao.queryForFirst(
                    walletDao.queryBuilder()
                            .where()
                            .eq("id", uniqueId)
                            .and()
                            .eq("coin", coin)
                            .prepare()
            ));

            if (existingEntry.isPresent()) {
                WalletEntry entry = existingEntry.get();
                entry.setAmount(entry.getAmount() + amount);
                walletDao.update(entry);
            } else {
                WalletEntry newEntry = new WalletEntry();
                newEntry.setId(uniqueId);
                newEntry.setCoin(coin);
                newEntry.setAmount(amount);
                walletDao.create(newEntry);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to deposit funds", e);
        }
    }

    public boolean withdraw(UUID uniqueId, String coin, double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive");
            }

            Optional<WalletEntry> existingEntry = Optional.ofNullable(walletDao.queryForFirst(
                    walletDao.queryBuilder()
                            .where()
                            .eq("id", uniqueId)
                            .and()
                            .eq("coin", coin)
                            .prepare()
            ));

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

    public double getBalance(UUID uniqueId, String coin) {
        try {
            Optional<WalletEntry> existingEntry = Optional.ofNullable(walletDao.queryForFirst(
                    walletDao.queryBuilder()
                            .where()
                            .eq("id", uniqueId)
                            .and()
                            .eq("coin", coin)
                            .prepare()
            ));

            return existingEntry.map(WalletEntry::getAmount).orElse(0.0);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get balance", e);
        }
    }

}