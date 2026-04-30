package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.MangrovePropagule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class BlockBreakRegistrar extends DropRegistrar<Player, Block> {
    public BlockBreakRegistrar(CoinsCore coins) {
        super(coins);
    }

    @EventHandler(ignoreCancelled = true)
    void onBlockBreakEvent(BlockBreakEvent event) {
        handleCropHarvest(event.getBlock(), event.getPlayer());
        handleBlockBreak(event);
    }

    private void handleCropHarvest(Block block, Player player) {
        if (!(block.getState().getBlockData() instanceof Ageable ageable)) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (block.getState().getBlockData() instanceof MangrovePropagule) {
            return;
        }

        if (ageable.getMaximumAge() != ageable.getAge() || ageable.getMaximumAge() < 3) {
            return;
        }

        performCoinEject(EventType.CROP_HARVEST, player, block);
    }

    private void handleBlockBreak(BlockBreakEvent event) {
        if (event.getExpToDrop() <= 0) {
            return; // todo selector for this
        }

        var player = event.getPlayer();
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        var block = event.getBlock();
        if (isSameDrop(block, player)) {
            return; // todo selector for this
        }

        performCoinEject(EventType.BLOCK_BREAK, player, block);
    }

    /// the block material that is mined is exactly the same as the item it drops
    private boolean isSameDrop(Block block, Player player) {
        var type = block.getType();
        var breakTool = player.getInventory().getItemInMainHand();

        for (ItemStack item : block.getDrops(breakTool)) {
            if (item.getType() == type) {
                return true;
            }
        }

        return false;
    }
}
