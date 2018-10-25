package msifeed.mc.aorta.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.client.gui.ScreenLangSelector;
import msifeed.mc.aorta.client.gui.chareditor.ScreenCharEditor;
import msifeed.mc.aorta.client.gui.roller.ScreenRoller;
import msifeed.mc.aorta.client.gui.statuseditor.ScreenStatusEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;

import java.util.function.Supplier;

public class GuiHandlerClient extends GuiHandler {
    @Override
    public void toggleRoller(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            toggleGui(ScreenRoller.class, () -> new ScreenRoller(entity));
    }

    @Override
    public void openCharEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new ScreenCharEditor(entity));
    }

    @Override
    public void toggleStatusEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            toggleGui(ScreenStatusEditor.class, () -> new ScreenStatusEditor(entity));
    }

    @Override
    public void toggleLangSelector(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            toggleGui(ScreenLangSelector.class, () -> new ScreenLangSelector(entity));
    }

    private void toggleGui(Class<?> c, Supplier<GuiScreen> screenSupplier) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (c.isInstance(mc.currentScreen)) {
            mc.displayGuiScreen(null);
        } else {
            FMLClientHandler.instance().displayGuiScreen(mc.thePlayer, screenSupplier.get());
        }
    }
}
