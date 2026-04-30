package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class EntityBreedRegistrar extends DropRegistrar<Player, Entity> {
    public EntityBreedRegistrar(CoinsCore coins) {
        super(coins);
    }

    // todo does this get triggered twice for one breeding? (cus 2 mobs)
    @EventHandler(ignoreCancelled = true)
    void onEntityBreedEvent(EntityBreedEvent event) {
        if (!(event.getBreeder() instanceof Player player)) {
            return;
        }

        performCoinEject(EventType.ENTITY_BREED, player, event.getEntity().getLocation());
    }
}
