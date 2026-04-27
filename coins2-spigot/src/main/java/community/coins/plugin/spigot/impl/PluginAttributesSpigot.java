package community.coins.plugin.spigot.impl;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.api.PluginAttributes;
import org.jspecify.annotations.NullMarked;

/**
 * @author Eli
 * @since April 27, 2026
 */
@NullMarked
public final class PluginAttributesSpigot implements PluginAttributes {
    public final BasicPlugin plugin;
    public PluginAttributesSpigot(BasicPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getUrl() {
        var website = plugin.getDescription().getWebsite();
        return website == null? "" : website;
    }

    @Override
    public String getDescription() {
        var description = plugin.getDescription().getDescription();
        return description == null? "" : description;
    }
}
