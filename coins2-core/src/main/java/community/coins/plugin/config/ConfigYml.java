package community.coins.plugin.config;

/**
 * @author Eli
 * @since April 27, 2026
 */
@ConfigFile("config.yml")
public final class ConfigYml {
    @ConfigEntry("locale")
    public static String LOCALE = "en-US";

    // todo move to coins.yml or drops.yml? different coins can be for different economies..
    @ConfigEntry("economy-type")
    public static EconomyType ECONOMY_TYPE = EconomyType.NONE;

    @ConfigEntry("notify-on-update")
    public static boolean NOTIFY_ON_UPDATE = true;
}
