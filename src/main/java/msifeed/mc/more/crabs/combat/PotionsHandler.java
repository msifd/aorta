package msifeed.mc.more.crabs.combat;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.parser.BuffJsonAdapter;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.JsonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public enum PotionsHandler {
    INSTANCE;

    private TypeToken<HashMap<Integer, ArrayList<PotionRule>>> potionRulesType = new TypeToken<HashMap<Integer, ArrayList<PotionRule>>>() {};
    private JsonConfig<HashMap<Integer, ArrayList<PotionRule>>> rulesConfig = ConfigBuilder.of(potionRulesType, "potion_rules.json")
            .addAdapter(Buff.class, new BuffJsonAdapter())
            .create();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving.worldObj.isRemote)
            return;

        final Collection<PotionEffect> effects = event.entityLiving.getActivePotionEffects();
        if (effects.isEmpty())
            return;

        final CombatContext com = CombatAttribute.get(event.entityLiving).orElse(null);
        if (com == null || com.phase != CombatContext.Phase.DEFEND)
            return;

        final Entity offenderEntity = event.entityLiving.worldObj.getEntityByID(com.target);
        if (offenderEntity == null)
            return;
        final CombatContext offenderCom = CombatAttribute.get(offenderEntity).orElse(null);
        if (offenderCom == null || offenderCom.phase != CombatContext.Phase.ATTACK)
            return;

        final HashMap<Integer, ArrayList<PotionRule>> ruleLists = rulesConfig.get();
        final ArrayList<Integer> toRemove = new ArrayList<>();

        for (PotionEffect e : effects) {
            final ArrayList<PotionRule> rules = ruleLists.get(e.getPotionID());
            if (rules == null)
                continue;
            for (PotionRule pr : rules) {
                if (e.getDuration() <= pr.maxDuration && e.getAmplifier() <= pr.maxAmplifier) {
                    System.out.println(String.format("Add buff '%s' to %s", pr.buff.toString(), event.entityLiving.getCommandSenderName()));
                    Buff.mergeBuff(com.buffs, pr.buff);
                    toRemove.add(e.getPotionID());
                    break;
                }
            }
        }

        for (int id : toRemove)
            event.entityLiving.removePotionEffect(id);

        if (!toRemove.isEmpty())
            CombatAttribute.INSTANCE.set(event.entityLiving, com);
    }

    public static class PotionRule {
        public int maxDuration;
        public int maxAmplifier;
        public Buff buff;
    }
}
