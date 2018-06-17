package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderUtils;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Rect;

public class Separator extends Widget {
    protected Part horizontalPart = Mellow.THEME.parts.get("separator_hor");

    public Separator() {
        setSizeHint(horizontalPart.size.x, horizontalPart.size.y);
    }

    @Override
    protected void renderSelf() {
        final Rect geom = getGeometry();
        RenderWidgets.beginCropped(this, geom);
        final int endX = geom.x + geom.w;
        final int partWidth = horizontalPart.pos.x / RenderUtils.getScreenScaleFactor() + 1;
        for (int x = geom.x; x < endX; x += partWidth) {
            RenderParts.slice(horizontalPart, x, geom.y);
        }
        RenderWidgets.endCropped();
    }
}
