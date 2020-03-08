package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionCritical;
import msifeed.mc.more.crabs.action.effects.Buff;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;

public class ActionContext implements Comparable<ActionContext> {
    public final CombatContext context;
    public final Action action;

    public ArrayList<DamageAmount> damageToDeal = new ArrayList<>();
    public ArrayList<DamageAmount> damageToReceive = new ArrayList<>();
    public ArrayList<Buff> buffsToReceive = new ArrayList<>();

    public int scorePlayerMod;
    public int scoreEffects;
    public int scoreScores;
    public float scoreMultiplier;
    public ActionCritical critical;
    public boolean successful;

    public ActionContext(CombatContext context, Action action) {
        this.context = context;
        this.action = action;
        resetScore();
    }

    public void resetScore() {
        scorePlayerMod = 0;
        scoreEffects = 0;
        scoreScores = 0;
        scoreMultiplier = 1;
        critical = ActionCritical.NONE;
        successful = true;
    }

    public int scoreRoll() {
        return MathHelper.floor_float((scoreScores + scoreEffects) * scoreMultiplier);
    }

    public int score() {
        return scoreRoll() + scorePlayerMod;
    }

    @Override
    public int compareTo(ActionContext o) {
        return Comparator
                .comparing(ActionContext::isSuccessful)
                .thenComparing(ActionContext::getCritical)
                .thenComparingInt(ActionContext::score)
                .compare(this, o);
    }

    private ActionCritical getCritical() {
        return critical;
    }

    private boolean isSuccessful() {
        return successful;
    }
}
