package community.coins.plugin.type.filter;

import org.bukkit.NamespacedKey;

import java.util.Set;

/**
 * @author Eli
 * @since May 02, 2026
 */
public final class EventFilterConfig {
    // initiator

    private String initiatorPermission;

    public void setInitiatorPermission(String initiatorPermission) {
        this.initiatorPermission = initiatorPermission;
    }

    public String getInitiatorPermission() {
        return initiatorPermission;
    }

    private Set<NamespacedKey> initiatorType;

    public void setInitiatorType(Set<NamespacedKey> initiatorType) {
        this.initiatorType = initiatorType;
    }

    public Set<NamespacedKey> getInitiatorType() {
        return initiatorType;
    }

    private Boolean initiatorAny;

    public void setInitiatorAny(boolean initiatorAny) {
        this.initiatorAny = initiatorAny;
    }

    public Boolean getInitiatorAny() {
        return initiatorAny;
    }

    // target

    private Set<NamespacedKey> targetType;

    public void setTargetType(Set<NamespacedKey> targetType) {
        this.targetType = targetType;
    }

    public Set<NamespacedKey> getTargetType() {
        return targetType;
    }

    private Set<String> targetCategory;

    public void setTargetCategory(Set<String> targetCategory) {
        this.targetCategory = targetCategory;
    }

    public Set<String> getTargetCategory() {
        return targetCategory;
    }

    private Integer targetMinXpDrop;

    public void setTargetMinXpDrop(int targetMinXpDrop) {
        this.targetMinXpDrop = targetMinXpDrop;
    }

    public Integer getTargetMinXpDrop() {
        return targetMinXpDrop;
    }

    private Boolean targetAllowSameBlock;

    public void setTargetAllowSameBlock(boolean targetAllowSameBlock) {
        this.targetAllowSameBlock = targetAllowSameBlock;
    }

    public Boolean getTargetAllowSameBlock() {
        return targetAllowSameBlock;
    }

    private Boolean targetPreventAlts;

    public void setTargetPreventAlts(boolean targetPreventAlts) {
        this.targetPreventAlts = targetPreventAlts;
    }

    public Boolean getTargetPreventAlts() {
        return targetPreventAlts;
    }

    private Double targetMinPlayerDamage;

    public void setTargetMinPlayerDamage(double targetMinPlayerDamage) {
        this.targetMinPlayerDamage = targetMinPlayerDamage;
    }

    public Double getTargetMinPlayerDamage() {
        return targetMinPlayerDamage;
    }

    // location

    private Set<String> locationDisabledWorlds;

    public void setLocationDisabledWorlds(Set<String> locationDisabledWorlds) {
        this.locationDisabledWorlds = locationDisabledWorlds;
    }

    public Set<String> getLocationDisabledWorlds() {
        return locationDisabledWorlds;
    }

    private Integer locationCooldownCapAmount;

    public void setLocationCooldownCapAmount(int locationCooldownCapAmount) {
        this.locationCooldownCapAmount = locationCooldownCapAmount;
    }

    public Integer getLocationCooldownCapAmount() {
        return locationCooldownCapAmount;
    }

    private String locationCooldownDuration;

    public void setLocationCooldownDuration(String locationCooldownDuration) {
        this.locationCooldownDuration = locationCooldownDuration;
    }

    public String getLocationCooldownDuration() {
        return locationCooldownDuration;
    }
}
