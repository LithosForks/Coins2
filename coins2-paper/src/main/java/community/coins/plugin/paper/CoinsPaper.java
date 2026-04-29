package community.coins.plugin.paper;

import com.mojang.brigadier.tree.LiteralCommandNode;
import community.coins.plugin.CoinsCore;
import community.coins.plugin.api.ItemParseApi;
import community.coins.plugin.api.PluginAttributes;
import community.coins.plugin.paper.commands.TestCommand;
import community.coins.plugin.paper.impl.ItemParseApiPaper;
import community.coins.plugin.paper.impl.PluginAttributesPaper;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Eli
 * @since April 27, 2026
 */
public final class CoinsPaper extends CoinsCore {
    private PluginAttributesPaper pluginAttributesPaper;
    private ItemParseApiPaper itemParseApiPaper;

    @Override
    public void beforeCoreLoaded() {
        this.pluginAttributesPaper = new PluginAttributesPaper(this);
        this.itemParseApiPaper = new ItemParseApiPaper(this);
    }

    @Override
    public void afterCoreLoaded() {
        new TestCommand(this);
        getLogger().info("Loaded CoinsPaper");
    }

    public void registerCommand(LiteralCommandNode<CommandSourceStack> node, String description, Collection<String> aliases) {
        getLifecycleManager().registerEventHandler(
            LifecycleEvents.COMMANDS,
            event -> event.registrar().register(node, description, aliases)
        );
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
