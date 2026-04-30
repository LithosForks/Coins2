package community.coins.plugin.type;

import community.coins.plugin.CoinsCore;

/**
 * @author Eli
 * @since April 29, 2026
 */
public abstract class Selector<T> {
    protected final CoinsCore coins;
    protected final T type;

    public Selector(CoinsCore coins, T type) {
        this.coins = coins;
        this.type = type;
    }
}
