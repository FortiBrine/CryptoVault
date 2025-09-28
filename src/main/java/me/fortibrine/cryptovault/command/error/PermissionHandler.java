package me.fortibrine.cryptovault.command.error;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import me.fortibrine.cryptovault.CryptoVaultPlugin;
import me.fortibrine.cryptovault.util.MiniMessageDeserializer;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

public class PermissionHandler implements MissingPermissionsHandler<CommandSender> {

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();

    @Override
    public void handle(
            Invocation<CommandSender> invocation,
            MissingPermissions missingPermissions,
            ResultHandlerChain<CommandSender> chain
    ) {
        String permissions = missingPermissions.asJoinedText();

        invocation.sender().sendMessage(MiniMessageDeserializer.deserializePlayer(
                plugin.getConfigManager().getMessageConfig().error.insufficientPermissions,
                invocation.sender(),
                Placeholder.unparsed("permissions", permissions)
        ));

    }

}
