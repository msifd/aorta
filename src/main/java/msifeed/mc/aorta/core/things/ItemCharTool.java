package msifeed.mc.aorta.core.things;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.client.gui.ScreenCharEditor;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemCharTool extends Item {
    public static String ITEM_NAME = "tool_char";

    public ItemCharTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("aorta:tool_char");
        setCreativeTab(AortaCreativeTab.TOOLS);
        setMaxStackSize(1);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && player.isSneaking()) {
            handleEntity(player);
        }
        return itemStack;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            return;
        final ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemCharTool))
            return;

        handleEntity(event.entityLiving);
    }

    private void handleEntity(EntityLivingBase entity) {
        FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new ScreenCharEditor(entity));
    }
}
