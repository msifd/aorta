package msifeed.mc.more.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.tools.ItemDebugTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public enum CombatOverlay {
    INSTANCE;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
//        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = RenderManager.instance.getFontRenderer();

        if (fr == null)
            return;

        final EntityLivingBase entity = getEntity();

        final ArrayList<String> lines = new ArrayList<>();
        lines.add("[debug]");
        lines.add("Entity: " + entity.getCommandSenderName());
//        addCharProps(lines, entity);
//        addLang(lines, entity);

        GL11.glPushMatrix();
//        GL11.glScalef(0.5f, 0.5f, 0);
        int y = 5;
        for (String l : lines) {
            fr.drawString(l, 5, y, 0xed5050);
            y += fr.FONT_HEIGHT;
        }
        GL11.glPopMatrix();
    }

    private static EntityLivingBase getEntity() {
        final Minecraft mc = Minecraft.getMinecraft();
        final MovingObjectPosition mop = mc.objectMouseOver;
        if (mop != null
            && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
            && mop.entityHit instanceof EntityLivingBase)
            return (EntityLivingBase) mop.entityHit;
        return mc.thePlayer;
    }
}
