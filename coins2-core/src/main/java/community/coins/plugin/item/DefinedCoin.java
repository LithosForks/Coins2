package community.coins.plugin.item;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * @author Eli
 * @since April 28, 2026
 */
@NullMarked
public record DefinedCoin(String id, ItemStack itemStack, Component singularName, Component pluralName, boolean immutable) {
    public ItemStack getClonedCoin() {
        return itemStack.clone();
    }
}
