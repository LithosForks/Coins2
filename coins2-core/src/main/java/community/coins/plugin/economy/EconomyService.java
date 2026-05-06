package community.coins.plugin.economy;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.component.ComponentUtil;
import community.coins.plugin.config.ConfigWarns;
import community.coins.plugin.economy.hook.VaultEconomyHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * @author Eli
 * @since May 05, 2026
 */
public final class EconomyService {
    private final CoinsCore coins;
    public EconomyService(CoinsCore coins) {
        this.coins = coins;

        // economy: 'Vault'
        hookIfInstalled(
            VaultEconomyHook.NAME,
            () -> Optional.ofNullable(coins.getServer().getServicesManager().getRegistration(Economy.class))
                .map(registration -> new VaultEconomyHook(coins, this, registration.getProvider()))
        );

        // todo add a 'physical' economy/currency

        // add more economy hooks here
    }

    // registering economies/plugins

    // String = plugin's name (case-sensitive)
    private final Map<String, EconomyHook> economyHooks = new HashMap<>();

    // called from EconomyHook to register itself
    public void registerEconomy(EconomyHook economy) {
        economyHooks.put(economy.getName(), economy);
    }

    /// @param pluginName case-sensitive plugin name of the economy
    public Optional<EconomyHook> getEconomy(String pluginName) {
        return Optional.ofNullable(economyHooks.get(pluginName));
    }

    /// @param pluginName case-sensitive plugin name
    private void hookIfInstalled(String pluginName, Supplier<Optional<EconomyHook>> hook) {
        if (!coins.getServer().getPluginManager().isPluginEnabled(pluginName)) {
            return;
        }

        EconomyHook economy;
        try { economy = hook.get().orElse(null); }
        catch (NullPointerException | NoClassDefFoundError ignored) {
            economy = null;
        }

        if (economy == null) {
            coins.log(Level.SEVERE, "Found '%s', but it is missing an economy providing plugin.".formatted(pluginName));
            return;
        }

        coins.log(Level.INFO, "Hooked into '%s' as an economy provider.".formatted(pluginName));
    }

    // registering currencies

    public void clearAllCurrencies() {
        currencyToEconomyNames.clear();
        economyHooks.values().forEach(EconomyHook::clearCurrencies);
    }

    // <currency identifier, economy plugin name>
    private final Map<String, String> currencyToEconomyNames = new HashMap<>();

    public void registerCurrency(DefinedCurrency currency, ConfigWarns.Named warns) {
        EconomyHook economy = currency.getEconomyHook();
        if (!economy.isMultiCurrency() && economy.getAmountOfCurrencies() > 0) {
            warns.warn("Found multiple currencies for a plugin that only supports one currency.");
            return;
        }

        economy.addCurrency(currency);
        currencyToEconomyNames.put(currency.getIdentifier(), economy.getName());
    }

    public Optional<DefinedCurrency> getCurrency(String currency) {
        String economyName = currencyToEconomyNames.get(currency);
        if (economyName == null) {
            return Optional.empty();
        }

        return getEconomy(economyName).flatMap(economy -> economy.getCurrency(economyName));
    }

    public void submitTransaction(DefinedCurrency currency, Consumer<EconomyAction> action) {
        action.accept(currency.getEconomyHook());
    }

    public void sendDepositMessage(DefinedCurrency currency, Player player, double amount) {
        var component = ComponentUtil.replaceAmount(currency.getDepositMessage(), currency.formatAmount(amount));
        coins.getComponentApi().sendMessage(player, currency.getDepositPosition(), component);
    }
}
