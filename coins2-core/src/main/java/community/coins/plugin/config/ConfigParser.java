package community.coins.plugin.config;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.component.ComponentUtil;
import community.coins.plugin.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eli
 * @since April 27, 2026
 */
public final class ConfigParser {
    private final BasicPlugin plugin;
    private final ConfigService service;

    // todo allow custom @ConfigFile classes in ConfigFile and parse those as well
    public ConfigParser(BasicPlugin plugin, ConfigService service) {
        this.plugin = plugin;
        this.service = service;
    }

    private <T> String getName(Class<T> type) {
        return type.getAnnotation(ConfigFile.class).value();
    }

    public <T> void parse(Class<T> type) {
        String fileName = getName(type);
        var configFile = plugin.getDataFolder().toPath().resolve(fileName);

        if (!Files.exists(configFile)) {
            plugin.saveResource(fileName, false);
        }

        var config = YamlConfiguration.loadConfiguration(configFile.toFile());
        for (Field field : type.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigEntry.class) || !Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
            String configKey = configEntry.value();
            field.setAccessible(true);

            if (!config.contains(configKey) && configEntry.required()) {
                service.printWarning("Config '%s' is missing `%s`, using default.".formatted(fileName, configKey));
                continue;
            }

            try {
                // get current/default value, for the type
                var value = field.get(type);

                // handling different types
                var updatedValue = switch (value) {
                    case String _ -> config.getString(configKey);
                    case List<?> _ -> config.getStringList(configKey);
                    case Set<?> _ -> new HashSet<>(config.getStringList(configKey));
                    case Component _ -> ComponentUtil.parse(config.getString(configKey));
                    case ItemStack _ -> plugin.getItemParseApi().getFromItemType(config.getString(configKey)).orElse(null);
                    case Material _ -> Util.getType(config.getString(configKey), Registry.MATERIAL).orElse(null);
                    case EconomyType _ -> getEnum(EconomyType.class, config.getString(configKey));
                    case Long _ -> config.getLong(configKey);
                    case Integer _ -> config.getInt(configKey);
                    case Double _ -> config.getDouble(configKey);
                    default -> config.get(configKey);
                };

                if (updatedValue == null) {
                    service.printWarning("Config '%s' has invalid value for `%s`, using default.".formatted(fileName, configKey));
                    return;
                }

                // update the field of the config class to the config's value
                field.set(type, updatedValue);
            }
            catch (Throwable throwable) {
                service.printWarning("Config '%s' has invalid value for `%s`, using default.".formatted(fileName, configKey));
            }
        }
    }

    private static <T extends Enum<T>> T getEnum(@NotNull Class<T> type, @Nullable String value) {
        if (value == null) {
            return null;
        }

        try { return Enum.valueOf(type, value.toUpperCase().replace(" ", "_")); }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
