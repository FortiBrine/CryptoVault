package me.fortibrine.cryptovault.command.error;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invalidusage.InvalidUsageHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import me.fortibrine.cryptovault.CryptoVaultPlugin;
import me.fortibrine.cryptovault.util.MiniMessageDeserializer;
import org.bukkit.command.CommandSender;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler<CommandSender> {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();

    @Override
    public void handle(Invocation<CommandSender> invocation, InvalidUsage<CommandSender> result, ResultHandlerChain<CommandSender> chain) {
        invocation.sender().sendMessage(MiniMessageDeserializer.deserializePlayer(
                plugin.getConfigManager().getMessageConfig().usage.all,
                invocation.sender()
        ));
    }
}
