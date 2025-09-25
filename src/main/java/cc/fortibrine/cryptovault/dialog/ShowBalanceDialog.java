package cc.fortibrine.cryptovault.dialog;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.config.ConfigManager;
import cc.fortibrine.cryptovault.util.BalanceFormatter;
import cc.fortibrine.cryptovault.util.MiniMessageDeserializer;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowBalanceDialog implements PluginDialog {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final ConfigManager configManager = plugin.getConfigManager();

    private final Player player;
    private final Dialog dialog;

    public ShowBalanceDialog(Player player) {
        this.player = player;

        double balance = plugin.getEconomyManager().getBalance(player);

        List<DialogBody> dialogBodies = new ArrayList<>();

        dialogBodies.addAll(List.of(
                DialogBody.item(new ItemStack(Material.ENCHANTED_BOOK))
                        .showTooltip(false)
                        .build(),
                DialogBody.plainMessage(MiniMessageDeserializer.deserializePlayer(
                        configManager.getMessageConfig().dialog.balance.header.component,
                        player,
                        Placeholder.unparsed("balance", BalanceFormatter.format(balance))
                ), configManager.getMessageConfig().dialog.balance.header.width)
        ));

        Map<String, String> coinNames = plugin.getCoinManager().getCoinNames();
        coinNames.forEach((coin, coinName) -> {
            double coinAmount = plugin.getCryptoDatabase().getBalance(player.getUniqueId(), coin);
            double coinPrice = plugin.getCoinManager().getCoinPrice(coin);

            double balanceCoin = coinAmount * coinPrice;
            dialogBodies.add(DialogBody.plainMessage(MiniMessageDeserializer.deserializePlayer(
                    configManager.getMessageConfig().dialog.balance.content.component,
                    player,
                    Placeholder.unparsed("balance_unit", coinName),
                    Placeholder.unparsed("count_unit", BalanceFormatter.format(coinPrice)),
                    Placeholder.unparsed("balance_unit", BalanceFormatter.format(balanceCoin))
            ), configManager.getMessageConfig().dialog.balance.content.width));

        });

        dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(MiniMessageDeserializer.deserializePlayer(configManager.getMessageConfig().dialog.balance.title, player))
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
