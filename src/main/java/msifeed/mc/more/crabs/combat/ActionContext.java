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

    public ArrayList<Buff> buffsToReceive = new ArrayList<>();

    public EnumMap<Ability, Integer> effectAbilityMods = new EnumMap<>(Ability.class);

    public int scorePlayerMod = 0;
    public int scoreAction = 0;
    public Criticalness critical = Criticalness.NONE;
    public boolean successful = true;

    public ActionContext(Action action) {
        this.action = action;
    }

    public int score() {
        return scoreAction + scorePlayerMod;
    }

    public void resetScore() {
        scorePlayerMod = 0;
        scoreAction = 0;
        critical = Criticalness.NONE;
        successful = true;
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
