package msifeed.mc.aorta.core.client;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class DisableVanillaHud {
    public static final DisableVanillaHud INSTANCE = new DisableVanillaHud();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        switch (event.type) {
            case HEALTH:
//                renderHud(event);
            case ARMOR:
            case FOOD:
            case EXPERIENCE:
                event.setCanceled(true);
                break;
        }
    }

    private static void renderHud(RenderGameOverlayEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = mc.fontRenderer;

        mc.mcProfiler.startSection("health");
        GL11.glEnable(GL11.GL_BLEND);




        final String line = "foobar";
        final int lineWidth = fr.getStringWidth(line);

        final int y = event.resolution.getScaledHeight() / 2;
        final int x = (event.resolution.getScaledWidth() - lineWidth) / 2;

        fr.drawString(line, x, y, 0xffffff);

        GL11.glDisable(GL11.GL_BLEND);
        mc.mcProfiler.endSection();
    }
}
