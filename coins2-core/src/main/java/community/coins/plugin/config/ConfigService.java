package community.coins.plugin.config;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.language.LanguageParser;
import org.bukkit.configuration.file.YamlConfiguration;

import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * @author Eli
 * @since April 27, 2026
 */
public final class ConfigService {
    private final BasicPlugin plugin;
    private final ConfigParser configParser;
    private final CoinsConfig coinsConfig;
    private final LanguageParser languageParser;

    public ConfigService(BasicPlugin plugin) {
        this.plugin = plugin;
        this.configParser = new ConfigParser(plugin, this);
        this.coinsConfig = new CoinsConfig(plugin, this);
        this.languageParser = new LanguageParser(plugin, this);

        reload();
    }

    // different configs

    public CoinsConfig getCoinsConfig() {
        return coinsConfig;
    }

    public void reload() {
        warnings.set(0);

        configParser.parseAndInject(ConfigYml.class);
        coinsConfig.parseAndReload();
        languageParser.reloadLanguage();

        if (warnings.get() == 0) {
            return;
        }

        plugin.log(Level.WARNING, """
            Loaded the config of Coins with %d warnings. See above here for details.""".formatted(warnings.get())
        );
    }

    // config util

    public YamlConfiguration getOrCreateConfig(String fileName) {
        var configFile = plugin.getDataFolder().toPath().resolve(fileName);

        if (!Files.exists(configFile)) {
            plugin.saveResource(fileName, false);
        }

        return YamlConfiguration.loadConfiguration(configFile.toFile());
    }

    // warnings

    private final AtomicInteger warnings = new AtomicInteger(0);

    public void addWarning(String message) {
        int warning = warnings.incrementAndGet();
        plugin.log(Level.WARNING, "#%,d: %s".formatted(warning, message));
    }

    public void printConfigWarning(String config, String message) {
        int warning = warnings.incrementAndGet();
        plugin.log(Level.WARNING, "[%s] #%,d: %s".formatted(config, warning, message));
    }
}
