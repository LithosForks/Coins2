package community.coins.plugin.item;

import community.coins.plugin.CoinsCore;

/**
 * @author Eli
 * @since April 28, 2026
 */
public final class CoinService {
    private final CoinsCore coins;
    private final CoinMeta coinMeta;

    public CoinService(CoinsCore coins) {
        this.coins = coins;
        this.coinMeta = new CoinMeta(coins);
    }

    public CoinMeta getCoinMeta() {
        return coinMeta;
    }
}
