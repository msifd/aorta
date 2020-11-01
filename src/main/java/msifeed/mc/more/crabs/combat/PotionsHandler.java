package msifeed.mc.more.crabs.combat;

import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.ItemBrew;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionList;
import com.emoniph.witchery.brewing.action.BrewPotionEffect;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.crabs.action.ActionTag;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.parser.BuffJsonAdapter;
import msifeed.mc.more.crabs.utils.ActionAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.GetUtils;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.JsonConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import vazkii.botania.api.brew.IBrewItem;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public enum PotionsHandler {
    INSTANCE;

    private final TypeToken<HashMap<Integer, PotionRule>> potionRulesType = new TypeToken<HashMap<Integer, PotionRule>>() {
    };
    private final JsonConfig<HashMap<Integer, PotionRule>> rulesConfig = ConfigBuilder.of(potionRulesType, "potion_rules.json")
            .addAdapter(Buff.class, new BuffJsonAdapter())
            .sync()
            .create();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving.worldObj.isRemote)
            return;

        final CombatContext com = CombatAttribute.get(event.entityLiving).orElse(null);
        if (com == null)
            return;

        if (com.phase == CombatContext.Phase.DEFEND)
            handleDefender(event.entityLiving, com);
    }

    private void handleDefender(EntityLivingBase entity, CombatContext com) {
        final boolean offenceActionHasApply = GetUtils.entityLiving(entity, com.offender)
                .flatMap(CombatAttribute::get)
                .filter(c -> c.phase == CombatContext.Phase.ATTACK)
                .map(c -> c.action != null && c.action.hasAnyTag(ActionTag.apply))
                .orElse(false);
        if (!offenceActionHasApply)
            return;

        final ActionContext act = ActionAttribute.get(entity).orElse(new ActionContext());
        act.buffsToReceive.addAll(convertEntityEffects(entity));
        ActionAttribute.INSTANCE.set(entity, act);
    }

    public static List<Buff> convertEntityEffects(EntityLivingBase entity) {
        final Collection<PotionEffect> effects = entity.getActivePotionEffects();
        if (effects.isEmpty())
            return Collections.emptyList();

        final List<Integer> toRemove = new ArrayList<>(effects.size());
        final List<Buff> convertedBuffs = new ArrayList<>(effects.size());

        for (PotionEffect e : effects) {
            if (convert(e, convertedBuffs))
                toRemove.add(e.getPotionID());
        }

        for (int id : toRemove)
            entity.removePotionEffect(id);

        return convertedBuffs;
    }

    public static List<Effect> convertPassiveEffects(EntityLivingBase entity) {
        final Collection<PotionEffect> potions = entity.getActivePotionEffects();
        if (potions.isEmpty())
            return Collections.emptyList();

        final List<Buff> convertedBuffs = new ArrayList<>(potions.size());
        for (PotionEffect e : potions) {
            convert(e, convertedBuffs);
        }

        return convertedBuffs.stream().map(buff -> buff.effect).collect(Collectors.toList());
    }

    public static List<Buff> convertItemStack(ItemStack stack, EntityLivingBase user) {
        final Class<? extends Item> clazz = stack.getItem().getClass();

        final List<PotionEffect> effects;
        if (clazz.getName().startsWith("com.emoniph.witchery"))
            effects = convertWitcheryPotion(stack, user instanceof EntityPlayer ? (EntityPlayer) user : null);
        else if (clazz.getName().startsWith("vazkii.botania"))
            effects = convertBotaniaPotion(stack);
        else
            effects = convertVanillaPotion(stack);

        if (effects == null || effects.isEmpty())
            return Collections.emptyList();

        final List<Buff> convertedBuffs = new ArrayList<>();
        for (PotionEffect e : effects)
            convert(e, convertedBuffs);

        return convertedBuffs;
    }

    private static List<PotionEffect> convertWitcheryPotion(ItemStack stack, EntityPlayer player) {
        if (!(stack.getItem() instanceof ItemBrew))
            return convertVanillaPotion(stack);

        try {
            final Field ingredientsField = WitcheryBrewRegistry.class.getDeclaredField("ingredients");
            ingredientsField.setAccessible(true);
            final Hashtable<BrewItemKey, BrewAction> ingredients = (Hashtable<BrewItemKey, BrewAction>) ingredientsField.get(WitcheryBrewRegistry.INSTANCE);

            final BrewActionList actionList = new BrewActionList(stack.getTagCompound(), ingredients);
            final ModifiersEffect modifiers = new ModifiersEffect(1.0D, 1.0D, false, null, false, 0, player);

            final List<PotionEffect> effects = new ArrayList<>();
            for (BrewAction action : actionList.actions) {
                if (action.augmentEffectLevels(modifiers.effectLevel)) {
                    action.augmentEffectModifiers(modifiers);
                    if (action instanceof BrewPotionEffect) {
                        final BrewPotionEffect b = (BrewPotionEffect) action;
                        final int strength = Math.min(modifiers.getStrength(), 10);
                        effects.add(new PotionEffect(b.potion.id, modifiers.getModifiedDuration(b.baseDuration), strength, modifiers.noParticles));
                    }
                }
            }

            return effects;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Collections.emptyList();
        }
    }

    private static List<PotionEffect> convertBotaniaPotion(ItemStack stack) {
        if (stack.getItem() instanceof IBrewItem)
            return ((IBrewItem) stack.getItem()).getBrew(stack).getPotionEffects(stack);
        else
            return convertVanillaPotion(stack);
    }

    private static List<PotionEffect> convertVanillaPotion(ItemStack stack) {
        return Items.potionitem.getEffects(stack);
    }

    private static boolean convert(PotionEffect e, List<Buff> converted) {
        final HashMap<Integer, PotionRule> rulesMap = INSTANCE.rulesConfig.get();
        final PotionRule rule = rulesMap.get(e.getPotionID());
        if (rule == null || rule.amplifications.isEmpty())
            return false;

        final int steps = e.getDuration() / rule.stepDuration;
        final int index = Math.min(e.getAmplifier(), rule.amplifications.size() - 1);

        for (Buff tmp : rule.amplifications.get(index)) {
            final Buff copy = tmp.copy();
            copy.steps += steps;
            converted.add(copy);
        }

        return true;
    }

    public static class PotionRule {
        public int stepDuration = 1200;
        public List<List<Buff>> amplifications;
    }
}
