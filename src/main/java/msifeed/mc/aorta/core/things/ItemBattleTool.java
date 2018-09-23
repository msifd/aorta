package msifeed.mc.aorta.core.things;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemBattleTool extends Item {
    public static String ITEM_NAME = "tool_battle";

    public ItemBattleTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("aorta:tool_battle");
        setCreativeTab(AortaCreativeTab.TOOLS);
        setMaxStackSize(1);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity target) {
        if (FMLCommonHandler.instance().getSide().isClient() || !(target instanceof EntityLivingBase))
            return true;

        System.out.println("entity in");

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            System.out.println("self out");
        }
        return itemStack;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient())
            return;
        final ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemBattleTool))
            return;

        System.out.println("entity out");
    }
}
