package community.coins.plugin.api;

import community.coins.plugin.config.MessagePosition;
import community.coins.plugin.language.FormatEntry;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.List;

/**
 * @author Eli
 * @since April 30, 2026
 */
@NullMarked
public interface ComponentApi {
    /// don't forget to apply ItemMeta to ItemStack afterward
    void setDisplayName(ItemMeta meta, Component component);

    /// don't forget to apply ItemMeta to ItemStack afterward
    void setLore(ItemMeta meta, List<Component> components);

    void setTeamColor(Team team, NamedTextColor color);

    /// set the display name of an item entity to its custom name
    void applyDisplayName(Item item);

    // send messages

    Audience getAudience(CommandSender sender);

    Title.Times TITLE_DURATION = Title.Times.times(
        Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500)
    );

    default void sendMessage(CommandSender sender, MessagePosition position, Component component) {
        Audience audience = getAudience(sender);
        switch (position) {
            case CHAT -> audience.sendMessage(component);
            case ACTIONBAR -> audience.sendActionBar(component);
            case TITLE -> audience.showTitle(Title.title(component, Component.empty(), TITLE_DURATION));
            case SUBTITLE -> audience.showTitle(Title.title(Component.empty(), component, TITLE_DURATION));
        }
    }

    default void sendMessage(CommandSender sender, Component component) {
        sendMessage(sender, MessagePosition.CHAT, component);
    }

    default void sendMessage(CommandSender sender, FormatEntry entry) {
        sendMessage(sender, entry.getComponent());
    }
}
