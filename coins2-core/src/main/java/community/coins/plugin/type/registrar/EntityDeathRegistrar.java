package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class EntityDeathRegistrar extends DropRegistrar<Player, Entity> {
    public EntityDeathRegistrar(CoinsCore coins) {
        super(coins);
    }

    // ENTITY_KILL_STAB
    // ENTITY_KILL_PROJECTILE
    // ENTITY_DEATH

    // maybe reduce to ENTITY_DEATH
    // with filters: kill, stab, projectile, player

    @EventHandler
    void onEntityDeathEvent(EntityDeathEvent event) {
        // todo filter type=projectile|stab
        // todo filter source=player|any
        // todo filter limit-for-location
        // todo filter percentage-player-hit
        // todo filter type=hostile|passive|player|any
        // todo filter player-alts
        // todo filter from-split
        // todo filter from-spawner

//      todo  performCoinEject();
    }
}
