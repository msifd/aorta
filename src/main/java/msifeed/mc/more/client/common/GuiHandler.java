package msifeed.mc.more.client.common;

import msifeed.mc.extensions.locks.LockObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class GuiHandler {
    public void init() {}
    public void toggleCombat(EntityLivingBase entity) { }
    public void toggleCombatController(EntityLivingBase entity) { }
    public void openCharEditor(EntityLivingBase entity) { }
    public void toggleStatusEditor(EntityLivingBase entity) { }
    public void toggleStatus(EntityLivingBase entity) { }
    public void toggleBookViewer(EntityPlayer player) { }
    public void toggleBookLoader(EntityPlayer player) { }
    public void toggleBookEditor(EntityPlayer player) { }
    public void toggleDigitalLock(LockObject lock) { }
    public void toggleSkeletalKey(LockObject lock) { }
    public void toggleDesignerScreen() { }
    public void toggleRenamer() { }
}
