package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.layout.FloatLayout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.List;
import java.util.function.Consumer;

public class DropDown<T> extends Widget {
    private final DropButton header = new DropButton(this);
    private final DropList list;
    private final List<T> items;
    private int selectedItem;
    private boolean opened = false;

    protected Consumer<T> selectCallback = null;

    public DropDown(List<T> items) {
        this.items = items;
        this.list = new DropList(this);
        selectItem(0);

        setSizeHint(header.getSizeHint());
        setSizePolicy(header.getSizePolicy());
        setLayout(new VerticalLayout(0));

        this.list.visible = false;

        addChild(header);
        addChild(list);
    }

    public void selectItem(int i) {
        if (i < items.size()) {
            selectedItem = i;
            header.setLabel(getSelectedItem().toString());
            if (selectCallback != null)
                selectCallback.accept(items.get(i));
        }
    }

    public T getSelectedItem() {
        return items.get(selectedItem);
    }

    public void setSelectCallback(Consumer<T> callback) {
        this.selectCallback = callback;
    }

    @Override
    public boolean containsPoint(Point p) {
        return super.containsPoint(p) || list.containsPoint(p);
    }

    private static class DropButton extends Button {
        final DropDown parent;
        Part textPart = Mellow.THEME.parts.get("dropdown_text");
        Part downIconPart = Mellow.THEME.parts.get("dropdown_icon");
        Part buttonNormalPart = Mellow.THEME.parts.get("dropdown_button");
        Part buttonHoverPart = Mellow.THEME.parts.get("dropdown_button_hover");
        Geom textGeom = new Geom();
        Geom buttonGeom = new Geom();
        Geom iconGeom = new Geom();

        DropButton(DropDown parent) {
            this.parent = parent;
            setSizeHint(100, 11);
            setSizePolicy(SizePolicy.Policy.PREFERRED, SizePolicy.Policy.MAXIMUM);
            setLayout(FloatLayout.INSTANCE);
            getMargin().set(1, 0, 0, 3);

            if (label instanceof Label)
                ((Label) label).setColor(((Label) label).darkColor);
        }

        @Override
        protected void updateSelf() {
            textGeom.set(getGeometry());
            textGeom.w = textBarWidth();

            buttonGeom.set(getGeometry());
            buttonGeom.x += textGeom.w;
            buttonGeom.w = buttonNormalPart.size.x;

            iconGeom.set(buttonGeom);
            iconGeom.setSize(downIconPart.size);
            iconGeom.translate((buttonNormalPart.size.x - iconGeom.w) / 2, (buttonNormalPart.size.y - iconGeom.h) / 2);
        }

        @Override
        protected void renderSelf() {
            RenderParts.nineSlice(textPart, textGeom);
            if (isHovered() || parent.opened)
                RenderParts.nineSlice(buttonHoverPart, buttonGeom);
            else
                RenderParts.nineSlice(buttonNormalPart, buttonGeom);

            RenderParts.slice(downIconPart, iconGeom.x, iconGeom.y);
        }

        @Override
        public void onClick(int xMouse, int yMouse, int button) {
            parent.opened = !parent.opened;
            super.onClick(xMouse, yMouse, button);
        }

        @Override
        protected void onFocusLoss() {
            if (!(focusedWidget instanceof ListButton))
                parent.opened = false;
        }

        private int textBarWidth() {
            return parent.getGeometry().w - parent.header.buttonNormalPart.size.x - 2;
        }
    }

    private static class DropList extends Widget {
        final DropDown parent;
        Part listPart = Mellow.THEME.parts.get("dropdown_list");

        DropList(DropDown parent) {
            this.parent = parent;
            setPos(1, 0);
            setZLevel(10);
            getMargin().set(1);
            setSizeHint(10, parent.items.size() * 11 + 2);
            setSizePolicy(SizePolicy.Policy.FIXED, SizePolicy.Policy.FIXED);
            setLayout(VerticalLayout.INSTANCE);

            for (int i = 0; i < parent.items.size(); i++) {
                addChild(new ListButton(parent, i));
            }
        }

        @Override
        public Point getLayoutSizeHint() {
            final Point p = super.getLayoutSizeHint();
            p.x = parent.header.textBarWidth() - 2;
            return p;
        }

        @Override
        public boolean isVisible() {
            return parent.opened;
        }

        @Override
        protected void renderSelf() {
            RenderParts.nineSlice(listPart, getGeometry());
        }
    }

    private static class ListButton extends Button {
        final DropDown parent;
        final int itemN;

        ListButton(DropDown parent, int n) {
            this.parent = parent;
            this.itemN = n;
            setZLevel(1);
            getMargin().set(2, 0);
            setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));
            setLabel(parent.items.get(n).toString());
            setClickCallback(() -> {
                parent.selectItem(itemN);
                parent.opened = false;
            });

            if (label instanceof Label)
                ((Label) label).setColor(((Label) label).darkColor);
        }

        @Override
        protected void renderSelf() {
            if (isHovered())
                RenderShapes.rect(getGeometry(), 0xc3a38a, 255);
        }
    }
}
