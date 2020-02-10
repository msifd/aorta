package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.effects.Buff;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public class CombatContext {
    public Stage stage;
//    public long lastStatusChange;

    public boolean knockedOut;
    public ArrayList<Buff> buffs = new ArrayList<>();
//    public ArrayList<String> buffNames = new ArrayList<>(); // Для вывода клиентом. Сами баффы живут на сервере.

    public ActionContext actionContext = null;

//    public int puppet;
//    public int target;
//    public Action action;
//    public boolean described;
//    public float damageDealt;

    public CombatContext() {
        softReset(Stage.NONE);
    }

    public void softReset() {
        softReset(stage.isInCombat() ? Stage.IDLE : Stage.NONE);
    }

    public void softReset(Stage stage) {
        updateStage(stage);

        actionContext = null;
//        puppet = 0;
//        action = null;
//        described = false;
//        target = 0;
//        damageDealt = 0;
    }

    public void hardReset() {
        softReset(Stage.IDLE);

        knockedOut = false;
        buffs.clear();
//        buffNames.clear();
    }

    public void updateStage(Stage stage) {
        this.stage = stage;
//        this.lastStatusChange = System.currentTimeMillis() / 1000;
    }

    public void endEffects() {
        buffs.removeIf(Buff::ended);
    }

//    public void updateAction(Action action) {
//        this.action = action;
//        this.described = false;
//        this.damageDealt = 0;
//    }

//    public boolean canSelectAction() {
//        return stage == Stage.ACTIVE && !knockedOut;
//    }
//
//    public boolean canAttack() {
//        return action != null && described && !knockedOut && (stage.ordinal() <= Stage.DAMAGE.ordinal());
//    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        return c;
    }

    public void fromNBT(NBTTagCompound c) {

    }

    public enum Stage {
        IDLE, // Wait for action
        DESCRIBE, // Describe action
        DAMAGE, // Apply damage
        WAIT, // Wait opponent's action
        LEAVE, // Try to leave combat
        LEFT, // Remove from combat
        NONE, // Not in combat
        ;

        public boolean isInCombat() {
            return ordinal() < LEFT.ordinal();
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
