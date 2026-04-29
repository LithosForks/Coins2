package community.coins.plugin.spigot;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.api.ItemParseApi;
import community.coins.plugin.api.PluginAttributes;
import community.coins.plugin.spigot.impl.ItemParseApiSpigot;
import community.coins.plugin.spigot.impl.PluginAttributesSpigot;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eli
 * @since April 27, 2026
 */
public final class CoinsSpigot extends CoinsCore {
    private PluginAttributesSpigot pluginAttributesSpigot;
    private ItemParseApiSpigot itemParseApiSpigot;

    @Override
    public void beforeCoreLoaded() {
        this.pluginAttributesSpigot = new PluginAttributesSpigot(this);
        this.itemParseApiSpigot = new ItemParseApiSpigot(this);
    }

    @Override
    public void afterCoreLoaded() {
        getLogger().info("Loaded CoinsSpigot");
    }

    @Override
    public @NotNull PluginAttributes getAttributes() {
        return pluginAttributesSpigot;
    }

    @Override
    public @NotNull ItemParseApi getItemParseApi() {
        return itemParseApiSpigot;
    }
}
