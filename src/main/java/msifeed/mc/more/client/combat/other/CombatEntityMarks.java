package msifeed.mc.more.client.combat.other;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public enum CombatEntityMarks {
    INSTANCE;

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Post event) {
        final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
        if (event.entity == self) return;

        CombatAttribute.get(event.entity).ifPresent(ctx -> {
            renderLines(event, Lists.newArrayList("combatant - " + ctx.stage.toString()));
        });
    }

    private void renderLines(RenderLivingEvent.Post event, List<String> lines) {
        final RenderManager renderManager = RenderManager.instance;
        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;


        GL11.glPushMatrix();

        GL11.glTranslated(event.x, event.y + event.entity.height + 1F, event.z);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        final float f1 = 0.016666668F * 1.6F;
        GL11.glScalef(-f1, -f1, f1);

//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);
        final int xOffset = maxWidth + maxWidth / 2;

        int yOffset = 0;
        for (String l : lines) {
            fr.drawString(l, -xOffset, yOffset, Color.white.getRGB());
            yOffset += fr.FONT_HEIGHT;
        }


//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_BLEND);
//        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//
//        final double icon_half = 5;
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.setColorRGBA_I(color, 200);
//        tessellator.addVertex(-icon_half, -icon_half, 0.0D);
//        tessellator.addVertex(-icon_half, icon_half, 0.0D);
//        tessellator.addVertex(icon_half, icon_half, 0.0D);
//        tessellator.addVertex(icon_half, -icon_half, 0.0D);
//        tessellator.draw();
//
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }
}
