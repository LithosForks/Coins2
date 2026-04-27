package community.coins.plugin.util;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Eli
 * @since April 27, 2026
 */
public final class Util {
    public static <T extends Keyed> Optional<T> getType(@Nullable String type, Registry<T> registry) {
        if (type == null) {
            return Optional.empty();
        }

        var key = NamespacedKey.fromString(type);
        if (key == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(registry.get(key));
    }

    public static String toCapitalized(String message) {
        return (message == null || message.isEmpty())? "" : message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
    }
}
