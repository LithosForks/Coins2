package community.coins.plugin.config;

/**
 * @author Eli
 * @since April 27, 2026
 */
public enum EconomyType {
    NONE("none"),
    VAULT("Vault"),
    PHYSICAL("physical");

    private final String name;
    EconomyType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
