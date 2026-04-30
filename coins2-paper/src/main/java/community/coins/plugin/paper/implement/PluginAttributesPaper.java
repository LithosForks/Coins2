package community.coins.plugin.paper.implement;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.api.PluginAttributes;
import org.jspecify.annotations.NullMarked;

/**
 * @author Eli
 * @since April 27, 2026
 */
@NullMarked
public final class PluginAttributesPaper implements PluginAttributes {
    public final BasicPlugin plugin;
    public PluginAttributesPaper(BasicPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public String getName() {
        return plugin.getPluginMeta().getName();
    }

    @Override
    public String getUrl() {
        var website = plugin.getPluginMeta().getWebsite();
        return website == null? "" : website;
    }

    @Override
    public String getDescription() {
        var description = plugin.getPluginMeta().getDescription();
        return description == null? "" : description;
    }
}
