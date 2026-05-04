package community.coins.plugin.drops;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.config.ConfigService;
import community.coins.plugin.item.DefinedCoin;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.SplittableRandom;
import java.util.TreeMap;

/**
 * the 'coins:' section in the drops.yml config
 * @author Eli
 * @since May 04, 2026
 */
public final class DefinedCoinDrop {
    private final NavigableMap<Double, AmountedCoin> coinChances = new TreeMap<>();
    private static final SplittableRandom RANDOM = new SplittableRandom();

    private DefinedCoinDrop(Map<Double, AmountedCoin> chances) {
        double total = 0.;
        for (var entry : chances.entrySet()) {
            double chance = entry.getKey();
            if (chance <= 0) {
                continue;
            }

            total += chance;
            coinChances.put(total, entry.getValue());
        }
    }

    // coins:
    //   'coin_name':
    //     value: 1
    //     chance: 0.05
    //   'coins_two':
    //     value: [1, 5]
    //     chance: 0.40

    public static @Nullable DefinedCoinDrop of(CoinsCore coins, ConfigService service, ConfigurationSection coinsSection) {
        if (coinsSection == null) {
            return null;
        }

        Map<Double, AmountedCoin> coinChances = new HashMap<>();
        for (String coinName : coinsSection.getKeys(false)) {
            Optional<DefinedCoin> definedCoin = service.getCoinsConfig().getDefinedItem(coinName);
            if (definedCoin.isEmpty()) {
                coins.getConfigService().addWarning("No coin found with name " + coinName); // todo add drop name
                continue;
            }

            ConfigurationSection section = coinsSection.getConfigurationSection(coinName);
            if (section == null) {
                continue; // todo add warning
            }

            double chance = section.getDouble("chance", 1);
            double min = section.getDouble("value", -1);
            double max = section.getDouble("value", -1);

            if (min < 0 || max < 0) {
                List<Double> values = section.getDoubleList("value");
                if (values.size() == 2) {
                    min = values.get(0);
                    max = values.get(1);
                }
                else {
                    min = 1;
                    max = 1;
                }
            }

            if (min > max) {
                double minimum = min;
                min = max;
                max = minimum;
            }

            // todo warn when the total sum of chance exceeds 1.00

            DefinedCoin coin = definedCoin.get();
            coinChances.put(chance, new AmountedCoin(min, max, coin));
        }

        return new DefinedCoinDrop(coinChances);
    }

    /// pick a random coin from the list based on the configured chance
    /// @return empty when the chance didn't allow it
    public Optional<AmountedCoin> getRandomPick() {
        var coin = coinChances.ceilingEntry(RANDOM.nextDouble());
        if (coin == null) {
            return Optional.empty(); // leftover probability
        }

        return Optional.ofNullable(coin.getValue());
    }
}
