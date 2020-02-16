package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionCritical;

public class ActionContext {
    public final CombatContext context;

    public int puppet;
    public int target;

    public Action action;

    public int damageToDeal = 0;
    public int damageToReceive = 0;

    public int scorePlayerMod = 0;
    public int scoreEffects = 0;
    public int scoreDices = 0;
    public int scoreStats = 0;
    public int scoreModifiers = 0;
    public int scoreMultipliers = 0;
    public ActionCritical critical = ActionCritical.NONE;

    public boolean successful = true;

    public ActionContext(CombatContext context) {
        this.context = context;
    }

    public int score() {
        return (scorePlayerMod + scoreEffects + scoreDices + scoreStats + scoreModifiers) * scoreMultipliers;
    }
}
