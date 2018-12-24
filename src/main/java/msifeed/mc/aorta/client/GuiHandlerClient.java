package msifeed.mc.aorta.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.client.gui.ScreenItemDesigner;
import msifeed.mc.aorta.client.gui.ScreenLangSelector;
import msifeed.mc.aorta.client.gui.book.ScreenBookEditor;
import msifeed.mc.aorta.client.gui.book.ScreenBookViewer;
import msifeed.mc.aorta.client.gui.chareditor.ScreenCharEditor;
import msifeed.mc.aorta.client.gui.roller.ScreenRoller;
import msifeed.mc.aorta.client.gui.statuseditor.ScreenStatusEditor;
import msifeed.mc.aorta.client.hud.DebugHud;
import msifeed.mc.aorta.client.hud.DisableVanillaHud;
import msifeed.mc.aorta.client.lock.HudDoorLock;
import msifeed.mc.aorta.client.lock.ScreenDigitalLock;
import msifeed.mc.aorta.locks.LockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

public class GuiHandlerClient extends GuiHandler {
    public void init() {
        MinecraftForge.EVENT_BUS.register(DisableVanillaHud.INSTANCE);
        MinecraftForge.EVENT_BUS.register(DebugHud.INSTANCE);
        MinecraftForge.EVENT_BUS.register(HudDoorLock.INSTANCE);
    }

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

    @Override
    public void toggleBookViewer(EntityPlayer player) {
        if (player.worldObj.isRemote)
            toggleGui(ScreenBookViewer.class, () -> new ScreenBookViewer(player));
    }

    @Override
    public void toggleBookEditor(EntityPlayer player) {
        if (player.worldObj.isRemote)
            toggleGui(ScreenBookEditor.class, () -> new ScreenBookEditor(player));
    }

    @Override
    public void toggleDigitalLock(LockTileEntity lock) {
        if (lock.getWorldObj().isRemote)
            toggleGui(ScreenDigitalLock.class, () -> new ScreenDigitalLock(lock));
    }

    public void toggleDesignerScreen() {
        if (Minecraft.getMinecraft().theWorld.isRemote)
            toggleGui(ScreenItemDesigner.class, ScreenItemDesigner::new);
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
