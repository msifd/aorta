package msifeed.mc.more.tools;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.More;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemStatusTool extends Item {
    public static String ITEM_NAME = "tool_status";

    public ItemStatusTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.TOOLS);
        setMaxStackSize(1);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        // RMB + Shift = Self
        if (player.isSneaking()) {
            handleEntity(player);
        }
        return itemStack;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        final ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemStatusTool))
            return;
        if (event.target instanceof EntityLivingBase)
            handleEntity((EntityLivingBase) event.target);
    }

    private void handleEntity(EntityLivingBase entity) {
        More.GUI_HANDLER.toggleStatusEditor(entity);
    }
}
