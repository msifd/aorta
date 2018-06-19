package msifeed.mc.mellow.widgets;

import com.google.common.collect.ImmutableList;
import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Rect;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DropDown extends Widget {
    private DropButton header = new DropButton(this);
    private DropList list = new DropList(this);
    private boolean toggled = false;

    private final List<String> items;
    private int selectedItem;

    public DropDown(List<String> items) {
        this.items = items;
        selectItem(0);

        setSizeHint(header.getSizeHint());
        setSizePolicy(header.getSizePolicy());
        setLayout(VerticalLayout.INSTANCE);

        this.list.visible = false;

        addChild(header);
        addChild(list);
    }

    public void selectItem(int i) {
        if (i < items.size()) {
            selectedItem = i;
            header.setLabel(getSelectedItem());
        }
    }

    public String getSelectedItem() {
        return items.get(selectedItem);
    }

    protected static class DropButton extends Button {
        protected Part textPart = Mellow.THEME.parts.get("dropdown_text");
        protected Part downIconPart = Mellow.THEME.parts.get("dropdown_icon");
        protected Part buttonNormalPart = Mellow.THEME.parts.get("dropdown_button");
        protected Part buttonHoverPart = Mellow.THEME.parts.get("dropdown_button_hover");
        protected final DropDown parent;

        protected Rect textGeom = new Rect();
        protected Rect buttonGeom = new Rect();
        protected Rect iconGeom = new Rect();

        public DropButton(DropDown parent) {
            this.parent = parent;
            setSizeHint(100, 11);
            setSizePolicy(SizePolicy.Policy.PREFERRED, SizePolicy.Policy.MAXIMUM);
            setLayout(FloatLayout.INSTANCE);
            getMargin().set(1, 0, 0, 2);
        }

        @Override
        protected void updateSelf() {
            textGeom.set(getGeometry());
            textGeom.w -= buttonNormalPart.size.x + 2;

            buttonGeom.set(getGeometry());
            buttonGeom.x += textGeom.w;
            buttonGeom.w = buttonNormalPart.size.x;

            iconGeom.setPos(buttonGeom.x, buttonGeom.y);
            iconGeom.setSize(downIconPart.size);
            iconGeom.translate(buttonNormalPart.size.x / 2, buttonNormalPart.size.y / 2);
            iconGeom.translate(-iconGeom.w / 2, -iconGeom.h / 2 - 1);
        }

        @Override
        protected void renderBackground() {
            RenderParts.nineSlice(textPart, textGeom);
            if (parent.toggled || isHovered())
                RenderParts.nineSlice(buttonHoverPart, buttonGeom);
            else
                RenderParts.nineSlice(buttonNormalPart, buttonGeom);
        }

        @Override
        protected void renderLabel() {
            super.renderLabel();
            RenderParts.slice(downIconPart, iconGeom.x, iconGeom.y);
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
