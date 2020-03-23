package msifeed.mc.more.client.common;

import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.books.client.ScreenBookLoader;
import msifeed.mc.extensions.books.client.ScreenBookViewer;
import msifeed.mc.extensions.books.client.ScreenNoteEditor;
import msifeed.mc.extensions.locks.LockObject;
import msifeed.mc.extensions.locks.client.HudLock;
import msifeed.mc.extensions.locks.client.ScreenDigitalLock;
import msifeed.mc.extensions.locks.client.ScreenSkeletalKey;
import msifeed.mc.extensions.rename.ScreenRenamer;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.more.client.combat.CombatScreen;
import msifeed.mc.more.client.combat.other.CombatOverlay;
import msifeed.mc.more.client.morph.MorphScreen;
import msifeed.mc.more.client.status.StatusScreen;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

public class GuiHandlerClient extends GuiHandler {
    public void init() {
        MinecraftForge.EVENT_BUS.register(HudLock.INSTANCE);
        MinecraftForge.EVENT_BUS.register(CombatOverlay.INSTANCE);
//        MinecraftForge.EVENT_BUS.register(CombatEntityMarks.INSTANCE);
    }

    @Override
    public void toggleCombat(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            toggleGui(CombatScreen.class, () -> new CombatScreen(entity));
    }


    public void toggleCombatController(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            toggleGui(CombatScreen.class, () -> new CombatScreen(entity));
//            toggleGui(CombatControllerScreen.class, () -> new CombatControllerScreen(entity));
    }

    @Override
    public void openCharEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote)
            FMLClientHandler.instance().displayGuiScreen(Minecraft.getMinecraft().thePlayer, new MorphScreen(entity));
    }

    @Override
    public void toggleStatusEditor(EntityLivingBase entity) {
        if (entity.worldObj.isRemote) {
            final boolean isGm = CharacterAttribute.has(Minecraft.getMinecraft().thePlayer, Trait.gm);
            toggleGui(StatusScreen.class, () -> new StatusScreen(entity, true, isGm));
        }
    }

    @Override
    public void toggleStatus(EntityLivingBase entity) {
        if (entity.worldObj.isRemote) {
            final boolean isGm = CharacterAttribute.has(Minecraft.getMinecraft().thePlayer, Trait.gm);
            toggleGui(StatusScreen.class, () -> new StatusScreen(entity, false, isGm));
        }
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

//    @Override
//    public void toggleDesignerScreen() {
//        if (Minecraft.getMinecraft().theWorld.isRemote)
//            toggleGui(ScreenItemDesigner.class, ScreenItemDesigner::new);
//    }

    @Override
    public void toggleRenamer() {
        if (Minecraft.getMinecraft().theWorld.isRemote)
            toggleGui(ScreenRenamer.class, () -> new ScreenRenamer(Minecraft.getMinecraft().thePlayer));
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
