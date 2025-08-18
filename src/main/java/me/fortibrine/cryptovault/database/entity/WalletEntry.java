package me.fortibrine.cryptovault.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = "wallet")
public class WalletEntry {
    @DatabaseField(canBeNull = false, columnName = "id")
    private UUID id;
    @DatabaseField(canBeNull = false, columnName = "coin")
    private String coin;
    @DatabaseField(canBeNull = false, columnName = "amount")
    private double amount;
}
