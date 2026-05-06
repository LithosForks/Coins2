package community.coins.plugin.economy;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.event.PlayerPickupCoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eli
 * @since May 05, 2026
 */
public final class PickupHandler implements Listener {
    private final CoinsCore coins;
    public PickupHandler(CoinsCore coins) {
        this.coins = coins;
        coins.parseEventHandlers(this);
    }

    private final Map<UUID, Double> pickupAmountCache = new ConcurrentHashMap<>();
    private final Map<UUID, Long> pickupTimeCache = new ConcurrentHashMap<>();

    private static final long ACCUMULATE_MILLIS = 1500;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    void onPlayerPickupCoinEvent(PlayerPickupCoinEvent event) {
        Player player = event.getPlayer();
        ItemStack coin = event.getItem().getItemStack();

        OptionalDouble value = coins.getCoinService().getCoinMeta().getCoinValue(coin);
        Optional<String> currencyName = coins.getCoinService().getCoinMeta().getCoinCurrency(coin);
        if (value.isEmpty() || currencyName.isEmpty()) {
            event.setCancelled(true);
            return; // cannot pick up a coin that has no value
        }

        Optional<DefinedCurrency> currency = coins.getEconomyService().getCurrency(currencyName.get());
        if (currency.isEmpty()) {
            coins.getLogger().warning("""
                Attached currency to coin '%s' was not found. Consider to add it to 'currencies.yml' again."""
                .formatted(coins.getName())
            );
            return;
        }

        UUID uuid = player.getUniqueId();
        double amount = value.getAsDouble();

        if (pickupTimeCache.computeIfAbsent(uuid, _ -> 0L) > System.currentTimeMillis() - ACCUMULATE_MILLIS) {
            // recently shown actionbar
            double previousAmount = pickupAmountCache.computeIfAbsent(uuid, _ -> 0D);
            pickupAmountCache.put(uuid, amount + previousAmount);
        }
        else {
            pickupAmountCache.put(uuid, amount);
        }

        double displayAmount = pickupAmountCache.computeIfAbsent(uuid, _ -> 0D);
        pickupTimeCache.put(uuid, System.currentTimeMillis());

        coins.getEconomyService().submitTransaction(currency.get(), action -> {
            if (action.deposit(uuid, displayAmount)) {
                coins.getEconomyService().sendDepositMessage(currency.get(), player, displayAmount);
            }
        });
    }

    // clear cache of showing deposits
    @EventHandler
    void onPlayerQuitEvent(PlayerQuitEvent event) {
        var uuid = event.getPlayer().getUniqueId();
        pickupAmountCache.remove(uuid);
        pickupTimeCache.remove(uuid);
    }
}
