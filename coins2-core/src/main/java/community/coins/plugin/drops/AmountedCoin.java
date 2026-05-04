package community.coins.plugin.drops;

import community.coins.plugin.item.DefinedCoin;

/**
 * @author Eli
 * @since May 04, 2026
 */
public final class AmountedCoin {
    private final double minValue;
    private final double maxValue;
    private final DefinedCoin coin;

    public AmountedCoin(double minValue, double maxValue, DefinedCoin coin) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.coin = coin;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public DefinedCoin getCoin() {
        return coin;
    }
}
