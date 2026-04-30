package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class ItemEnchantRegistrar extends DropRegistrar<Player, Block> {
    public ItemEnchantRegistrar(CoinsCore coins) {
        super(coins);
    }

    @EventHandler(ignoreCancelled = true)
    void onEnchantItemEvent(EnchantItemEvent event) {
        // todo do something with event.getExpLevelCost()

        // performFilterCheck()...?
        // applyFormula(int amount)?? with event.getExpLevelCost()

        performCoinEject(EventType.ITEM_ENCHANT, event.getEnchanter(), event.getEnchantBlock().getRelative(BlockFace.UP));
    }
}
