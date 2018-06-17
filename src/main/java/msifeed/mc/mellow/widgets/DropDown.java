package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;

public class DropDown extends Widget {
    protected DropButton header = new DropButton(this);
    protected DropList list = new DropList(this);
    protected boolean toggled = false;

    public DropDown() {
        this.list.visible = false;

        addChild(header);
        addChild(list);
    }

    protected static class DropButton extends Button {
        protected Part textPart = Mellow.THEME.parts.get("dropdown_text");
        protected Part buttonNormalPart = Mellow.THEME.parts.get("dropdown_button");
        protected Part buttonHoverPart = Mellow.THEME.parts.get("dropdown_button_hover");
        protected final DropDown parent;

        protected Icon downIcon = new Icon("dropdown_icon");

        public DropButton(DropDown parent) {
//            super(new Icon("dropdown_icon"));
            this.parent = parent;
        }

        @Override
        protected void renderBackground() {
            RenderParts.nineSlice(textPart, getGeometry());
            if (parent.toggled || isHovered())
                RenderParts.nineSlice(buttonHoverPart, getGeometry());
            else
                RenderParts.nineSlice(buttonNormalPart, getGeometry());
        }

        @Override
        protected void renderLabel() {
            super.renderLabel();
            // TODO: draw label
        }
    }

    protected static class DropList extends Widget {
        protected Part listPart = Mellow.THEME.parts.get("dropdown_list");
        protected final DropDown parent;

        public DropList(DropDown parent) {
            this.parent = parent;
        }
    }
}
