package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Offset;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.widgets.Widget;

public class AnchorLayout extends Layout {
    private Anchor horAnchor;
    private Anchor verAnchor;

    public AnchorLayout(Anchor both) {
        this.horAnchor = both;
        this.verAnchor = both;
    }

    public AnchorLayout(Anchor hor, Anchor ver) {
        this.horAnchor = hor;
        this.verAnchor = ver;
    }

    @Override
    public void update() {
        final Rect hostBounds = host.getBounds();
        hostBounds.offset(host.getPadding());

        for (Widget w : host.getChildren()) {
            setAnchoredBounds(w, hostBounds);
        }
    }

    private void setAnchoredBounds(Widget w, Rect hostBounds) {
        final Rect result = new Rect();
        final Offset margin = w.getMargin();

        result.translate(margin.left, margin.top); // ???
        result.translate(hostBounds);
        result.resize(w.getMinSize());

        switch (verAnchor) {
            case CENTER:
                result.y += (hostBounds.h - result.h) / 2;
                break;
            case TOP:
                break;
            default:
                break;
        }

        switch (horAnchor) {
            case CENTER:
                result.x += (hostBounds.w - result.w) / 2;
                break;
            case LEFT:
                break;
            default:
                break;
        }

        w.setBounds(result);
    }

    public enum Anchor {
        CENTER, TOP, LEFT
    }
}
