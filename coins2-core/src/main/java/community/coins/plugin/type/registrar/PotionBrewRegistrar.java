package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class PotionBrewRegistrar extends DropRegistrar<BrewingStand, Block> {
    public PotionBrewRegistrar(CoinsCore coins) {
        super(coins);
    }

    // todo never tested before
    @EventHandler(ignoreCancelled = true)
    void onBrewEvent(BrewEvent event) {
        // todo get player from who brewed it
//        performCoinEject(EventType.POTION_BREW, player, event.getBlock());
    }
}
