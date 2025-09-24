package cc.fortibrine.cryptovault.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BalanceFormatter {

    public static String format(double balance) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,##0.########", symbols);
        format.setGroupingUsed(true);
        format.setGroupingSize(3);

        return format.format(balance);
    }

}
