package community.coins.plugin.paper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import community.coins.plugin.paper.CoinsPaper;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Eli
 * @since April 28, 2026
 */
public final class TestCommand {
    public TestCommand(CoinsPaper coins) {
        coins.registerCommand(
            Commands.literal("givecoin")
            .then(
                Commands.argument("coin_id", StringArgumentType.word())
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }

                    String coinId = StringArgumentType.getString(context, "coin_id");
                    var coin = coins.getConfigService().getCoinsConfig().getDefinedCoin(coinId);
                    if (coin.isEmpty()) {
                        player.sendRichMessage("<#ff0000>Not found");
                        return Command.SINGLE_SUCCESS;
                    }

                    player.getInventory().addItem(coin.get().getClonedCoin());
                    player.sendRichMessage("<#00ff00>Gave coin");
                    return Command.SINGLE_SUCCESS;
                })
            )
            .build(),
            "Test command",
            List.of("coingive")
        );
    }
}
