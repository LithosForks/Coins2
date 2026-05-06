package community.coins.plugin.handler;

import community.coins.plugin.CoinsCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * @author Eli
 * @since April 30, 2026
 */
public final class CoinBehaviorHandler implements Listener {
    private final CoinsCore coins;

    public CoinBehaviorHandler(CoinsCore coins) {
        this.coins = coins;
        coins.parseEventHandlers(this);
    }

    // apply characteristics of coins that are not present as ItemStack, but only as Item
    @EventHandler(ignoreCancelled = true)
    void onItemSpawnEvent(ItemSpawnEvent event) {
        var item = event.getEntity();
        coins.getCoinService().getCoinMeta().applyGlowIfPresent(item);
        coins.getCoinService().getCoinMeta().applyHologramIfPresent(item);
        coins.getCoinService().getCoinMeta().applyUniqueIfPresent(item);
    }

    // remove the uniqueness of the coin, so it can stack in the inventory again
    @EventHandler(ignoreCancelled = true)
    void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        coins.getCoinService().getCoinMeta().removeUniqueIfPresent(event.getItem());
    }

    // prevent coins from being picked up by hoppers if configured that way
    @EventHandler(ignoreCancelled = true)
    void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        if (event.getInventory().getType() != InventoryType.HOPPER) {
            return;
        }

        if (coins.getCoinService().getCoinMeta().isNoHopperPickup(event.getItem())) {
            event.setCancelled(true);
        }
    }
}
