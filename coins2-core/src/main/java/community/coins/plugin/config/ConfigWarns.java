package community.coins.plugin.config;

import community.coins.plugin.CoinsCore;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * @author Eli
 * @since May 05, 2026
 */
public final class ConfigWarns {
    private final CoinsCore coins;
    public ConfigWarns(CoinsCore coins) {
        this.coins = coins;
    }

    private final AtomicInteger warnings = new AtomicInteger(0);

    public int getWarnings() {
        return warnings.get();
    }

    public void clearWarnings() {
        warnings.set(0);
    }

    public Named create(String fileName) {
        return new Named(fileName);
    }

    public class Named {
        private final String fileName;
        public Named(String fileName) {
            this.fileName = fileName;
        }

        public void warn(String message) {
            int warning = warnings.incrementAndGet();
            coins.log(Level.WARNING, "[%s] #%,d: %s".formatted(fileName, warning, message));
        }
    }
}
