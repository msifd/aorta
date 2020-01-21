package msifeed.mc.extensions.locks.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.extensions.locks.LockObject;
import msifeed.mc.extensions.locks.LockType;
import msifeed.mc.extensions.locks.items.LockItem;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.EnumMap;

public class HudLock extends Gui {
    public static final HudLock INSTANCE = new HudLock();

    private EnumMap<LockType, ResourceLocation> lockIcons = new EnumMap<>(LockType.class);

    private HudLock() {
        LockType.locks().forEach(t -> {
            final String s = "textures/items/" + LockItem.getItemId(t) + ".png";
            lockIcons.put(t, new ResourceLocation("aorta", s));
        });
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
            return;

        final Minecraft mc = Minecraft.getMinecraft();
        final MovingObjectPosition mop = mc.objectMouseOver;
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return;

        final LockObject lock = LockObject.find(mc.theWorld, mop.blockX, mop.blockY, mop.blockZ);
        if (lock == null || !lock.hasLock())
            return;

        final int txSize = 32;
        final int size = 16;
        final int x = (event.resolution.getScaledWidth() + size) / 2;
        final int y = (event.resolution.getScaledHeight() - size) / 2;

        mc.getTextureManager().bindTexture(lockIcons.get(lock.getLockType()));
        drawSprite(x, y, size, txSize);

        final FontRenderer fr = mc.fontRenderer;
        final String diffStr = String.valueOf(lock.getDifficulty());
        final int diffStrWidth = fr.getStringWidth(diffStr);
        drawString(fr, diffStr, x + (size - diffStrWidth) / 2 + 1, y + size, 0xaaaaaaaa);

        if (lock.getLockType() == LockType.PADLOCK) {
            String statusText = L10n.tr(lock.isLocked() ? "aorta.lock.locked" : "aorta.lock.unlocked");
            drawString(fr, statusText, x + size + 2, y + (size - fr.FONT_HEIGHT) / 2, 0xaaaaaaaa);
        }
    }

    private void drawSprite(double x, double y, double s, double ts) {
        // 32:32 sprite aspect ratio
        final double f = 0.03125;
        final double f1 = 0.03125;
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + s, 1, 0, ts * f1);
        tessellator.addVertexWithUV(x + s, y + s, 1, ts * f, ts * f1);
        tessellator.addVertexWithUV(x + s, y + 0, 1, ts * f, 0);
        tessellator.addVertexWithUV(x + 0, y + 0, 1, 0, 0);
        tessellator.draw();
    }
}
