package community.coins.plugin.type.filter;

import community.coins.plugin.CoinsCore;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * this contract has all paths that are allowed in the config for defining a drop
 * @author Eli
 * @since May 02, 2026
 */
public final class FilterContract {
    private final CoinsCore coins;
    private final Set<String> configPaths;

    public FilterContract(CoinsCore coins, Set<String> configPaths) {
        this.coins = coins;
        this.configPaths = configPaths;
    }

    // get a FilterConfig based on the contract (of the event)
    public @NotNull EventFilterConfig getFilterConfig(@Nullable ConfigurationSection config, String eventType) {
        if (config == null) {
            return new EventFilterConfig(); // no filters
        }

        var filter = new EventFilterConfig();
        if (contains("initiator.permission", config)) {
            filter.setInitiatorPermission(config.getString("initiator.permission"));
        }
        if (contains("initiator.type", config)) {
            List<String> values = config.getStringList("initiator.type");
            filter.setInitiatorType(toNamespacedKeys(values, eventType));
        }
        if (contains("initiator.any", config)) {
            filter.setInitiatorAny(config.getBoolean("initiator.any"));
        }
        if (contains("target.type", config)) {
            List<String> values = config.getStringList("target.type");
            filter.setTargetType(toNamespacedKeys(values, eventType));
        }
        if (contains("target.category", config)) {
            List<String> values = config.getStringList("target.category");
            filter.setTargetCategory(new HashSet<>(values));
        }
        if (contains("target.min-xp-drop", config)) {
            filter.setTargetMinXpDrop(config.getInt("target.min-xp-drop"));
        }
        if (contains("target.allow-same-block", config)) {
            filter.setTargetAllowSameBlock(config.getBoolean("target.allow-same-block"));
        }
        if (contains("target.prevent-alts", config)) {
            filter.setTargetPreventAlts(config.getBoolean("target.prevent-alts"));
        }
        if (contains("target.min-player-damage", config)) {
            filter.setTargetMinPlayerDamage(config.getDouble("target.min-player-damage"));
        }
        if (contains("location.disabled-worlds", config)) {
            List<String> values = config.getStringList("location.disabled-worlds");
            filter.setLocationDisabledWorlds(new HashSet<>(values));
        }
        if (contains("location.cooldown.cap-amount", config) && contains(config.getString("location.cooldown.duration"), config)) {
            filter.setLocationCooldownCapAmount(config.getInt("location.cooldown.cap-amount"));
            filter.setLocationCooldownDuration(config.getString("location.cooldown.duration"));
        }

        return filter;
    }

    private boolean contains(String path, ConfigurationSection section) {
        boolean inConfig = section.contains(path);
        if (inConfig && !configPaths.contains(path)) { // todo improve warning adding eventIdentifier
            coins.getConfigService().addWarning("Found '%s' in config where it is not supported.");
            return false;
        }
        return inConfig;
    }

    private Set<NamespacedKey> toNamespacedKeys(List<String> values, String eventType) {
        Set<NamespacedKey> keys = new HashSet<>();
        for (String value : values) {
            var name = NamespacedKey.fromString(value);
            if (name == null) { // todo improve warning adding eventIdentifier
                coins.getConfigService().addWarning("Invalid type found for event type '%s'.".formatted(eventType));
                continue;
            }
            keys.add(name);
        }
        return keys;
    }
}
