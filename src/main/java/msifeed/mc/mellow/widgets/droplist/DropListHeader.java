package msifeed.mc.mellow.widgets.droplist;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.button.ButtonLabel;

class DropListHeader extends ButtonLabel {
    private Part textPart = Mellow.THEME.parts.get("dropdown_text");
    private Part downIconPart = Mellow.THEME.parts.get("dropdown_icon");
    private Part buttonNormalPart = Mellow.THEME.parts.get("dropdown_button");
    private Part buttonHoverPart = Mellow.THEME.parts.get("dropdown_button_hover");

    private DropList parent;
    private Geom textBgGeom = new Geom();
    private Geom buttonBgGeom = new Geom();
    private Geom iconGeom = new Geom();

    DropListHeader(DropList parent) {
        this.parent = parent;
        setSizeHint(100, 11);
        setVerSizePolicy(SizePolicy.Policy.MINIMUM);
        setLayout(FreeLayout.INSTANCE);
        getMargin().set(1, 0, 0, 3);

        label.setColor(label.darkColor);
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
    protected void renderSelf() {
        RenderParts.nineSlice(textPart, textBgGeom);
        if (isHovered() || parent.isOpened())
            RenderParts.nineSlice(buttonHoverPart, buttonBgGeom);
        else
            RenderParts.nineSlice(buttonNormalPart, buttonBgGeom);

        RenderParts.slice(downIconPart, iconGeom.x, iconGeom.y);
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
