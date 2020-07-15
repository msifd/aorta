package msifeed.mc.more.crabs.action.effects;

import msifeed.mc.more.crabs.combat.FighterInfo;

public interface Effect {
    String name();
    boolean shouldApply(Stage stage, FighterInfo target, FighterInfo other);
    void apply(Stage stage, FighterInfo target, FighterInfo other);

    boolean same(Effect other);
    boolean stronger(Effect lesser);

    Effect copy();
    String encode();

    enum Stage {
        PRE_SCORE, SCORE, ACTION
    }
}
