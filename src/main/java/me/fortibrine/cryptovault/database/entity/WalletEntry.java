package me.fortibrine.cryptovault.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@DatabaseTable(tableName = "wallet")
@NoArgsConstructor
public class WalletEntry {
    @Setter
    @DatabaseField(id = true)
    private String compositeId;

    @DatabaseField(canBeNull = false, columnName = "id")
    private UUID playerId;

    @DatabaseField(canBeNull = false, columnName = "coin")
    private String coin;

    @Setter
    @DatabaseField(canBeNull = false, columnName = "amount")
    private double amount;

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
        this.compositeId = playerId.toString() + "_" + coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
        this.compositeId = playerId.toString() + "_" + coin;
    }

    public WalletEntry(UUID playerId, String coin, double amount) {
        this.playerId = playerId;
        this.coin = coin;
        this.amount = amount;
        this.compositeId = playerId.toString() + "_" + coin;
    }
}
