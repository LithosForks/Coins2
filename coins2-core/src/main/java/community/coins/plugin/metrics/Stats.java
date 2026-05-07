package community.coins.plugin.metrics;

import community.coins.plugin.CoinsCore;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

/**
 * @author Eli
 * @since April 28, 2026
 */
public final class Stats {
    public Stats(CoinsCore coins) {
        Metrics metrics = new Metrics(coins, 31147);
        metrics.addCustomChart(new SimplePie("chart_id", () -> "My value"));
    }
}
