package community.coins.plugin.type.registrar;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.EventType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Eli
 * @since April 29, 2026
 */
public abstract class DropRegistrar<A, T> implements Listener {
    protected final CoinsCore coins;
    public DropRegistrar(CoinsCore coins) {
        this.coins = coins;
        coins.parseEventHandlers(this);
    }

    // todo this is probably the place where filters/selectors go??

    // dropEachCoin -> see DropFeature:49 (backend)

    // todo maybe instead it should take a Player, Location, and some sort of measurement (amount) of coins

    public void performAction(EventType type, Consumer<List<ItemStack>> coins) {
        // todo filters/selectors
    }

    public void performCoinEject(EventType type, Player player, Block block) {
        performCoinEject(type, player, block.getLocation().add(.5, .5, .5));
    }

    // either by dropping or putting directly into inventory
    public void performCoinEject(EventType type, Player player, Location location) {

        // todo put directly into inventory if configured

        performAction(type, coins -> {
            for (ItemStack coin : coins) {
                if (location.getWorld() == null) {
                    continue;
                }

                location.getWorld().dropItemNaturally(location, coin);
            }
        });
    }
}
