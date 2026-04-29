package community.coins.plugin.paper.impl;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.api.ItemParseApi;
import community.coins.plugin.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Eli
 * @since April 27, 2026
 */
@NullMarked
public final class ItemParseApiPaper extends ItemParseApi {
    public final BasicPlugin plugin;
    public ItemParseApiPaper(BasicPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<SkullMeta> applyMetaFromTexture(SkullMeta meta, @Nullable String texture, UUID uuid, String name) {
        Optional<String> url = getSkinUrl(texture);
        if (url.isEmpty()) {
            return Optional.empty();
        }

        var profile = plugin.getServer().createProfile(uuid, name);
        try {
            var textures = profile.getTextures();
            textures.setSkin(URI.create(url.get()).toURL());
            profile.setTextures(textures);
        }
        catch (MalformedURLException exception) {
            return Optional.empty();
        }

        meta.setPlayerProfile(profile);
        return Optional.of(meta);
    }

    @Override
    public Optional<ItemStack> getFromItemType(@Nullable String itemType) {
        return Util.getType(itemType, Registry.ITEM).map(item -> item.createItemStack(1));
    }

    @Override
    public void setDisplayName(ItemMeta meta, Component component, boolean immutable) {
        meta.itemName(component);
        // todo immutable
        // todo this doesn't work for player heads
    }

    @Override
    public void setLore(ItemMeta meta, List<Component> components) {
        meta.lore(components);
    }
}
