package msifeed.mc.more.tools;

import cpw.mods.fml.common.registry.LanguageRegistry;
import msifeed.mc.genesis.GenesisCreativeTab;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Arrays;
import java.util.List;

public class ItemHealthTool extends Item {
    public static final String ITEM_NAME = "tool_health";

    ItemHealthTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("nether_star");
        setCreativeTab(GenesisCreativeTab.TOOLS);
        setMaxStackSize(1);
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
        String desc = LanguageRegistry.instance().getStringLocalization("item.health_controller.desc");
        desc = StringEscapeUtils.unescapeJava(desc);
        lines.addAll(Arrays.asList(desc.split("\n")));
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase player, ItemStack stack) {
        // Shift+ЛКМ - убрать 1 хп у себя

        if (player.isSneaking())
            changeHealth(player, -1);

        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity target) {
        // ЛКМ по существу - убрать 1 хп у существа

        if (!player.isSneaking()) {
            if (player.worldObj.isRemote || !(target instanceof EntityLivingBase)) return true;
            final EntityLivingBase entity = (EntityLivingBase) target;
            changeHealth(entity, -1);
        }

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        // Shift+ПКМ - добавить 1 хп себе

        if (player.isSneaking())
            changeHealth(player, 1);

        return stack;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
        // ПКМ этим предметом по существу - добавить 1 хп существу
        if (!player.isSneaking()) {
            changeHealth(entity, 1);
            return true;
        }

        return false;
    }

    private static void changeHealth(EntityLivingBase entity, int value) {
        final float currentEntityHealth = entity.getHealth();
        final float result = currentEntityHealth + value;
        if (result > 0 && result <= entity.getMaxHealth())
            entity.setHealth(result);
    }
}
