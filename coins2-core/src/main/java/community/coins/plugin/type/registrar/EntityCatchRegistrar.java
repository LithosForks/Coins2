package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class EntityCatchRegistrar extends DropRegistrar<Player, Item> {
    public EntityCatchRegistrar(CoinsCore coins) {
        super(coins);
    }

    @EventHandler(ignoreCancelled = true)
    void onPlayerFishEvent(PlayerFishEvent event) {
        if (event.getExpToDrop() <= 0) {
            return;
        }

        if (!(event.getCaught() instanceof Item item)) {
            return;
        }

        // todo test item.getlocation, otherwise player.getlocation
        performCoinEject(EventType.ENTITY_CATCH, event.getPlayer(), item.getLocation());
    }
}
