package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.LootGenerateEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class LootChestOpenRegistrar extends DropRegistrar<Player, Container> {
    public LootChestOpenRegistrar(CoinsCore coins) {
        super(coins);
    }

    // todo never tested before
    @EventHandler
    void onLootGenerateEvent(LootGenerateEvent event) {
        if (!(event.getInventoryHolder() instanceof Container)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        performAction(EventType.LOOT_CHEST_OPEN, coins -> coins.forEach(coin -> event.getLoot().add(coin)));
    }
}
