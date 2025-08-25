package cc.fortibrine.cryptovault.dialog;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
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

public class ShowPricesDialog implements PluginDialog {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();

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
                DialogBody.plainMessage(plugin.getMessageManager().getComponent("dialog.prices.header.component", player,
                        Placeholder.unparsed("balance", String.valueOf(balance))),

                        plugin.getMessageManager().getInt("dialog.prices.header.width"))
        ));

        plugin.getCoinManager().getCoinCosts().forEach((name, price) -> {
            dialogBodies.add(DialogBody.plainMessage(plugin.getMessageManager().getComponent("dialog.prices.content.component", player,
                    Placeholder.unparsed("price", String.valueOf(price)),
                    Placeholder.unparsed("unit", name)),

                    plugin.getMessageManager().getInt("dialog.prices.content.width")));
        });

        dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(plugin.getMessageManager().getComponent("dialog.prices.title", player))
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
