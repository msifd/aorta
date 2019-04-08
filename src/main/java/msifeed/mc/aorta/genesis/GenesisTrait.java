package msifeed.mc.aorta.genesis;

public enum GenesisTrait {
    // Types
    block, item,
    rain,

    // Size
    tiny, small, large,

    // Blocks // // // // // // // //
    wooden, stone, metal,
    unbreakable, not_collidable,
    transparent, with_alpha,
    bright_light, dim_light,

    // Appearance
    rotatable, pillar,
    half, crossed_squares,
    without_particles,

    // Logic
    container, chest, door, torch, pane, bed,

    // Extensions
    add_slabs, add_stairs,

    // Special subtypes
    special_log, special_bush,

    // Items // // // // // // // //
    not_stackable, hold_like_tool, usable,

    // Armor
    armor, helmet, plate, legs, boots,

    // Food
    consumable, no_saturation,

    // Books
    book,

    // Rarity
    poor, common, uncommon, rare, epic, legendary,

    ;
}
