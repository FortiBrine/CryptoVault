package cc.fortibrine.cryptovault.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class MiniMessageDeserializer {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component deserialize(List<String> strings, TagResolver... resolvers) {
        if (strings == null || strings.isEmpty()) {
            return Component.empty();
        }

        String combined = String.join("\n", strings);
        return MINI_MESSAGE.deserialize(combined, resolvers);
    }

    public static Component deserializePlayer(String string, CommandSender player, TagResolver... resolvers) {
        return deserializePlayer(Collections.singletonList(string), player, resolvers);
    }

    public static Component deserializePlayer(List<String> strings, CommandSender player, TagResolver... resolvers) {
        if (strings == null || strings.isEmpty()) {
            return Component.empty();
        }

        String combined = String.join("\n", strings);
        return MINI_MESSAGE.deserialize(combined, TagResolver.builder()
                .resolver(Placeholder.unparsed("player", player.getName()))
                .resolvers(resolvers)
                .build());
    }

}
