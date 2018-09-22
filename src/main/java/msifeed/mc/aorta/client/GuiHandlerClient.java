package msifeed.mc.aorta.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.client.gui.ScreenCharEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class GuiHandlerClient extends GuiHandler {
    @Override
    public void openCharEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new ScreenCharEditor(entity));
    }
}
