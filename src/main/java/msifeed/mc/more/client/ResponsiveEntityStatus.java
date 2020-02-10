package msifeed.mc.more.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public enum ResponsiveEntityStatus {
    INSTANCE;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onEntityClick(EntityInteractEvent event) {
        if (event.entityPlayer.getHeldItem() == null
                && event.target instanceof EntityLivingBase
                && CharacterAttribute.get(event.target).isPresent())
            More.GUI_HANDLER.toggleStatus((EntityLivingBase) event.target);
    }
}
