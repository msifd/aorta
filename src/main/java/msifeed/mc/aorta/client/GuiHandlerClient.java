package msifeed.mc.aorta.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.client.gui.ScreenRoller;
import msifeed.mc.aorta.client.gui.chareditor.ScreenCharEditor;
import msifeed.mc.aorta.client.gui.fighter.ScreenFightHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class GuiHandlerClient extends GuiHandler {
    @Override
    public void openCharEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new ScreenCharEditor(entity));
    }

    @Override
    public void toggleRoller() {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof ScreenRoller) {
            mc.displayGuiScreen(null);
        } else {
            FMLClientHandler.instance().displayGuiScreen(mc.thePlayer, new ScreenRoller());
        }
    }

    @Override
    public void toggleFightHelper(EntityLivingBase entity) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof ScreenFightHelper) {
            mc.displayGuiScreen(null);
        } else {
            FMLClientHandler.instance().displayGuiScreen(mc.thePlayer, new ScreenFightHelper(entity));
        }
    }
}
