package msifeed.mc.aorta.core.things;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.things.AortaCreativeTab;
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
        // RMB + Shift = Self
        if (player.isSneaking()) {
            handleEntity(player);
        }
        return itemStack;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        final ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemCharTool))
            return;
        if (event.target instanceof EntityLivingBase)
            handleEntity((EntityLivingBase) event.target);
    }

    private void handleEntity(EntityLivingBase entity) {
        Aorta.GUI_HANDLER.openCharEditor(entity);
    }
}
