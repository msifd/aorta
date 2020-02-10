package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.combat.action.ActionCritical;
import net.minecraft.item.ItemStack;

public class ActionContext {
    public final ItemStack weapon;

    public int puppet;
    public int target;

    public int damageToDeal = 0;
    public int damageToReceive = 0;

    public int playerScoreMod = 0;
    public int effectsScore = 0;
    public int dicesScore = 0;
    public int statsScore = 0;
    public int modifiersScore = 0;
    public ActionCritical critical = ActionCritical.NONE;

    public boolean successful = true;

    public ActionContext(ItemStack weapon) {
        this.weapon = weapon;
    }

    public int score() {
        return playerScoreMod + effectsScore + dicesScore + statsScore + modifiersScore;
    }
}
