package cc.fortibrine.cryptovault.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class MessagesConfig {

    public UsageSection usage = new UsageSection();

    @ConfigSerializable
    public static class UsageSection {
        public List<String> all = List.of(
                "<green>Usage: /cryptovault <subcommand> [arguments]",
                "<gray>Subcommands:",
                "<gray>  buy <coin_type> <amount> - Buy cryptocurrency",
                "<gray>  sell <coin_type> <amount> - Sell cryptocurrency",
                "<gray>  reload - Reloads the plugin configuration"
        );
    }

    public SuccessSection success = new SuccessSection();

    @ConfigSerializable
    public static class SuccessSection {
        public List<String> buy = List.of(
                "<green>Success: You have successfully purchased cryptocurrency!",
                "<gray>Check your balance for details."
        );

        public List<String> sell = List.of(
                "<green>Success: You have successfully sold cryptocurrency!",
                "<gray>Check your balance for details."
        );

        public List<String> balance = List.of(
                "<green>Your balance:",
                "<gray>Crypto: <crypto_balance> <coin> | Currency: <currency_balance>"
        );

        public List<String> reload = List.of(
                "<green>Success: Plugin configuration has been reloaded!",
                "<gray>All changes are now applied."
        );
    }

    public ErrorSection error = new ErrorSection();

    @ConfigSerializable
    public static class ErrorSection {
        public List<String> insufficientCurrency = List.of(
                "<red>Error: You do not have enough currency to complete this purchase!",
                "<gray>Check your balance and try again."
        );

        public List<String> insufficientCrypto = List.of(
                "<red>Error: You do not have enough coins to sell!"
        );

        public List<String> invalidCoinType = List.of(
                "<red>Error: Invalid coin type!",
                "<gray>Available coins: bitcoin, ethereum, etc."
        );

        public List<String> insufficientPermissions = List.of(
                "<red>Error: You do not have sufficient permissions (<permissions>) to perform this action!",
                "<gray>Contact an administrator for assistance."
        );
    }

    public DialogSection dialog = new DialogSection();

    @ConfigSerializable
    public static class DialogSection {

        public DialogBalance balance = new DialogBalance();
        public DialogPrices prices = new DialogPrices();

        @ConfigSerializable
        public static class DialogBalance {
            public String title = "Balance";
            public Header header = new Header();
            public Content content = new Content();

            @ConfigSerializable
            public static class Header {
                public String component = "Your balance: <balance>";
                public int width = 512;
            }

            @ConfigSerializable
            public static class Content {
                public String component = "You have <balance_unit> (<count_unit> of <unit>)";
                public int width = 512;
            }
        }

        @ConfigSerializable
        public static class DialogPrices {
            public String title = "Prices";
            public Header header = new Header();
            public Content content = new Content();

            @ConfigSerializable
            public static class Header {
                public String component = "Your balance: <balance>";
                public int width = 512;
            }

            @ConfigSerializable
            public static class Content {
                public String component = "$<price> cost 1 <unit>";
                public int width = 512;
            }
        }

    }
}