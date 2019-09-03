package msifeed.mc.aorta.client;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.client.gui.ScreenItemDesigner;
import msifeed.mc.aorta.client.gui.ScreenLangSelector;
import msifeed.mc.aorta.client.gui.book.ScreenBookLoader;
import msifeed.mc.aorta.client.gui.book.ScreenBookViewer;
import msifeed.mc.aorta.client.gui.book.ScreenNoteEditor;
import msifeed.mc.aorta.client.gui.chareditor.ScreenCharEditor;
import msifeed.mc.aorta.client.gui.roller.ScreenRoller;
import msifeed.mc.aorta.client.gui.status.ScreenStatus;
import msifeed.mc.aorta.client.hud.DebugHud;
import msifeed.mc.aorta.client.hud.StatusHudReplacer;
import msifeed.mc.aorta.client.lock.HudLock;
import msifeed.mc.aorta.client.lock.ScreenDigitalLock;
import msifeed.mc.aorta.client.lock.ScreenSkeletalKey;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.locks.LockObject;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

public class GuiHandlerClient extends GuiHandler {
    public void init() {
        StatusHudReplacer.init();
        MinecraftForge.EVENT_BUS.register(DebugHud.INSTANCE);
        MinecraftForge.EVENT_BUS.register(HudLock.INSTANCE);
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
        if (entity.worldObj.isRemote) {
            final boolean isGm = CharacterAttribute.has(Minecraft.getMinecraft().thePlayer, Trait.gm);
            toggleGui(ScreenStatus.class, () -> new ScreenStatus(entity, true, isGm));
        }
    }

    @Override
    public void toggleStatus(EntityLivingBase entity) {
        if (entity.worldObj.isRemote) {
            final boolean isGm = CharacterAttribute.has(Minecraft.getMinecraft().thePlayer, Trait.gm);
            toggleGui(ScreenStatus.class, () -> new ScreenStatus(entity, false, isGm));
        }
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
    public void toggleBookLoader(EntityPlayer player) {
        if (player.worldObj.isRemote)
            toggleGui(ScreenBookLoader.class, () -> new ScreenBookLoader(player));
    }

    public void toggleBookEditor(EntityPlayer player) {
        if (player.worldObj.isRemote)
            toggleGui(ScreenNoteEditor.class, () -> new ScreenNoteEditor(player));
    }

    @Override
    public void toggleDigitalLock(LockObject lock) {
        if (lock.getTileEntity().getWorldObj().isRemote)
            toggleGui(ScreenDigitalLock.class, () -> new ScreenDigitalLock(lock));
    }

    @Override
    public void toggleSkeletalKey(LockObject lock) {
        if (lock.getTileEntity().getWorldObj().isRemote)
            toggleGui(ScreenSkeletalKey.class, () -> new ScreenSkeletalKey(lock));
    }

    @Override
    public void toggleDesignerScreen() {
        if (Minecraft.getMinecraft().theWorld.isRemote)
            toggleGui(ScreenItemDesigner.class, ScreenItemDesigner::new);
    }

    private void toggleGui(Class<?> c, Supplier<GuiScreen> screenSupplier) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Class<?> currentScreenClass = mc.currentScreen == null ? null : mc.currentScreen.getClass();
        if (mc.currentScreen instanceof MellowGuiScreen) {
            ((MellowGuiScreen) mc.currentScreen).closeGui();
            if (mc.currentScreen != null)
                return;
        } else {
            mc.displayGuiScreen(null);
        }

        if (currentScreenClass != c) {
            try {
                FMLClientHandler.instance().displayGuiScreen(mc.thePlayer, screenSupplier.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
