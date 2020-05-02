package msifeed.mc.mellow.widgets.text;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.KeyHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.render.RenderUtils;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.inner.TextController;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.stream.Stream;

public class TextInputArea extends Widget implements KeyHandler, MouseHandler.Click {
    protected Part normalPart = Mellow.getPart("sunken");
    protected Part focusedPart = Mellow.getPart("sunken_focused");

    protected final NavMode mode;
    protected TextController controller = new TextController();
    protected long lastTimePressed = 0;

    protected int color = Mellow.getColor("text_dark");
    protected boolean withBackground = true;

    public TextInputArea(NavMode mode) {
        this.mode = mode;

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

    public void setWithBackground(boolean withBackground) {
        this.withBackground = withBackground;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLineSkip() {
        return controller.getOffsetLine();
    }

    public void updateLineSkip(int delta) {
        controller.updateOffsetLine(delta);
    }

    public void setLineLimit(int limit) {
        controller.setLinesPerView(limit);
    }

    public void setMaxLineWidth(int w) {
        controller.setMaxWidth(w);
    }

    public int getLineCount() {
        return controller.getLineCount();
    }

    public Stream<String> toLineStream() {
        return controller.toLineStream();
    }

    public void setLines(List<String> lines) {
        controller.setLines(lines);
    }

    @Override
    protected void updateSelf() {
        controller.setViewWidth(getGeometry().w - getMargin().horizontal());
        controller.setViewHeight(getGeometry().h - getMargin().vertical());
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
        final Geom geom = this.getGeomWithMargin();
        geom.z += 1;

        final int color = getColor();
        final int lineHeight = RenderWidgets.lineHeight();

        controller.viewLineStream().forEach(line -> {
            RenderWidgets.string(geom, line, color);
            geom.y += lineHeight;
        });
    }

    protected void renderCursor() {
        if ((System.currentTimeMillis() - lastTimePressed) % 1000 > 500)
            return;

        final int lineHeight = RenderWidgets.lineHeight();

        final Geom cursorGeom = this.getGeomWithMargin();
        cursorGeom.translate(controller.getCursorXOffset(), 2 + lineHeight * getCursorLineInView());
        cursorGeom.setSize(0, lineHeight - 1);

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

        final int curLine = getCursorLineInView();

        switch (key) {
            case Keyboard.KEY_LEFT:
                controller.moveCursorColumn(false);
                break;
            case Keyboard.KEY_RIGHT:
                controller.moveCursorColumn(true);
                break;
            case Keyboard.KEY_UP:
                if (curLine == 0 && controller.getOffsetLine() > 0)
                    moveOffsetLine(-1);
                controller.moveCursorLine(-1);
                break;
            case Keyboard.KEY_DOWN:
                controller.moveCursorLine(1);
                if (curLine == controller.getLinesPerView() - 1 && curLine + 1 < controller.getLineCount())
                    moveOffsetLine(1);
                break;
            case Keyboard.KEY_DELETE:
                controller.remove(true);
                break;
            case Keyboard.KEY_BACK:
                controller.remove(false);
                if (curLine == 0 && getCursorLineInView() > 0 || getLineSkip() >= getLineCount())
                    moveOffsetLine(-1);
                break;
            case Keyboard.KEY_RETURN:
                if (controller.breakLine() && curLine == controller.getLinesPerView() - 1 && curLine + 1 < controller.getLineCount())
                    moveOffsetLine(1);
                break;
            case Keyboard.KEY_HOME:
                controller.setCursor(controller.getCurLine(), 0);
                break;
            case Keyboard.KEY_END:
                controller.setCursor(controller.getCurLine(), controller.getCurrentLine().columns);
                break;
            case Keyboard.KEY_PRIOR: // Page Up
                if (mode == NavMode.PAGES)
                    controller.moveCursorLine(-(controller.getLinesPerView() + getCursorLineInView()));
                else
                    controller.moveCursorLine(-controller.getLinesPerView());
                break;
            case Keyboard.KEY_NEXT: // Page Down
                if (mode == NavMode.PAGES)
                    controller.moveCursorLine(controller.getLinesPerView() - getCursorLineInView());
                else
                    controller.moveCursorLine(controller.getLinesPerView());
                break;
            default:
                controller.insert(c);
//                if (controller.insert(c) && curLine == controller.getLinesPerView() - 1 && curLine + 1 < controller.getLineCount())
//                    moveOffsetLine(1);
                break;
        }

        lastTimePressed = System.currentTimeMillis();
    }

    private void moveOffsetLine(int units) {
        if (mode == NavMode.PAGES)
            units *= controller.getLinesPerView();
        controller.updateOffsetLine(units);
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final Geom geom = this.getGeomWithMargin();

        final int inAreaXPos = xMouse - geom.x;
        final int inAreaYPos = yMouse - geom.y;
        final int tune = 1;

        final int line = controller.getOffsetLine() + inAreaYPos / RenderWidgets.lineHeight();

        final String visibleLine = controller.getLine(line).sb.substring(controller.getOffsetColumn());
        final int column = controller.getOffsetColumn() + fr.trimStringToWidth(visibleLine, inAreaXPos + tune).length();

        controller.setCursor(line, column);
    }

    private int getCursorLineInView() {
        if (mode == NavMode.LINES)
            return controller.getCurLine() - controller.getOffsetLine();
        else
            return controller.getCurLine() % controller.getLinesPerView();
    }

    public enum NavMode {
        LINES, PAGES
    }
}
