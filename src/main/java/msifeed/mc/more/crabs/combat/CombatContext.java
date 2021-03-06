package msifeed.mc.more.crabs.combat;

import msifeed.mc.more.crabs.action.ActionHeader;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.parser.EffectStringParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CombatContext {
    public float healthBeforeJoin = 0;
    public int puppet;

    public boolean knockedOut;
    public List<Buff> buffs = new ArrayList<>();
    public Set<String> prevActions = new HashSet<>();

    public Phase phase = Phase.NONE;
    public Role role = Role.NONE;
    public int offender = 0;
    public List<Integer> defenders = Collections.emptyList();

    public ArrayList<DamageAmount> damageDealt = new ArrayList<>();
    public ArrayList<DamageAmount> damageToReceive = new ArrayList<>();

    public ActionHeader action = null;

    public boolean isTraining() {
        return healthBeforeJoin > 0;
    }

    public void removeEndedEffects() {
        buffs.removeIf(Buff::ended);
    }

    public void addPrevAction(String id) {
        prevActions.add(id);
    }

    public void softReset() {
        phase = phase.isInCombat() ? CombatContext.Phase.IDLE : CombatContext.Phase.NONE;
        role = CombatContext.Role.NONE;
        offender = 0;
        defenders = Collections.emptyList();
        damageDealt.clear();
        damageToReceive.clear();
        action = null;
    }

    public void hardReset() {
        healthBeforeJoin = 0;
        puppet = 0;

        knockedOut = false;
        buffs.clear();
        prevActions.clear();

        phase = CombatContext.Phase.NONE;
        role = CombatContext.Role.NONE;
        offender = 0;
        defenders = Collections.emptyList();

        damageDealt.clear();
        damageToReceive.clear();
        action = null;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setFloat(Tags.training, healthBeforeJoin);
        c.setInteger(Tags.puppet, puppet);
        c.setBoolean(Tags.knocked, knockedOut);

        final NBTTagList buffsNbt = new NBTTagList();
        for (Buff b : buffs)
            buffsNbt.appendTag(new NBTTagString(b.encode()));
        c.setTag(Tags.buffs, buffsNbt);

        final NBTTagList prevActionsNbt = new NBTTagList();
        for (String s : prevActions)
            prevActionsNbt.appendTag(new NBTTagString(s));
        c.setTag(Tags.prevActions, prevActionsNbt);

        c.setByte(Tags.phase, (byte) phase.ordinal());
        c.setByte(Tags.role, (byte) role.ordinal());
        c.setInteger(Tags.offender, offender);
        c.setIntArray(Tags.defenders, defenders.stream().mapToInt(Integer::intValue).distinct().toArray());

        final NBTTagList dmgDealtNbt = new NBTTagList();
        for (DamageAmount da : damageDealt)
            dmgDealtNbt.appendTag(da.toNBT());
        c.setTag(Tags.dmgDealt, dmgDealtNbt);

        final NBTTagList dmgToReceiveNbt = new NBTTagList();
        for (DamageAmount da : damageToReceive)
            dmgToReceiveNbt.appendTag(da.toNBT());
        c.setTag(Tags.dmgToReceive, dmgToReceiveNbt);

        if (action != null)
            c.setString(Tags.action, action.id);

        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        healthBeforeJoin = c.getFloat(Tags.training);
        puppet = c.getInteger(Tags.puppet);
        knockedOut = c.getBoolean(Tags.knocked);

        final NBTTagList buffsNbt = c.getTagList(Tags.buffs, 8); // 8 - NBTTagString
        for (int i = 0; i < buffsNbt.tagCount(); i++) {
            final Effect e = EffectStringParser.parseEffect(buffsNbt.getStringTagAt(i));
            if (e instanceof Buff)
                buffs.add((Buff) e);
        }

        final NBTTagList prevActionsNbt = c.getTagList(Tags.prevActions, 8); // 8 - NBTTagString
        prevActions.clear();
        for (int i = 0; i < prevActionsNbt.tagCount(); i++) {
            prevActions.add(prevActionsNbt.getStringTagAt(i));
        }

        phase = Phase.values()[c.getByte(Tags.phase)];
        role = Role.values()[c.getByte(Tags.role)];
        offender = c.getInteger(Tags.offender);
        defenders = IntStream.of(c.getIntArray(Tags.defenders))
                .distinct()
                .boxed()
                .collect(Collectors.toList());

        final NBTTagList dmgDealtNbt = c.getTagList(Tags.dmgDealt, 10); // 10 - NBTTagCompound
        damageDealt.clear();
        for (int i = 0; i < dmgDealtNbt.tagCount(); i++) {
            damageDealt.add(new DamageAmount(dmgDealtNbt.getCompoundTagAt(i)));
        }

        final NBTTagList dmgToReceiveNbt = c.getTagList(Tags.dmgToReceive, 10); // 10 - NBTTagCompound
        damageToReceive.clear();
        for (int i = 0; i < dmgToReceiveNbt.tagCount(); i++) {
            damageToReceive.add(new DamageAmount(dmgToReceiveNbt.getCompoundTagAt(i)));
        }

        action = ActionRegistry.getHeader(c.getString(Tags.action));
    }

    public enum Phase {
        NONE, // Not in combat
        IDLE, // Wait for action
        ATTACK, // Apply damage
        DEFEND, // Select defencive action
        WAIT, // Wait for all defenders select action
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

        public String format() {
            switch (this) {
                case NONE:
                    return "нет";
                case IDLE:
                    return "стоим";
                case ATTACK:
                    return "атака";
                case DEFEND:
                    return "защита";
                case WAIT:
                    return "ожидание";
                case END:
                    return "развязка";
                case LEAVE:
                    return "выход";
                default:
                    return "";
            }
        }
    }

    public enum Role {
        NONE, OFFENCE, DEFENCE;

        public String format() {
            switch (this) {
                case NONE:
                    return "угодно";
                case OFFENCE:
                    return "атака";
                case DEFENCE:
                    return "защита";
                default:
                    return "";
            }
        }
    }

    private static final class Tags {
        static final String training = "training";
        static final String puppet = "puppet";
        static final String knocked = "knocked";
        static final String buffs = "buffs";
        static final String prevActions = "prevActions";
        static final String phase = "phase";
        static final String role = "role";
        static final String offender = "offender";
        static final String defenders = "defenders";
        static final String dmgDealt = "dmgDealt";
        static final String dmgToReceive = "dmgToReceive";
        static final String action = "action";
    }
}
