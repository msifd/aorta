package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.parser.EffectStringParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CombatContext {
    public int puppet;

    public boolean knockedOut;
    public List<Buff> buffs = new ArrayList<>();
    public Stack<String> prevActions = new Stack<>();

    public Phase phase = Phase.NONE;
    public int target = 0;
    public ActionHeader action = null;

    public void removeEndedEffects() {
        buffs.removeIf(Buff::ended);
    }

    public void addPrevAction(String id) {
        prevActions.remove(id);
        prevActions.push(id);
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setInteger(Tags.puppet, puppet);
        c.setBoolean(Tags.knocked, knockedOut);

        final NBTTagList buffsNbt = new NBTTagList();
        for (Buff b : buffs)
            buffsNbt.appendTag(new NBTTagString(b.toString()));
        c.setTag(Tags.buffs, buffsNbt);

        final NBTTagList prevActionsNbt = new NBTTagList();
        for (String s : prevActions)
            prevActionsNbt.appendTag(new NBTTagString(s));
        c.setTag(Tags.prevActions, prevActionsNbt);

        c.setByte(Tags.phase, (byte) phase.ordinal());
        c.setInteger(Tags.target, target);
        if (action != null)
            c.setString(Tags.action, action.id);

        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        puppet = c.getInteger(Tags.puppet);
        knockedOut = c.getBoolean(Tags.knocked);

        final NBTTagList buffsNbt = c.getTagList(Tags.buffs, 8); // 8 - NBTTagString
        for (int i = 0; i < buffsNbt.tagCount(); i++) {
            final Effect e = EffectStringParser.parseEffect(buffsNbt.getStringTagAt(i));
            if (e instanceof Buff)
                buffs.add((Buff) e);
        }

        final NBTTagList prevActionsNbt = c.getTagList(Tags.prevActions, 8); // 8 - NBTTagString
        for (int i = 0; i < prevActionsNbt.tagCount(); i++) {
            prevActions.add(prevActionsNbt.getStringTagAt(i));
        }

        phase = Phase.values()[c.getByte(Tags.phase)];
        target = c.getInteger(Tags.target);
        action = ActionRegistry.getHeader(c.getString(Tags.action));
    }

    public enum Phase {
        NONE, // Not in combat
        IDLE, // Wait for action
        ATTACK, // Apply damage
        WAIT, // Wait defender's action
        DEFEND, // Wait for defencive action
        END, // Pass damage to entity
        LEAVE, // Try to leave combat
        ;

        public boolean isInCombat() {
            return ordinal() > NONE.ordinal();
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private static final class Tags {
        static final String puppet = "puppet";
        static final String knocked = "knocked";
        static final String buffs = "buffs";
        static final String prevActions = "prevActions";
        static final String phase = "phase";
        static final String target = "target";
        static final String action = "action";
    }
}
