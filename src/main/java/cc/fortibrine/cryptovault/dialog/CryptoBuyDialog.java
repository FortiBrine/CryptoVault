package cc.fortibrine.cryptovault.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class CryptoBuyDialog implements Listener {

    private final Player player;

    public CryptoBuyDialog(Player player) {
        this.player = player;

        CryptoVaultPlugin plugin = CryptoVaultPlugin.getPlugin(CryptoVaultPlugin.class);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Example dialog"))
                        .inputs(List.of(
                                DialogInput.singleOption("option", Component.text("Currency"), List.of(
                                                SingleOptionDialogInput.OptionEntry.create("btc", Component.text("BTC"), true),
                                                SingleOptionDialogInput.OptionEntry.create("ton", Component.text("TON"), false)
                                        ))
                                        .width(300)
                                        .build(),
                                DialogInput.numberRange("count", Component.text("Count"), 0f, 100f)
                                        .step(1f)
                                        .initial(0f)
                                        .width(300)
                                        .build()
                        ))
                        .build()
                )
                .type(DialogType.confirmation(
                        ActionButton.create(
                                Component.text("Confirm", TextColor.color(0xAEFFC1)),
                                Component.text("Click to confirm your input."),
                                100,
                                DialogAction.customClick((view, audience) -> {

                                    String option = view.getText("option");
                                    Float count = view.getFloat("count");

                                    audience.sendMessage(Component.text("Option: ")
                                            .append(Component.text(option)));
                                    audience.sendMessage(Component.text("Count: ")
                                            .append(Component.text(count)));

                                }, ClickCallback.Options.builder().build())
                        ),
                        ActionButton.create(
                                Component.text("Discard", TextColor.color(0xFFA0B1)),
                                Component.text("Click to discard your input."),
                                100,
                                null
                        )
                )));

        player.showDialog(dialog);

    }

}
