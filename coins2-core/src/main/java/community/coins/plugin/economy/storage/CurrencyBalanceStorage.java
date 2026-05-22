package community.coins.plugin.economy.storage;

import community.coins.plugin.CoinsCore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static community.coins.plugin.economy.storage.BalanceData.rounded;

/**
 * @author Eli
 * @since May 20, 2026
 */
public final class CurrencyBalanceStorage {
    private final CoinsCore coins;
    private final CurrencyStorage storage;

    private final Map<UUID, Double> balancesCache = new ConcurrentHashMap<>();
    private final Map<UUID, Double> offlineCoinsReceived = new ConcurrentHashMap<>();

    public CurrencyBalanceStorage(CoinsCore coins, CurrencyStorage storage) {
        this.coins = coins;
        this.storage = storage;

        storage.loadBalances(balances -> {
            for (BalanceData balance : balances) {
                balancesCache.put(balance.uuid(), balance.balance());
                offlineCoinsReceived.put(balance.uuid(), balance.receivedInactive());
            }
        });
    }

    public boolean hasExistingBalance(UUID uuid) {
        return balancesCache.containsKey(uuid);
    }

    public void createIfNotExists(UUID uuid) {
        if (balancesCache.containsKey(uuid)) {
            return;
        }

        balancesCache.put(uuid, 0D);
        storage.createAccount(uuid, 0D);
    }

    public double getCachedBalance(UUID uuid) {
        return balancesCache.getOrDefault(uuid, 0D);
    }

    public void withdraw(UUID uuid, double amount) {
        createIfNotExists(uuid);

        balancesCache.put(uuid, rounded(getCachedBalance(uuid) - amount));
        storage.withdraw(uuid, amount);

        var player = coins.getServer().getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            addOfflineCoins(uuid, -amount);
        }
    }

    public void deposit(UUID uuid, double amount) {
        createIfNotExists(uuid);

        balancesCache.put(uuid, rounded(getCachedBalance(uuid) + amount));
        storage.deposit(uuid, amount);

        var player = coins.getServer().getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            addOfflineCoins(uuid, amount);
        }
    }

    public double getAndResetOfflineCoins(UUID uuid) {
        double coins = getOfflineCoins(uuid);
        resetOfflineCoins(uuid);
        return coins;
    }

    public double getOfflineCoins(UUID uuid) {
        return offlineCoinsReceived.computeIfAbsent(uuid, _ -> 0D);
    }

    private void addOfflineCoins(UUID uuid, double amount) {
        offlineCoinsReceived.put(uuid, rounded(getOfflineCoins(uuid) + amount));
        storage.addInactiveCoins(uuid, amount);
    }

    public void resetOfflineCoins(UUID uuid) {
        offlineCoinsReceived.put(uuid, 0D);
        storage.resetInactiveCoins(uuid);
    }
}
