package cc.fortibrine.cryptovault.command.argument;

import cc.fortibrine.cryptovault.CryptoVaultPlugin;
import cc.fortibrine.cryptovault.coin.CoinManager;
import cc.fortibrine.cryptovault.util.MiniMessageDeserializer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

public class CoinArgument extends ArgumentResolver<CommandSender, String> {

    public static final String KEY = "coin-argument";

    private final CryptoVaultPlugin plugin = CryptoVaultPlugin.getInstance();
    private final CoinManager coinManager = plugin.getCoinManager();

    @Override
    protected ParseResult<String> parse(Invocation<CommandSender> invocation, Argument<String> context, String argument) {
        if (!coinManager.getCoinNames().containsKey(argument)) {
            return ParseResult.failure(MiniMessageDeserializer.deserializePlayer(
                    plugin.getConfigManager().getMessageConfig().error.invalidCoinType,
                    invocation.sender(),
                    Placeholder.unparsed("coin", argument)
            ));
        }

        return ParseResult.success(argument);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<String> argument, SuggestionContext context) {
        return SuggestionResult.of(coinManager.getCoinNames().keySet());
    }
}
