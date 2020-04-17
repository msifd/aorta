package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.rolls.Criticalness;

import java.util.ArrayList;
import java.util.Comparator;

public class ActionContext implements Comparable<ActionContext> {
    public final Action action;
    public final Role role;

    public ArrayList<DamageAmount> damageToDeal = new ArrayList<>();
    public ArrayList<DamageAmount> damageToReceive = new ArrayList<>();
    public ArrayList<Buff> buffsToReceive = new ArrayList<>();

    public int scorePlayerMod;
    public int scoreAction;
    public float scoreMultiplier;
    public Criticalness critical;
    public boolean successful;

    public ActionContext(Action action, Role role) {
        this.action = action;
        this.role = role;
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

    public enum Role {
        offence, defence
    }
}
