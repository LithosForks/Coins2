package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.GameMode;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class AdvancementDoneRegistrar extends DropRegistrar<Player, Advancement> {
    public AdvancementDoneRegistrar(CoinsCore coins) {
        super(coins);
    }

    // todo maybe allow selectors for types of advancements
    @EventHandler(ignoreCancelled = true)
    void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        var player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        Advancement advancement = event.getAdvancement();
        if (advancement.getCriteria().contains("has_the_recipe")) {
            performCoinEject(EventType.RECIPE_UNLOCK, player, player.getLocation());
        }
        if (advancement.getDisplay() != null) {
            performCoinEject(EventType.ADVANCEMENT_DONE, player, player.getLocation());
        }
    }
}
