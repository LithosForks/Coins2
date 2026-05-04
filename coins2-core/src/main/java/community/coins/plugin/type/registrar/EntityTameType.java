package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventTypeService;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class EntityTameType extends EventType {
    public EntityTameType(CoinsCore coins, EventTypeService service) {
        var filter = service.filterBuilder()
            .hasInitiatorPlayer()
            .hasTargetEntity()
            .hasLocationWorld()
            .hasLocationCooldown();
        super(coins, service, "entity_tame", filter.build());
    }

    // https://github.com/justEli/Coins2/wiki/Defining-drop-filters#entity_tame

    @EventHandler(ignoreCancelled = true)
    void onEntityTameEvent(EntityTameEvent event) {
        if (!(event.getOwner() instanceof Player player)) {
            return;
        }

        if (!(event.getEntity() instanceof Tameable tameable)) {
            return;
        }

        var filter = createFilter()
            .withInitiatorEntity(player)
            .withTargetEntity(tameable)
            .withLocationWorld(tameable.getWorld())
            .withLocationCooldown(tameable.getLocation());

        callEvent(filter, tameable.getLocation());
    }
}
