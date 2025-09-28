package me.fortibrine.cryptovault.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.fortibrine.cryptovault.CryptoVaultPlugin;
import me.fortibrine.cryptovault.config.ConfigManager;
import me.fortibrine.cryptovault.util.BalanceFormatter;
import me.fortibrine.cryptovault.util.MiniMessageDeserializer;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShowPricesDialog implements PluginDialog {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final ConfigManager configManager = plugin.getConfigManager();

    private final Player player;
    private final Dialog dialog;

    public ShowPricesDialog(Player player) {
        this.player = player;

        double balance = plugin.getEconomyManager().getBalance(player);

        List<DialogBody> dialogBodies = new ArrayList<>();

        dialogBodies.addAll(List.of(
                DialogBody.item(new ItemStack(Material.ENCHANTED_BOOK))
                        .showTooltip(false)
                        .build(),
                DialogBody.plainMessage(MiniMessageDeserializer.deserializePlayer(
                        configManager.getMessageConfig().dialog.prices.header.component,
                        player,
                        Placeholder.unparsed("balance", BalanceFormatter.format(balance))
                ), configManager.getMessageConfig().dialog.prices.header.width)
        ));

        plugin.getCoinManager().getCoinNames().forEach((coin, coinName) -> {
            double price = plugin.getCoinManager().getCoinPrice(coin);

            dialogBodies.add(DialogBody.plainMessage(MiniMessageDeserializer.deserializePlayer(
                    configManager.getMessageConfig().dialog.prices.content.component,
                    player,
                    Placeholder.unparsed("price", BalanceFormatter.format(price)),
                    Placeholder.unparsed("unit", coinName)
            ), configManager.getMessageConfig().dialog.prices.content.width));
        });

        dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(MiniMessageDeserializer.deserializePlayer(configManager.getMessageConfig().dialog.prices.title, player))
                        .body(dialogBodies)
                        .build()
                )
                .type(DialogType.notice()));

    }

    @Override
    public void show() {
        player.showDialog(dialog);
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
