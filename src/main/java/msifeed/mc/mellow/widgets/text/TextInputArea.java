package msifeed.mc.mellow.widgets.text;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.KeyHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.layout.LayoutUtils;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.text.inner.TextController;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.stream.Stream;

public class TextInputArea extends TextWall implements KeyHandler, MouseHandler.Click {
    protected Part normalPart = Mellow.getPart("sunken");
    protected Part focusedPart = Mellow.getPart("sunken_focused");

    protected TextController controller = new TextController();
    protected long lastTimePressed = 0;

    protected int color = Mellow.getColor("text_dark");
    protected boolean withBackground = true;

    public TextInputArea() {
        setSizeHint(10, 13);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);

        getMargin().set(2, 3, 2, 3);
    }

    public TextController getController() {
        return controller;
    }

    public String getText() {
        return controller.toJoinedString();
    }

    public void setMaxLineWidth(int w) {
        controller.setMaxWidth(w);
        getContentSize().x = w;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    public void setWithBackground(boolean withBackground) {
        this.withBackground = withBackground;
    }

    @Override
    public int getLineSkip() {
        return controller.getLineSkip();
    }

    @Override
    public void setLineSkip(int skip) {
        controller.setLineSkip(skip);
    }

    @Override
    public void setLineLimit(int limit) {
        controller.setSkipLimit(limit);
    }

    @Override
    public int getLineCount() {
        return controller.getLineCount();
    }

    @Override
    public Stream<String> getLines() {
        return controller.toLineStream();
    }

    @Override
    public void setLines(List<String> lines) {
        super.setLines(lines);
        controller.setLines(lines);
    }

    @Override
    public Point getContentSize() {
        return super.getContentSize();
    }

    @Override
    public Geom getTextGeom() {
        return LayoutUtils.getGeomWithMargin(this);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void renderSelf() {
        if (withBackground)
            renderBackground();
        renderText();
        if (isFocused())
            renderCursor();
    }

    protected void renderBackground() {
        RenderParts.nineSlice(isFocused() ? focusedPart : normalPart, getGeometry());
    }

    protected void renderText() {
        final Geom geom = getTextGeom();
        final int color = getColor();
        final int lineHeight = lineHeight();
        getLines().forEach(line -> {
            RenderWidgets.string(geom, line, color);
            geom.y += lineHeight;
        });
    }

    protected void renderCursor() {
        if ((System.currentTimeMillis() - lastTimePressed) % 1000 > 500)
            return;

        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final String subline = controller.getCurrentLine().sb.substring(0, controller.getCurColumn());
        final int lineHeight = lineHeight();

        final Geom cursorGeom = getTextGeom();
        cursorGeom.x += 0;
        cursorGeom.y += 2 + lineHeight * controller.getCurLineView();
        cursorGeom.w = 0;
        cursorGeom.h = lineHeight - 1;
        cursorGeom.translate(fr.getStringWidth(subline), 0, 1);

        RenderShapes.line(cursorGeom, 3, getColor());
    }

    @Override
    public void onKeyboard(char c, int key) {
        switch (c) {
//            case 1:
//                this.setCursorPositionEnd();
//                this.setSelectionPos(0);
//                return true;
            case 3:
                GuiScreen.setClipboardString(controller.toJoinedString());
                return;
            case 22:
                controller.insert(GuiScreen.getClipboardString());
                return;
//            case 24:
//                GuiScreen.setClipboardString(this.getSelectedText());
//
//                if (this.isEnabled) {
//                    this.writeText("");
//                }
//
//                return true;
        }

        final int curLine = controller.getCurLineView();

        switch (key) {
            case Keyboard.KEY_LEFT:
                controller.moveCursorColumn(false);
                break;
            case Keyboard.KEY_RIGHT:
                controller.moveCursorColumn(true);
                break;
            case Keyboard.KEY_UP:
                if (curLine == 0 && controller.getLineSkip() > 0)
                    controller.updateLineSkip(-controller.getSkipLimit());
                controller.moveCursorLine(-1);
                break;
            case Keyboard.KEY_DOWN:
                controller.moveCursorLine(1);
                if (curLine == controller.getSkipLimit() - 1 && controller.getCurLineView() == 0)
                    controller.updateLineSkip(controller.getSkipLimit());
                break;
            case Keyboard.KEY_DELETE:
                controller.remove(true);
                break;
            case Keyboard.KEY_BACK:
                controller.remove(false);
                if (curLine == 0 && controller.getCurLineView() > 0 || getLineSkip() >= getLineCount())
                    controller.updateLineSkip(-controller.getSkipLimit());
                break;
            case Keyboard.KEY_RETURN:
                if (controller.breakLine() && curLine == controller.getSkipLimit() - 1 && controller.getCurLineView() == 0)
                    controller.updateLineSkip(controller.getSkipLimit());
                break;
            default:
                if (controller.insert(c) && curLine == controller.getSkipLimit() - 1 && controller.getCurLineView() == 0)
                    controller.updateLineSkip(controller.getSkipLimit());
                break;
        }

        lastTimePressed = System.currentTimeMillis();
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final Geom geom = getTextGeom();

        final int lineHeight = lineHeight();
        final int line = controller.getLineSkip() + (yMouse - geom.y - lineHeight / 2) / lineHeight;
        final int tune = -1;
        final int column = fr.trimStringToWidth(controller.getLine(line).toString(), xMouse - geom.x - tune).length();

        controller.moveCursor(line, column);
    }
}
