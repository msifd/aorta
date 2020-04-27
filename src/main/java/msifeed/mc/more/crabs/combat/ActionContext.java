package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.rolls.Criticalness;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;

public class ActionContext implements Comparable<ActionContext> {
    public Action action;

    public ArrayList<DamageAmount> damageDealt = new ArrayList<>();
    public ArrayList<DamageAmount> damageToReceive = new ArrayList<>();
    public ArrayList<Buff> buffsToReceive = new ArrayList<>();

    public EnumMap<Ability, Integer> effectAbilityMods = new EnumMap<>(Ability.class);

    public int scorePlayerMod;
    public int scoreAction;
    public Criticalness critical;
    public boolean successful;

    public void updateAction(Action action) {
        this.action = action;
        resetScore();
    }

    public void reset() {
        action = null;
        damageDealt.clear();
        damageToReceive.clear();
        buffsToReceive.clear();
        effectAbilityMods.clear();

        resetScore();
    }

    public void resetScore() {
        scorePlayerMod = 0;
        scoreAction = 0;
        critical = Criticalness.NONE;
        successful = true;
    }

    public int score() {
        return scoreAction + scorePlayerMod;
    }

    @Override
    public int compareTo(ActionContext o) {
        if (this == o) return 0;
        return Comparator
                .comparing(ActionContext::isSuccessful)
                .thenComparing(ActionContext::getCritical)
                .thenComparingInt(ActionContext::score)
                .compare(this, o);
    }

    private Criticalness getCritical() {
        return critical;
    }

    private boolean isSuccessful() {
        return successful;
    }
}
