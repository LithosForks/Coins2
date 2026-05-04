package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventTypeService;
import org.bukkit.GameMode;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class AdvancementDoneType extends EventType {
    public AdvancementDoneType(CoinsCore coins, EventTypeService service) {
        var filter = service.filterBuilder()
            .hasInitiatorPlayer()
            .hasTargetType()
            .hasLocationWorld();
        super(coins, service, "advancement_done", filter.build());
    }

    // https://github.com/justEli/Coins2/wiki/Defining-drop-filters#advancement_done

    @EventHandler(ignoreCancelled = true)
    void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        // todo NoSuchMethodError
        Advancement advancement = event.getAdvancement();
        if (advancement.getDisplay() == null) {
            return;
        }

        var filter = createFilter()
            .withInitiatorEntity(player)
            .withTargetType(advancement)
            .withLocationWorld(player.getLocation().getWorld());

        callEvent(filter, player.getLocation());
    }
}
