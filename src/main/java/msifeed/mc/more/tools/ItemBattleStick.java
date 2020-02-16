package msifeed.mc.more.tools;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Arrays;
import java.util.List;

public class ItemBattleStick extends Item {
    public static String ITEM_NAME = "battle_stick";

    public ItemBattleStick() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("blaze_rod");
        setCreativeTab(GenesisCreativeTab.TOOLS);
        setMaxStackSize(1);
        setFull3D();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advanced) {
        String desc = LanguageRegistry.instance().getStringLocalization("item.battle_stick.desc");
        desc = StringEscapeUtils.unescapeJava(desc);
        lines.addAll(Arrays.asList(desc.split("\n")));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity target) {
        // Обработка только на сервере
        // ЛКМ палкой по существу: со сником - сброс, без - добавление в битву
        if (player.worldObj.isRemote || !(target instanceof EntityLivingBase)) return true;

        // FIXME:
//        final EntityLivingBase entity = (EntityLivingBase) target;
//        final CombatContext actor = ContextManager.INSTANCE.getOrCreateContext(entity);
//
//        if (player.isSneaking()) {
//            ContextManager.INSTANCE.hardResetContext(actor);
//        } else {
//            if (entity instanceof EntityLiving) ((EntityLiving) entity).playLivingSound();
//            FightManager.INSTANCE.joinFight(actor);
//        }

        return true;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        // Обработка только на сервере
        // ПКМ палкой по существу:  со сником - исключение из битвы, без - контроль действий
        if (event.entityPlayer.worldObj.isRemote || !(event.target instanceof EntityLivingBase)) return;

        final ItemStack itemStack = event.entityPlayer.getHeldItem();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBattleStick)) return;

        // FIXME:
//        final EntityLivingBase entity = (EntityLivingBase) event.target;
//        final CombatContext actor = ContextManager.INSTANCE.getContext(entity);
//        if (actor == null) return;
//
//        if (event.entityPlayer.isSneaking()) {
//            FightManager.INSTANCE.leaveFight(actor, true);
//        } else {
//            final CombatContext context = ContextManager.INSTANCE.getContext(event.entityPlayer);
//            FightManager.INSTANCE.toggleControl(context, actor);
//        }
    }
}
