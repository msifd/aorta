package msifeed.mc.more.client.combat.other;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.lwjgl.opengl.GL11;

public enum CombatEntityMarks {
    INSTANCE;

    private ResourceLocation icons = new ResourceLocation(Bootstrap.MODID, "textures/gui/combat_icons.png");

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Post event) {
        final EntityPlayer self = Minecraft.getMinecraft().thePlayer;
        if (event.entity == self)
            return;
        if (!CombatAttribute.require(self).phase.isInCombat())
            return;

        CombatAttribute.get(event.entity).ifPresent(ctx -> renderIcons(event, ctx));
    }

    private void renderIcons(RenderLivingEvent.Post event, CombatContext com) {
        final RenderManager renderManager = RenderManager.instance;

        GL11.glPushMatrix();

        GL11.glTranslated(event.x, event.y + event.entity.height + 1.0f, event.z);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        final float f1 = 0.016666668F * 1F;
        GL11.glScalef(-f1, -f1, f1);

        final int texU = com.phase.ordinal() * 32;

        Minecraft.getMinecraft().getTextureManager().bindTexture(icons);
        RenderParts.slice(-16, 0, event.z - 16, 32, 32, texU, 0, 32, 32);

        GL11.glPopMatrix();
    }
}
