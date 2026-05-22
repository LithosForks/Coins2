package community.coins.plugin.spigot.command;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.command.BalanceCommandLogic;
import community.coins.plugin.command.CommandLogic;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eli
 * @since May 21, 2026
 */
public final class BalanceCommand extends BalanceCommandLogic {
    public BalanceCommand(CoinsCore coins) {
        super(coins, coins.getCommandService());
    }

    // todo this is a copy from CoinsCommand, so merge it
    @Override
    public void register(List<String> labels, String permission) {
        if (labels.isEmpty()) {
            return;
        }

        String label = labels.getFirst();
        labels.removeFirst();

        try {
            Field commandMapField = coins.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            CommandMap commandMap = (CommandMap) commandMapField.get(coins.getServer());
            Command command = new BukkitCommand(label) {
                @NullMarked
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    boolean success = handleCommand(sender, args);
                    if (!success) {
                        sender.sendMessage(ChatColor.RED + "Incomplete command.");
                    }
                    return true;
                }

                @NullMarked
                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return handleCompletions(sender, args);
                }
            };

            command.setPermission(permission);
            command.setDescription(getDescription());
            command.setAliases(labels);
            commandMap.register(coins.getDescription().getName(), command);
        }
        catch (Exception _) {}
    }

    private boolean handleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            showBalance(sender, null, null);
            return true;
        }

        showBalance(sender, args[0].toLowerCase(), null); // todo allow specifying target uuid
        return true;
    }

    private List<String> handleCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length <= 1) {
            String remaining = args.length == 1? args[0].toLowerCase() : "";
            CommandLogic.suggestStartsWith(coins.getEconomyService().getCurrencyIdentifiers(), remaining, completions::add);
        }
        return completions;
    }
}
