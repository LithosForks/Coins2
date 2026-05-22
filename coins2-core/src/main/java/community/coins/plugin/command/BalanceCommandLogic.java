package community.coins.plugin.command;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.economy.DefinedCurrency;
import community.coins.plugin.language.EntryReplacement;
import community.coins.plugin.language.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Eli
 * @since May 21, 2026
 */
public abstract class BalanceCommandLogic extends CommandLogic {
    public BalanceCommandLogic(CoinsCore coins, CommandService service) {
        super(coins, service, "balance");
    }

    private static final EntryReplacement FILL_ID = new EntryReplacement("identifier");
    private static final EntryReplacement FILL_CURRENCY = new EntryReplacement("currency");
    private static final EntryReplacement FILL_BALANCE = new EntryReplacement("balance");

    @Override
    public String getDescription() {
        return "Show your current balance for a specific currency.";
    }

    public void showBalance(CommandSender sender, @Nullable String currencyId, @Nullable UUID targetUuid) {
        if (currencyId == null) {
            if (coins.getConfigService().getCurrenciesConfig().getDefinedKeys().size() != 1) {
                coins.sendMessage(sender, Language.BALANCE_PROVIDE_CURRENCY);
                return;
            }

            showBalance(sender, coins.getConfigService().getCurrenciesConfig().getDefinedItems().iterator().next(), targetUuid);
            return;
        }

        Optional<DefinedCurrency> currency = coins.getEconomyService().getCurrency(currencyId);
        if (currency.isEmpty()) {
            coins.sendMessage(sender, Language.CURRENCY_NOT_FOUND.with(FILL_ID.filled(currencyId)));
            return;
        }

        showBalance(sender, currency.get(), targetUuid);
    }

    private void showBalance(CommandSender sender, @NotNull DefinedCurrency currency, @Nullable UUID targetUuid) {
        UUID uuid;
        if (targetUuid == null) {
            if (!(sender instanceof Player player)) {
                coins.sendMessage(sender, Language.BALANCE_PROVIDE_PLAYER);
                return;
            }
            uuid = player.getUniqueId();
        }
        else {
            uuid = targetUuid;
        }

        currency.submitTransaction(transaction ->
            coins.sendMessage(sender, Language.BALANCE_DISPLAY.with(
                FILL_CURRENCY.filled(currency.getPluralName()),
                FILL_BALANCE.filled(currency.getFormatMessage(transaction.getBalance(uuid)))
            ))
        );
    }
}
