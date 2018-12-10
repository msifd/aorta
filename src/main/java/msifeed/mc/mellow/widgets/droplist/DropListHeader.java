package msifeed.mc.mellow.widgets.droplist;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.AnchorLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

class DropListHeader extends ButtonLabel {
    private Part textPart = Mellow.getPart("dropdown_text");
    private Part downIconPart = Mellow.getPart("dropdown_icon");
    private Part buttonNormalPart = Mellow.getPart("dropdown_button");
    private Part buttonHoverPart = Mellow.getPart("dropdown_button_hover");

    private DropList parent;
    private Geom textBgGeom = new Geom();
    private Geom buttonBgGeom = new Geom();
    private Geom iconGeom = new Geom();

    DropListHeader(DropList parent) {
        this.parent = parent;
        getMargin().set(1, 0, 1, 3);
        setLayout(new AnchorLayout(AnchorLayout.Anchor.LEFT, AnchorLayout.Anchor.CENTER));

        label.setColor(label.darkColor);
    }

    @Override
    public Point getContentSize() {
        final Point p = super.getContentSize();
        p.x = parent.popupList.getContentSize().x + buttonNormalPart.size.x + 4;
        return p;
    }

    @Override
    protected void updateSelf() {
        textBgGeom.set(getGeometry());
        textBgGeom.w -= buttonNormalPart.size.x + 2;

        buttonBgGeom.set(getGeometry());
        buttonBgGeom.x += textBgGeom.w;
        buttonBgGeom.w = buttonNormalPart.size.x;

        iconGeom.set(buttonBgGeom);
        iconGeom.setSize(downIconPart.size);
        iconGeom.translate((buttonNormalPart.size.x - iconGeom.w) / 2, (buttonNormalPart.size.y - iconGeom.h) / 2, 1);
    }

    @Override
    public void render() {
        if (parent.isOpened()) RenderWidgets.toggleCropping();
        super.render();
        if (parent.isOpened()) RenderWidgets.toggleCropping();
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(textPart, textBgGeom);
        if (isHovered() || parent.isOpened())
            RenderParts.nineSlice(buttonHoverPart, buttonBgGeom);
        else
            RenderParts.nineSlice(buttonNormalPart, buttonBgGeom);

        RenderParts.slice(downIconPart, iconGeom);
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        parent.setOpened(!parent.isOpened());
        super.onClick(xMouse, yMouse, button);
    }

    @Override
    protected void onFocusLoss() {
        if (!(focusedWidget instanceof ListEntryButton))
            parent.setOpened(false);
    }

    int getTextBgWidth() {
        return textBgGeom.w;
    }
}
