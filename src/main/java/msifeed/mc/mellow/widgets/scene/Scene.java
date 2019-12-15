package msifeed.mc.mellow.widgets.scene;

import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Optional;

public class Scene extends Widget {

    public Scene() {
        setLayout(new AnchorLayout());
    }

    public Optional<Widget> lookupWidget(Point p, Class<?> c) {
        return fullLookup(p, c);
    }

    private Optional<Widget> fullLookup(Point p, Class<?> type) {
        // Debug list
//        GL11.glPushMatrix();
//        GL11.glScalef(0.5f, 0.5f, 0.5f);
//        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
//        int y = 5;
//        for (Widget w : active.stream().filter(widget -> widget.containsPoint(p)).sorted(Widget::isHigherThan).collect(Collectors.toList())) {
//            final String s = String.format("%s (z: %d, d: %d)", w.toString(), w.getGeometry().z, w.getWidgetTreeDepth());
//            fr.drawString(s, 5, y, 0xffffff);
//            y += fr.FONT_HEIGHT + 2;
//        }
//        GL11.glPopMatrix();
        //

        return getWidgetsAtPoint(p, type).max(Widget::isHigherThan);
    }
}
