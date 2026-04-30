package community.coins.plugin.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Eli
 * @since April 29, 2026
 */
public final class EntityUtil {
    /**
     * @param attacker the attacker as a projectile, player, mob or another type of entity
     * @return the source attacker: the attacker itself if not a projectile, or the shooter of the projectile entity
     */
    public static Optional<Entity> getRootOfDamage(@Nullable Entity attacker) {
        if (!(attacker instanceof Projectile projectile)) {
            return Optional.ofNullable(attacker);
        }

        // projectile = trident, arrow, potions, etc.
        if (projectile.getShooter() instanceof Entity shooter) {
            return Optional.of(shooter); // the shooter of the
        }

        // projectile had no shooter
        return Optional.empty();
    }

    /**
     * @param victim the victim to get the attacker for
     * @return the attacker (or source of attacking projectile) of this victim entity
     */
    public static Optional<Entity> getRootAttacker(@NotNull LivingEntity victim) {
        if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent entityCause) {
            return getRootOfDamage(entityCause.getDamager());
        }

        return Optional.empty();
    }
}
