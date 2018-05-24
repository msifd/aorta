package msifeed.mc.mellow.events;

import msifeed.mc.mellow.Widget;

public abstract class MouseEvent extends WidgetEvent {
    public final int xPos;
    public final int yPos;
    public final int button;

    public MouseEvent(Widget target, int xPos, int yPos, int button) {
        super(target);
        this.xPos = xPos;
        this.yPos = yPos;
        this.button = button;
    }

    public static class Click extends MouseEvent {
        public Click(Widget target, int xPos, int yPos, int button) {
            super(target, xPos, yPos, button);
        }
    }
}
