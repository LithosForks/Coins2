package community.coins.plugin.paper;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.api.ItemParseApi;
import community.coins.plugin.api.PluginAttributes;
import community.coins.plugin.paper.impl.ItemParseApiPaper;
import community.coins.plugin.paper.impl.PluginAttributesPaper;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eli
 * @since April 27, 2026
 */
public final class CoinsPaper extends CoinsCore {
    private PluginAttributesPaper pluginAttributesPaper;
    private ItemParseApiPaper itemParseApiPaper;

    @Override
    public void onPluginLoad() {
        this.pluginAttributesPaper = new PluginAttributesPaper(this);
        this.itemParseApiPaper = new ItemParseApiPaper(this);
        getLogger().info("Loading CoinsPaper");
    }

    @Override
    public @NotNull PluginAttributes getAttributes() {
        return pluginAttributesPaper;
    }

    @Override
    public @NotNull ItemParseApi getItemParseApi() {
        return itemParseApiPaper;
    }
}
