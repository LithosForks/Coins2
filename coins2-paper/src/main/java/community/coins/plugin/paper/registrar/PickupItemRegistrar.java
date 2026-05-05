package community.coins.plugin.paper.registrar;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.event.PlayerPickupEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

/**
 * @author Eli
 * @since May 05, 2026
 */
public final class PickupItemRegistrar implements Listener {
    private final BasicPlugin plugin;
    public PickupItemRegistrar(BasicPlugin plugin) {
        this.plugin = plugin;
        plugin.parseEventHandlers(this);
    }

    // on Spigot, Paper and Folia, PlayerAttemptPickupItemEvent always completes before
    //  EntityPickupItemEvent is triggered. so these events are never triggered at the
    //  same time; it is sync after each other. this has also been tested
    @EventHandler(ignoreCancelled = true) // todo test ignoreCancelled with full inventory
    void onPlayerAttemptPickupItemEvent(PlayerAttemptPickupItemEvent event) {
        PlayerPickupEvent registerEvent = new PlayerPickupEvent(event.getPlayer(), event.getItem());
        plugin.getServer().getPluginManager().callEvent(registerEvent);

        if (registerEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }
}
