package msifeed.mc.aorta.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.client.gui.chareditor.ScreenCharEditor;
import msifeed.mc.aorta.client.gui.roller.ScreenRoller;
import msifeed.mc.aorta.client.gui.status_editor.ScreenFightHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class GuiHandlerClient extends GuiHandler {
    @Override
    public void toggleRoller(EntityLivingBase entity) {
        if (!entity.worldObj.isRemote)
            return;
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof ScreenRoller) {
            mc.displayGuiScreen(null);
        } else {
            FMLClientHandler.instance().displayGuiScreen(mc.thePlayer, new ScreenRoller(entity));
        }
    }

    @Override
    public void openCharEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new ScreenCharEditor(entity));
    }

    @Override
    public void toggleStatusEditor(EntityLivingBase entity) {
        if (!entity.worldObj.isRemote)
            return;
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof ScreenFightHelper) {
            mc.displayGuiScreen(null);
        } else {
            FMLClientHandler.instance().displayGuiScreen(mc.thePlayer, new ScreenFightHelper(entity));
        }
    }
}
