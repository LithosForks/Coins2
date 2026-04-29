package community.coins.plugin.spigot.impl;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.api.ItemParseApi;
import community.coins.plugin.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Eli
 * @since April 27, 2026
 */
@NullMarked
public final class ItemParseApiSpigot extends ItemParseApi {
    public final BasicPlugin plugin;
    public ItemParseApiSpigot(BasicPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<SkullMeta> applyMetaFromTexture(SkullMeta meta, @Nullable String texture, UUID uuid, String name) {
        Optional<String> url = getSkinUrl(texture);
        if (url.isEmpty()) {
            return Optional.empty();
        }

        var profile = plugin.getServer().createPlayerProfile(uuid, name);
        try {
            profile.getTextures().setSkin(URI.create(url.get()).toURL());
        }
        catch (MalformedURLException exception) {
            return Optional.empty();
        }

        meta.setOwnerProfile(profile);
        return Optional.of(meta);
    }

    @Override
    public Optional<ItemStack> getFromItemType(@Nullable String itemType) {
        return Util.getType(itemType, Registry.MATERIAL).map(ItemStack::new);
    }

    private static final LegacyComponentSerializer SERIALIZER =
        LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    @Override
    public void setDisplayName(ItemMeta meta, Component component, boolean immutable) {
        meta.setItemName(SERIALIZER.serialize(component));
    }

    @Override
    public void setLore(ItemMeta meta, List<Component> components) {
        List<String> lore = new ArrayList<>();
        for (Component component : components) {
            lore.add(SERIALIZER.serialize(component));
        }
        meta.setLore(lore);
    }
}
