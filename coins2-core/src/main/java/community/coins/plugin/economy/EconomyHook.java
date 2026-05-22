package community.coins.plugin.economy;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.config.ConfigWarns;
import community.coins.plugin.economy.storage.CurrencyBalanceStorage;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

/**
 * @author Eli
 * @since May 05, 2026
 */
@NullMarked
public abstract class EconomyHook {
    protected final CoinsCore coins;
    protected final EconomyService service;
    protected final String name;

    public EconomyHook(CoinsCore coins, EconomyService service, String name) {
        this.coins = coins;
        this.service = service;
        this.name = name; // not an identifier, but a case-sensitive (plugin) name
    }

    public String getPluginName() {
        return name;
    }

    protected boolean isPluginEnabled() {
        return coins.getServer().getPluginManager().isPluginEnabled(name);
    }

    /// if it has support for more than one currency
    public abstract boolean isMultiCurrencySupported();

    /// if it has support for being integrated as economy provider
    public abstract boolean isIntegrationSupported();

    /// @return successfully registered
    public abstract boolean registerIntegration(ConfigWarns.Named warns, DefinedCurrency currency, CurrencyBalanceStorage storage);

    /// @return successfully registered
    public abstract boolean registerCurrency(ConfigWarns.Named warns, DefinedCurrency currency);

    public abstract void unregister();

    public abstract double getBalance(UUID uuid, String currency);

    public abstract boolean canAfford(UUID uuid, String currency, double amount);

    public abstract boolean deposit(UUID uuid, String currency, double amount);

    public abstract boolean withdraw(UUID uuid, String currency, double amount);
}
