package msifeed.mc.more.crabs.combat;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.parser.BuffJsonAdapter;
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.JsonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.*;

public enum PotionsHandler {
    INSTANCE;

    private TypeToken<HashMap<Integer, ArrayList<PotionRule>>> potionRulesType = new TypeToken<HashMap<Integer, ArrayList<PotionRule>>>() {};
    private JsonConfig<HashMap<Integer, ArrayList<PotionRule>>> rulesConfig = ConfigBuilder.of(potionRulesType, "potion_rules.json")
            .addAdapter(Buff.class, new BuffJsonAdapter())
            .create();

    public void init() {
//        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving.worldObj.isRemote)
            return;

        final CombatContext com = CombatAttribute.get(event.entityLiving).orElse(null);
        if (com == null)
            return;

        if (com.phase == CombatContext.Phase.ATTACK)
            handleSelfApply(event.entityLiving, com);
        else if (com.phase == CombatContext.Phase.DEFEND)
            handleDefender(event.entityLiving, com);
    }

    private void handleSelfApply(EntityLivingBase entity, CombatContext com) {
        if (!com.action.hasAnyTag(ActionTag.apply))
            return;

        final ActionContext act = ActionAttribute.require(entity);
        final List<Buff> buffs = convertPotionEffects(entity, com);
        act.buffsToReceive.addAll(buffs);
    }

    private void handleDefender(EntityLivingBase entity, CombatContext com) {
        if (com.targets.isEmpty())
            return;
        final Entity offenderEntity = entity.worldObj.getEntityByID(com.targets.get(0));
        if (offenderEntity == null)
            return;
        final CombatContext offenderCom = CombatAttribute.get(offenderEntity).orElse(null);
        if (offenderCom == null || offenderCom.phase != CombatContext.Phase.ATTACK)
            return;
        if (!offenderCom.action.hasAnyTag(ActionTag.apply))
            return;

        final ActionContext act = ActionAttribute.require(entity);
        final List<Buff> buffs = convertPotionEffects(entity, com);
        act.buffsToReceive.addAll(buffs);
    }

    public static List<Buff> convertPotionEffects(EntityLivingBase entity, CombatContext com) {
        final Collection<PotionEffect> effects = entity.getActivePotionEffects();
        if (effects.isEmpty())
            return Collections.emptyList();

        final HashMap<Integer, ArrayList<PotionRule>> ruleLists = INSTANCE.rulesConfig.get();
        final ArrayList<Integer> toRemove = new ArrayList<>();
        final List<Buff> convertedBuffs = new ArrayList<>();

        for (PotionEffect e : effects) {
            final ArrayList<PotionRule> rules = ruleLists.get(e.getPotionID());
            if (rules == null)
                continue;
            for (PotionRule pr : rules) {
                if (e.getDuration() <= pr.maxDuration && e.getAmplifier() <= pr.maxAmplifier) {
                    for (Buff b : pr.buffs) {
//                        System.out.println(String.format("Add buff '%s' to %s", b.toString(), event.entityLiving.getCommandSenderName()));
//                        Buff.mergeBuff(com.buffs, b);
                        convertedBuffs.add(b);
                        toRemove.add(e.getPotionID());
                    }
                    break;
                }
            }
        }

        for (int id : toRemove)
            entity.removePotionEffect(id);

        return convertedBuffs;
    }

    public static class PotionRule {
        public int maxDuration;
        public int maxAmplifier;
        public List<Buff> buffs;
    }
}
