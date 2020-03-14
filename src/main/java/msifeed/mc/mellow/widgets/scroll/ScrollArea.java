package msifeed.mc.mellow.widgets.scroll;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

import java.util.stream.Stream;

public class ScrollArea extends Widget implements MouseHandler.Wheel {
    private static final int STENCIL_REF = 1 << MinecraftForgeClient.reserveStencilBit();
    private Part scrollbarBgPart = Mellow.getPart("scrollbar_bg");
    private Geom scrollbarBgGeom = new Geom();

    ScrollAreaThumb thumb = new ScrollAreaThumb(this);

    public ScrollArea() {
        setLayout(new ScrollAreaLayout());
        setSizePolicy(SizePolicy.FIXED);

        thumb.getSizeHint().x += 2;
        setDirty();
    }

    @Override
    protected void updateSelf() {
        scrollbarBgGeom.set(thumb.getGeometry());
        scrollbarBgGeom.y = getGeometry().y;
        scrollbarBgGeom.h = getGeometry().h;
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(scrollbarBgPart, scrollbarBgGeom);
    }

    @Override
    protected void renderChildren() {
        GL11.glEnable(GL11.GL_STENCIL_TEST);

        GL11.glStencilMask(0xff);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

        GL11.glColorMask(false, false, false, false);
        GL11.glStencilMask(0xff);
        GL11.glStencilFunc(GL11.GL_ALWAYS, STENCIL_REF, 0xff);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        RenderShapes.rect(getGeometry(), 0, 0xff);

        GL11.glColorMask(true, true, true, true);
        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, STENCIL_REF, 0xff);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);

        super.renderChildren();

        GL11.glDisable(GL11.GL_STENCIL_TEST);

        thumb.render();
    }

    @Override
    public boolean containsAnyChildren(Point p) {
        return getGeometry().contains(p);
    }

    @Override
    public Stream<Widget> getLookupChildren() {
        return Stream.concat(super.getLookupChildren(), Stream.of(thumb));
    }

    @Override
    public void onMouseWheel(int xMouse, int yMouse, int dWheel) {
        thumb.getPos().y -= dWheel / 120 * 8;
        thumb.clampYPos();
    }
}
