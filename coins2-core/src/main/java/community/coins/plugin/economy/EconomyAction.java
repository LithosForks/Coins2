package community.coins.plugin.economy;

import java.util.UUID;

/**
 * @author Eli
 * @since May 05, 2026
 */
public interface EconomyAction {
    double getBalance(UUID uuid);
    boolean canAfford(UUID uuid, double amount);
    boolean deposit(UUID uuid, double amount);
    boolean withdraw(UUID uuid, double amount);
}
