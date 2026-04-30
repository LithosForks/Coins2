package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class EntityTameRegistrar extends DropRegistrar<Player, Tameable> {
    public EntityTameRegistrar(CoinsCore coins) {
        super(coins);
    }

    @EventHandler(ignoreCancelled = true)
    void onEntityTameEvent(EntityTameEvent event) {
        if (!(event.getOwner() instanceof Player player)) {
            return;
        }

        if (!(event.getEntity() instanceof Tameable tameable)) {
            return;
        }

        performCoinEject(EventType.ENTITY_TAME, player, tameable.getLocation());
    }
}
