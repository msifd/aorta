package msifeed.mc.aorta.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.client.gui.ScreenCharEditor;
import msifeed.mc.aorta.client.gui.ScreenRoller;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class GuiHandlerClient extends GuiHandler {
    @Override
    public void openCharEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new ScreenCharEditor(entity));
    }

    @Override
    public void openRoller() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof ScreenRoller) {
            mc.displayGuiScreen(null);
        } else {
            FMLClientHandler.instance().displayGuiScreen(mc.thePlayer, new ScreenRoller());
        }
    }
}
