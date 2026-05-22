package community.coins.plugin.command;

import community.coins.plugin.CoinsCore;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Eli
 * @since May 07, 2026
 */
public abstract class CommandLogic {
    protected final CoinsCore coins;
    protected final String identifier;

    public CommandLogic(CoinsCore coins, CommandService service, String identifier) {
        this.coins = coins;
        this.identifier = identifier.toLowerCase();
        service.registerLogic(this);
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract void register(List<String> labels, String permission);

    public abstract String getDescription();

    /// @param remaining should already be lowercase
    public static void suggestStartsWith(Collection<? extends String> items, String remaining, Consumer<String> item) {
        for (String name : items) {
            if (name.toLowerCase().startsWith(remaining)) {
                item.accept(name);
            }
        }
    }
}
