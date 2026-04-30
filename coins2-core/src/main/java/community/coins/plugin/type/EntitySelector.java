package community.coins.plugin.type;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.data.TransformType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class EntitySelector extends Selector<Entity> {
    public EntitySelector(CoinsCore coins, Entity type) {
        super(coins, type);
    }

    public boolean isOfSelector(@NotNull String selector) {
        return switch (selector.toLowerCase()) {
            case "any" -> true;
            case "player" -> type instanceof Player;
            case "hostile" -> isHostile();
            case "passive" -> isPassive();
            case "from_spawner" -> coins.getPersistentData().isTransformType(type, TransformType.FROM_SPAWNER);
            case "from_split" ->  coins.getPersistentData().isTransformType(type, TransformType.FROM_SPLIT);
            case "from_breeding" -> coins.getPersistentData().isTransformType(type, TransformType.FROM_BREEDING);
            case "from_lightning" -> coins.getPersistentData().isTransformType(type, TransformType.FROM_LIGHTNING);
            default -> false;
        };
    }

    private static boolean hasSameIp(Player player0, Player player1) {
        var address0 = player0.getAddress();
        var address1 = player1.getAddress();

        return address0 != null && address1 != null
            && address0.getAddress().getHostAddress().equals(address1.getAddress().getHostAddress());
    }

    public boolean isHostile() {
        return type instanceof Monster || type instanceof Flying || type instanceof Slime || type instanceof Boss
            || (type instanceof Golem && !(type instanceof Snowman))
            || (type instanceof Wolf wolf && wolf.isAngry());
    }

    public boolean isPassive() {
        // todo can this be replaced with !isHostile() && type instanceof Mob?
        return !isHostile() && !(type instanceof Player) && type instanceof LivingEntity && !(type instanceof ArmorStand);
    }
}
