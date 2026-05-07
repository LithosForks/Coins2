package community.coins.plugin.economy;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.event.PlayerPickupCoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    void onPlayerPickupCoinEvent(PlayerPickupCoinEvent event) {
        var player = event.getPlayer();
        if (coins.getEconomyService().depositCoin(player, event.getItem().getItemStack())) {
            return; // successfully deposited
        }

        // cannot pick up a coin that has no value
        event.setCancelled(true);
    }
}
