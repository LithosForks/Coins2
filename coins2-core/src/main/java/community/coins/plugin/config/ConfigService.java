package community.coins.plugin.config;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.language.LanguageParser;

/**
 * @author Eli
 * @since April 27, 2026
 */
public final class ConfigService {
    private final BasicPlugin plugin;
    public ConfigService(BasicPlugin plugin) {
        this.plugin = plugin;

        var configParser = new ConfigParser(plugin, this);

        configParser.parse(ConfigYml.class);
        configParser.parse(CoinsYml.class);
        configParser.parse(DropsYml.class);

        var languageParser = new LanguageParser(plugin, this);
        languageParser.reloadLanguage();
    }

    // todo count warnings and print those
    public void printWarning(String message) {
        plugin.getLogger().warning(message);
    }

    public void reload() {

    }
}
