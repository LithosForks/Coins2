package community.coins.plugin;

import community.coins.plugin.api.BasicPlugin;
import community.coins.plugin.config.CoinsYml;
import community.coins.plugin.config.ConfigParser;
import community.coins.plugin.config.ConfigService;
import community.coins.plugin.config.ConfigYml;
import community.coins.plugin.config.DropsYml;
import community.coins.plugin.folialib.FoliaScheduler;
import community.coins.plugin.util.VersionCheck;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eli
 * @since April 27, 2026
 */
public abstract class CoinsCore extends BasicPlugin {
    @Override
    public void onEnable() {
        this.foliaScheduler = new FoliaScheduler(this);

        new ConfigService(this);

        var versionCheck = new VersionCheck(this);
        VIRTUAL_EXECUTOR.submit(() -> versionCheck.findLatestVersion(ConfigYml.NOTIFY_ON_UPDATE));

        addMetrics();
    }

    private FoliaScheduler foliaScheduler;

    public FoliaScheduler getScheduler() {
        return foliaScheduler;
    }

    private void addMetrics() {
        Metrics metrics = new Metrics(this, 30976);
        metrics.addCustomChart(new SimplePie("chart_id", () -> "My value"));
        getLogger().info("Loading CoinsCore");

        onPluginLoad();
    }

    public abstract void onPluginLoad();
}
