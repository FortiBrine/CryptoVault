package cc.fortibrine.cryptovault.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BalanceFormatter {

    public static String format(double balance) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#.##", symbols);
        return format.format(balance);
    }

}
