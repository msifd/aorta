package msifeed.mc.more.tools;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.More;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCombatTool extends Item {
    public static String ITEM_NAME = "tool_combat";

    ItemCombatTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        // RMB + Shift = Self
        if (player.isSneaking())
            handleEntity(player);

        return itemStack;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
        handleEntity(entity);
        return true;
    }

    private void handleEntity(EntityLivingBase entity) {
        More.GUI_HANDLER.toggleCombat(entity);
    }
}
