package community.coins.plugin.spigot.test;

import community.coins.plugin.CoinsCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Eli
 * @since May 06, 2026
 */
public final class TestLogic implements Listener {
    private final CoinsCore coins;
    public TestLogic(CoinsCore coins) {
        this.coins = coins;
        coins.parseEventHandlers(this);
    }

    @EventHandler
    void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        var coin = coins.getConfigService().getCoinsConfig().getDefinedItem(event.getMessage());
        if (coin.isEmpty()) {
            return;
        }

        var player = event.getPlayer();
        player.getInventory().addItem(coin.get().getItemStackClone());
        player.sendMessage("Gave coin");
    }
}
