package community.coins.plugin.type;

/**
 * @author Eli
 * @since April 29, 2026
 */
// todo could also be expanded by other plugins, so a registrar or some sorts
public enum EventType {
    // selectors be: player, hostile, passive, spawner, split, bred, etc.
    // todo from entity death event exclude armor stands and non LivingEntity
    ENTITY_KILL_STAB,
    ENTITY_KILL_PROJECTILE,
    ENTITY_DEATH,

    ENTITY_CATCH, // CATCH_FISH
    ENTITY_TAME, // TAME_ANIMAL
    ENTITY_BREED, // BREED_ANIMAL

    // let player define different types, like MINE_COMMON_BLOCK, MINE_VALUABLE_ORE, MINE_CHEAP_ORE
    BLOCK_BREAK,

    ITEM_REPAIR,
    ITEM_ENCHANT,

    RECIPE_UNLOCK,
    ADVANCEMENT_DONE,
    POTION_BREW,
    CROP_HARVEST,
    LOOT_CHEST_OPEN
}
