package community.coins.plugin.economy.storage;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Eli
 * @since May 20, 2026
 */
public interface CurrencyStorage {
    void loadBalances(Consumer<Collection<BalanceData>> balances);

    void createAccount(UUID uuid, double amount);

    void withdraw(UUID uuid, double amount);

    void deposit(UUID uuid, double amount);

    void addInactiveCoins(UUID uuid, double amount);

    void resetInactiveCoins(UUID uuid);
}
