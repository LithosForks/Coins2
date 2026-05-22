package community.coins.plugin.economy.storage;

import community.coins.plugin.util.Util;

import java.util.UUID;

/**
 * @author Eli
 * @since May 20, 2026
 */
public record BalanceData(UUID uuid, double balance, double receivedInactive) {
    public static final int MAX_DECIMALS = 6;

    public static double rounded(double value) {
        // round balances to avoid floating points, SQL also stores up to 6 decimals
        return Util.toRoundedMoneyDecimals(value, MAX_DECIMALS);
    }
}
