package community.coins.plugin.economy.hook;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.config.ConfigWarns;
import community.coins.plugin.economy.DefinedCurrency;
import community.coins.plugin.economy.EconomyHook;
import community.coins.plugin.economy.EconomyService;
import community.coins.plugin.economy.storage.CurrencyBalanceStorage;
import community.coins.plugin.misc.MetricsHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.logging.Level;

/**
 * @author Eli
 * @since May 05, 2026
 */
@NullMarked
public final class VaultEconomyHook extends EconomyHook {
    private @Nullable VaultEconomyProvider provider;
    private @Nullable Economy economy;

    public VaultEconomyHook(CoinsCore coins, EconomyService service) {
        super(coins, service, "Vault");
    }

    @Override
    public boolean registerIntegration(ConfigWarns.Named warns, DefinedCurrency currency, CurrencyBalanceStorage storage) {
        if (!isPluginEnabled()) {
            return false;
        }

        if (provider != null) {
            return true;
        }

        // register as Vault economy service manager
        // todo should this be moved to onLoad? Essentials does that
        try {
            this.provider = new VaultEconomyProvider(coins, storage, currency);
            coins.getServer().getServicesManager().register(Economy.class, provider, coins, ServicePriority.Highest);
            return true;
        }
        catch (NullPointerException | NoClassDefFoundError _) {}
        return false;
    }

    @Override
    public boolean registerCurrency(ConfigWarns.Named warns, DefinedCurrency currency) {
        if (!isPluginEnabled()) {
            warns.warn("Cannot register economy '%s' because the plugin is not installed.".formatted(name));
            return false;
        }

        MetricsHandler.USING_ECONOMY_VAULT = true;
        if (economy != null) {
            return true;
        }

        try {
            RegisteredServiceProvider<Economy> registration =
                coins.getServer().getServicesManager().getRegistration(Economy.class);

            if (registration != null) {
                this.economy = registration.getProvider();
                String pluginName = registration.getPlugin().getName();
                String type = pluginName.equals(coins.getName())
                    ? "currency '%s'".formatted(currency.getIdentifier())
                    : "plugin '%s'".formatted(pluginName);

                coins.log(Level.INFO, "Using economy provider '%s', integrated by %s.".formatted(name, type));
                return true;
            }
        }
        catch (NullPointerException | NoClassDefFoundError _) {}
        warns.warn("""
            Found '%s', but there is no plugin installed that integrates this economy. There are two options to solve this: \
            Install a plugin that integrates and manages %s, or set `integration.enabled` to true for currency '%s'."""
            .formatted(name, name, currency.getIdentifier()));
        return false;
    }

    @Override
    public void unregister() {
        if (provider == null) {
            return;
        }

        coins.getServer().getServicesManager().unregister(Economy.class, provider);
    }

    @Override
    public double getBalance(UUID uuid, String currency) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (economy == null || player.getName() == null) {
            return 0;
        }

        return economy.getBalance(player);
    }

    @Override
    public boolean canAfford(UUID uuid, String currency, double amount) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (economy == null || player.getName() == null) {
            return false;
        }

        return economy.has(player, amount);
    }

    @Override
    public boolean deposit(UUID uuid, String currency, double amount) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (economy == null || player.getName() == null) {
            return false;
        }

        return economy.depositPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean withdraw(UUID uuid, String currency, double amount) {
        OfflinePlayer player = coins.getServer().getOfflinePlayer(uuid);
        if (economy == null || player.getName() == null) {
            return false;
        }

        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean isMultiCurrencySupported() {
        return false;
    }

    @Override
    public boolean isIntegrationSupported() {
        return true;
    }
}
