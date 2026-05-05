package community.coins.plugin.type.filter;

import community.coins.plugin.CoinsCore;
import community.coins.plugin.type.registrar.EventType;

import java.util.HashSet;
import java.util.Set;

/**
 * this has all the entries of what is allowed (in the config) for this event filter
 * @author Eli
 * @since May 02, 2026
 */
public final class FilterContractBuilder {
    private final CoinsCore coins;
    private final EventType eventType;

    public FilterContractBuilder(CoinsCore coins, EventType eventType) {
        this.coins = coins;
        this.eventType = eventType;
    }

    public FilterContract build() {
        return new FilterContract(coins, eventType, allowedPaths);
    }

    // allowed paths in the config
    private final Set<String> allowedPaths = new HashSet<>();

    // initiator

    private void allows(String type, String path) {
        allowedPaths.add(type + "." + path);
    }

    public FilterContractBuilder hasInitiatorPlayer() {
        allows("initiator", "permission");
        return this;
    }

    public FilterContractBuilder hasInitiatorEntity() {
        allows("initiator", "type");
        return this;
    }

    public FilterContractBuilder hasInitiatorAny() {
        allows("initiator", "any");
        return this;
    }

    // target

    public FilterContractBuilder hasTargetType() {
        allows("target", "type");
        return this;
    }

    public FilterContractBuilder hasTargetEntity() {
        allows("target", "type");
        allows("target", "category");
        return this;
    }

    public FilterContractBuilder hasTargetMinXpDrop() {
        allows("target", "min-xp-drop");
        return this;
    }

    public FilterContractBuilder hasTargetAllowSameBlock() {
        allows("target", "allow-same-block");
        return this;
    }

    public FilterContractBuilder hasTargetPreventAlts() {
        allows("target", "prevent-alts");
        return this;
    }

    public FilterContractBuilder hasTargetMinPlayerDamage() {
        allows("target", "min-player-damage");
        return this;
    }

    // location

    public FilterContractBuilder hasLocationWorld() {
        allows("location", "disabled-worlds");
        return this;
    }

    public FilterContractBuilder hasLocationCooldown() {
        allows("location", "cooldown.cap-amount");
        allows("location", "cooldown.duration");
        return this;
    }
}
