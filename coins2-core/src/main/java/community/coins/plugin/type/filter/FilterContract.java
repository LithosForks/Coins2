package community.coins.plugin.type.filter;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.registrar.EventType;
import community.coins.plugin.util.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

/**
 * this contract has all paths that are allowed in the config for defining a drop
 * @author Eli
 * @since May 02, 2026
 */
public final class FilterContract {
    private final CoinsCore coins;
    private final EventType eventType;
    private final Set<String> configPaths;

    public FilterContract(CoinsCore coins, EventType eventType, Set<String> configPaths) {
        this.coins = coins;
        this.eventType = eventType;
        this.configPaths = configPaths;
    }

    /// get a FilterConfig based on the contract (of the event)
    public @NotNull EventFilterConfig createFilterConfig(ConfigurationSection conf, ConfigurationSection def) {
        if (conf == null) {
            conf = def;
        }
        if (conf == null) {
            return new EventFilterConfig(); // no filters
        }

        var filter = new EventFilterConfig();
        if (contains("initiator.permission", conf, def)) {
            filter.setInitiatorPermission(conf.getString("initiator.permission", def.getString("initiator.permission")));
        }
        if (contains("initiator.type", conf, def)) {
            List<String> values = conf.getStringList("initiator.type");
            if (values.isEmpty()) {
                values.addAll(def.getStringList("initiator.type"));
            }
            filter.setInitiatorType(toNamespacedKeys(values, eventType.getIdentifier()));
        }
        if (contains("initiator.any", conf, def)) {
            filter.setInitiatorAny(conf.getBoolean("initiator.any", def.getBoolean("initiator.any")));
        }
        if (contains("target.type", conf, def)) {
            List<String> values = conf.getStringList("target.type");
            if (values.isEmpty()) {
                values.addAll(def.getStringList("target.type"));
            }
            filter.setTargetType(toNamespacedKeys(values, eventType.getIdentifier()));
        }
        if (contains("target.category", conf, def)) {
            List<String> values = conf.getStringList("target.category");
            if (values.isEmpty()) {
                values.addAll(def.getStringList("target.category"));
            }
            filter.setTargetCategory(new HashSet<>(values));
        }
        if (contains("target.min-xp-drop", conf, def)) {
            filter.setTargetMinXpDrop(conf.getInt("target.min-xp-drop", def.getInt("target.min-xp-drop")));
        }
        if (contains("target.allow-same-block", conf, def)) {
            filter.setTargetAllowSameBlock(conf.getBoolean("target.allow-same-block", def.getBoolean("target.allow-same-block")));
        }
        if (contains("target.prevent-alts", conf, def)) {
            filter.setTargetPreventAlts(conf.getBoolean("target.prevent-alts", def.getBoolean("target.prevent-alts")));
        }
        if (contains("target.min-player-damage", conf, def)) {
            filter.setTargetMinPlayerDamage(conf.getDouble("target.min-player-damage", def.getDouble("target.min-player-damage")));
        }
        if (contains("location.disabled-worlds", conf, def)) {
            List<String> values = conf.getStringList("location.disabled-worlds");
            if (values.isEmpty()) {
                values.addAll(def.getStringList("location.disabled-worlds"));
            }
            filter.setLocationDisabledWorlds(new HashSet<>(values));
        }
        if (contains("location.cooldown.cap-amount", conf, def) && contains("location.cooldown.duration", conf, def)) {
            OptionalInt durationMillis = Util.toDurationMillis(conf.getString("location.cooldown.duration", def.getString("location.cooldown.duration")));
            if (durationMillis.isPresent()) {
                filter.setLocationCooldownCapAmount(conf.getInt("location.cooldown.cap-amount", def.getInt("location.cooldown.cap-amount")));
                filter.setLocationCooldownDurationMillis(durationMillis.getAsInt());
            }
            else {
                coins.getConfigService().addWarning("Found an invalid cooldown duration.");
                // todo improve warning
            }
        }

        return filter;
    }

    private boolean contains(String path, ConfigurationSection conf, ConfigurationSection def) {
        if (!configPaths.contains(path)) { // if config path is not supported
            if (conf.contains(path)) {
                // todo improve warning adding eventIdentifier
                coins.getConfigService().addWarning("Found '%s' in config where it is not supported.");
            }
            return false;
        }

        return conf.contains(path) || def.contains(path);
    }

    private Set<NamespacedKey> toNamespacedKeys(List<String> values, String eventType) {
        Set<NamespacedKey> keys = new HashSet<>();
        for (String value : values) {
            var name = NamespacedKey.fromString(value);
            if (name == null) {
                // todo improve warning adding eventIdentifier
                coins.getConfigService().addWarning("Invalid type found for event type '%s'.".formatted(eventType));
                continue;
            }
            keys.add(name);
        }
        return keys;
    }
}
