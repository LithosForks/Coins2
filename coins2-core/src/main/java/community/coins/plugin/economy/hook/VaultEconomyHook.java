package community.coins.plugin.economy.hook;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.economy.EconomyHook;
import community.coins.plugin.economy.EconomyService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * @author Eli
 * @since May 05, 2026
 */
public final class VaultEconomyHook extends EconomyHook {
    public static final String NAME = "Vault";

    private final CoinsCore coins;
    private final Economy economy;

    public VaultEconomyHook(CoinsCore coins, EconomyService service, Economy economy) {
        super(service, NAME);
        this.coins = coins;
        this.economy = economy;
    }

    @Override
    public double getBalance(UUID uuid) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (player.getName() == null) {
            return 0;
        }

        return economy.getBalance(player);
    }

    @Override
    public boolean canAfford(UUID uuid, double amount) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (player.getName() == null) {
            return false;
        }

        return economy.has(player, amount);
    }

    @Override
    public boolean deposit(UUID uuid, double amount) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (player.getName() == null) {
            return false;
        }

        return economy.depositPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean withdraw(UUID uuid, double amount) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (player.getName() == null) {
            return false;
        }

        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean isMultiCurrency() {
        return false;
    }
}
