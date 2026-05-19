package community.coins.plugin.command;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.coin.DefinedCoin;
import community.coins.plugin.economy.DefinedCurrency;
import community.coins.plugin.language.EntryReplacement;
import community.coins.plugin.language.Language;
import community.coins.plugin.util.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.SplittableRandom;

/**
 * @author Eli
 * @since May 07, 2026
 */
public abstract class CoinsCommandLogic extends CommandLogic {
    public CoinsCommandLogic(CoinsCore coins, CommandService service) {
        super(coins, service, "coins");
    }

    @Override
    public String getDescription() {
        return "Command with tools for coins.";
    }

    private static final SplittableRandom RANDOM = new SplittableRandom();

    private static final EntryReplacement FILL_DURATION = new EntryReplacement("duration");
    private static final EntryReplacement FILL_ID = new EntryReplacement("identifier");
    private static final EntryReplacement FILL_FORMAT = new EntryReplacement("format");
    private static final EntryReplacement FILL_TYPE = new EntryReplacement("type");
    private static final EntryReplacement.Filled FILL_MIN_1 = new EntryReplacement("min").filled(1);
    private static final EntryReplacement FILL_MAX = new EntryReplacement("max");
    private static final EntryReplacement FILL_AMOUNT = new EntryReplacement("amount");
    private static final EntryReplacement FILL_RADIUS = new EntryReplacement("radius");
    private static final EntryReplacement FILL_TARGET = new EntryReplacement("target");

    public void reload(CommandSender sender) {
        long millis = System.currentTimeMillis();
        coins.getConfigService().reload();
        long duration = System.currentTimeMillis() - millis;

        coins.sendMessage(sender, Language.RELOAD_SUCCESS.with(FILL_DURATION.filled(duration)));
    }

    public void giveCoin(CommandSender sender, String coinIdentifier) {
        if (!(sender instanceof Player player)) {
            coins.sendMessage(sender,  Language.PLAYERS_ONLY);
            return;
        }

        Optional<DefinedCoin> coin = coins.getConfigService().getCoinsConfig().getDefinedItem(coinIdentifier);
        if (coin.isEmpty()) {
            coins.sendMessage(sender,  Language.COIN_NOT_FOUND.with(FILL_ID.filled(coinIdentifier)));
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            coins.sendMessage(sender, Language.FULL_INVENTORY);
            return;
        }

        player.getInventory().addItem(coin.get().getItemStackClone());
        coins.sendMessage(sender, Language.GIVE_SUCCESS.with(FILL_ID.filled(coinIdentifier)));
    }

    public void setCoinValue(CommandSender sender, String currencyName, double value) {
        if (!(sender instanceof Player player)) {
            coins.sendMessage(sender, Language.PLAYERS_ONLY);
            return;
        }

        ItemStack stack = player.getInventory().getItemInMainHand();
        if (coins.getCoinMeta().getCoinCurrency(stack).isEmpty()) {
            coins.sendMessage(sender, Language.HOLD_A_COIN);
            return;
        }

        Optional<DefinedCurrency> currency = coins.getEconomyService().getCurrency(currencyName);
        if (currency.isEmpty()) {
            coins.sendMessage(sender, Language.CURRENCY_NOT_FOUND.with(FILL_ID.filled(currencyName)));
            return;
        }

        ItemMeta meta = stack.getItemMeta();
        coins.getCoinMeta().setCoinCurrency(meta, currency.get());
        coins.getCoinMeta().setCoinValue(meta, Util.toRoundedMoneyDecimals(value, currency.get().getDecimals()));
        stack.setItemMeta(meta);

        coins.sendMessage(
            sender,
            Language.SET_VALUE_SUCCESS.with(FILL_FORMAT.filled(currency.get().getFormatMessage(value)))
        );
    }

    @NullMarked
    public void dropCoins(CommandSender sender, Location location, String coinIdentifier, int amount, double radius, double value) {
        if (location.getWorld() == null) {
            return;
        }

        Optional<DefinedCoin> coin = coins.getConfigService().getCoinsConfig().getDefinedItem(coinIdentifier);
        if (coin.isEmpty()) {
            coins.sendMessage(sender, Language.COIN_NOT_FOUND.with(FILL_ID.filled(coinIdentifier)));
            return;
        }

        if (amount <= 0 || amount > 1000) {
            coins.sendMessage(sender, Language.COMMAND_INVALID_RANGE.with(
                FILL_TYPE.filled(Language.WORD_AMOUNT), FILL_MIN_1, FILL_MAX.filled(1000)
            ));
            return;
        }

        if (radius < 1 || radius > 100) {
            coins.sendMessage(sender, Language.COMMAND_INVALID_RANGE.with(
                FILL_TYPE.filled(Language.WORD_RADIUS), FILL_MIN_1, FILL_MAX.filled(100)
            ));
            return;
        }

        value = Util.toRoundedMoneyDecimals(value, coin.get().getCurrency().getDecimals());
        double updatedValue = value <= 0? 1D : value;

        String locationName = "x%.1f, y%.1f, z%.1f".formatted(location.getX(), location.getY(), location.getZ());

        coins.getScheduler().runLocationTaskRepeated(location, amount, 1, () -> {
            ItemStack itemStack = coin.get().getItemStackClone();
            ItemMeta meta = itemStack.getItemMeta();
            coins.getCoinMeta().setCoinValue(meta, updatedValue);
            itemStack.setItemMeta(meta);

            Item item = location.getWorld().dropItem(location, itemStack);

            item.setPickupDelay(30);
            item.setVelocity(new Vector(
                (RANDOM.nextDouble() - 0.5) * radius / 10,
                RANDOM.nextDouble() * radius / 5,
                (RANDOM.nextDouble() - 0.5) * radius / 10
            ));
        });

        coins.sendMessage(sender, Language.DROP_DROPPING.with(
            FILL_AMOUNT.filled(amount),
            FILL_RADIUS.filled(radius),
            FILL_TARGET.filled(locationName)
        ));
    }
}
