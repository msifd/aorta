package msifeed.mc.aorta.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.core.CoreGuiHandler;
import msifeed.mc.aorta.core.client.gui.ScreenCharEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class CoreGuiHandlerClient extends CoreGuiHandler {
    @Override
    public void openCharEditor(EntityLivingBase entity) {
        FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new ScreenCharEditor(entity));
    }
}
