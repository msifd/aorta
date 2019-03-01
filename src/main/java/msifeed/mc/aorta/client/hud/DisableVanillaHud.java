package msifeed.mc.aorta.client.hud;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.aorta.core.traits.Trait;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public class DisableVanillaHud {
    public static final DisableVanillaHud INSTANCE = new DisableVanillaHud();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event) {
        switch (event.type) {
            case HEALTH:
            case ARMOR:
            case FOOD:
            case EXPERIENCE:
                event.setCanceled(true);
                break;
        }
    }

    @SubscribeEvent
    public void onRenderTextOverlay(RenderGameOverlayEvent.Text event) {
        renderHud(event);
    }

    private static void renderHud(RenderGameOverlayEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = mc.fontRenderer;

        final EntityPlayer player = mc.thePlayer;
        final Optional<CharStatus> optStatus = StatusAttribute.get(player);
        if (!optStatus.isPresent())
            return;
        final CharStatus status = optStatus.get();

        mc.mcProfiler.startSection("health");
        GL11.glEnable(GL11.GL_BLEND);

        String statusText = "sanity: " + status.sanity;
        if (CharacterAttribute.has(player, Trait.psionic))
            statusText += " PSI: " + status.psionics;

        final int lineWidth = fr.getStringWidth(statusText);
        final int x = event.resolution.getScaledWidth() - lineWidth - 2;
        final int y = 2;

        fr.drawStringWithShadow(statusText, x, y, 0xeeeeee);

        GL11.glDisable(GL11.GL_BLEND);
        mc.mcProfiler.endSection();
    }
}
