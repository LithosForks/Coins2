package community.coins.plugin.drops;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.config.DepositType;
import community.coins.plugin.type.filter.EventFilterConfig;
import community.coins.plugin.type.filter.EventFilterForm;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;

/**
 * @author Eli
 * @since May 01, 2026
 */
public final class DefinedDrop {
    private final String identifier;
    private final EventFilterConfig eventFilterConfig;
    private final DefinedCoinDrop definedCoinDrop;

    public DefinedDrop(String identifier, EventFilterConfig filterConfig, DefinedCoinDrop coinDrop) {
        this.identifier = identifier;
        this.eventFilterConfig = filterConfig;
        this.definedCoinDrop = coinDrop;
    }

    private static final SplittableRandom RANDOM = new SplittableRandom();

    public String getIdentifier() {
        return identifier;
    }

    public EventFilterConfig getEventFilterConfig() {
        return eventFilterConfig;
    }

    public DepositType getDepositType() {
        // todo implement
        return DepositType.DROP;
    }

    private static final int DECIMAL_POINTS = 2;

    // todo decimal-points in config
    public static double toRoundedMoneyDecimals(double value) {
        return BigDecimal.valueOf(value).setScale(DECIMAL_POINTS, RoundingMode.HALF_UP).doubleValue();
    }

    public Optional<CoinDropAction> generateCoinAction(CoinsCore coins, EventFilterForm form) {
        Optional<AmountedCoin> amountedCoin = definedCoinDrop.getRandomPick();
        if (amountedCoin.isEmpty()) {
            return Optional.empty(); // chance didn't allow it
        }

        double min = amountedCoin.get().getMinValue();
        double max = amountedCoin.get().getMaxValue();
        double value = toRoundedMoneyDecimals(min == max? min : RANDOM.nextDouble(min, max));

        // todo drop-each-coin can be programmed here (currently always only 1 in list)

        List<ItemStack> items = new ArrayList<>();

        // create coin
        ItemStack coin = amountedCoin.get().getCoin().getItemStackClone();
        ItemMeta meta = coin.getItemMeta();
        coins.getCoinService().getCoinMeta().setCoinValue(meta, value);
        coin.setItemMeta(meta);

        // add coin to drop action
        items.add(coin);

        return Optional.of(new CoinDropAction(this, form, items));
    }
}
