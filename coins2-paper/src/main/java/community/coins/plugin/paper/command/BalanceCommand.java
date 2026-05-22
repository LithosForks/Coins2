package community.coins.plugin.paper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import community.coins.plugin.command.BalanceCommandLogic;
import community.coins.plugin.command.CommandLogic;
import community.coins.plugin.paper.CoinsPaper;
import io.papermc.paper.command.brigadier.Commands;

import java.util.List;

/**
 * @author Eli
 * @since May 21, 2026
 */
public final class BalanceCommand extends BalanceCommandLogic {
    private final CoinsPaper coins;
    public BalanceCommand(CoinsPaper coins) {
        super(coins, coins.getCommandService());
        this.coins = coins;
    }

    @Override
    public void register(List<String> labels, String permission) {
        if (labels.isEmpty()) {
            return;
        }

        String label = labels.getFirst();
        labels.removeFirst();

        coins.registerCommand(
            Commands.literal(label)
            .requires(source -> source.getSender().hasPermission(permission))
            .executes(context -> {
                showBalance(context.getSource().getSender(), null, null);
                return Command.SINGLE_SUCCESS;
            })
            .then(
                Commands.argument("currency", StringArgumentType.word())
                .executes(context -> {
                    // todo implement target uuid
                    showBalance(context.getSource().getSender(), context.getArgument("currency", String.class), null);
                    return Command.SINGLE_SUCCESS;
                })
                .suggests((_, builder) -> {
                    CommandLogic.suggestStartsWith(
                        coins.getEconomyService().getCurrencyIdentifiers(),
                        builder.getRemainingLowerCase(), builder::suggest
                    );

                    return builder.buildFuture();
                })
            )
            .build(),
            getDescription(),
            labels
        );
    }
}
