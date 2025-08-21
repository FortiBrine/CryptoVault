package cc.fortibrine.cryptovault.command.error;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.config.MessageManager;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

public class PermissionHandler implements MissingPermissionsHandler<CommandSender> {

    private final MessageManager messageManager = CryptoVaultPlugin.getInstance().getMessageManager();

    @Override
    public void handle(
            Invocation<CommandSender> invocation,
            MissingPermissions missingPermissions,
            ResultHandlerChain<CommandSender> chain
    ) {
        String permissions = missingPermissions.asJoinedText();

        messageManager.sendMessages("error.insufficient_permissions", invocation.sender(),
                Placeholder.unparsed("permissions", permissions));
    }

}
