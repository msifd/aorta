package msifeed.mc.mellow.widgets.text;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.KeyHandler;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
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

    private TextController controller = new TextController();
    private long lastTimePressed = 0;

    protected int color = Mellow.getColor("text_dark");

    public TextInputArea() {
        setSizeHint(10, 13);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    public TextController getController() {
        return controller;
    }

    public String getText() {
        return controller.toJoinedString();
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getLineSkip() {
        return controller.getSkip();
    }

    @Override
    public void setLineSkip(int skip) {
        controller.setSkip(skip);
    }

    @Override
    public void setLineLimit(int limit) {
        controller.setLimit(limit);
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
    protected void renderSelf() {
        super.renderSelf();
        if (isFocused())
            renderCursor();
    }

    protected void renderBackground() {
        RenderParts.nineSlice(isFocused() ? focusedPart : normalPart, getGeometry());
    }

    protected void renderCursor() {
        if ((System.currentTimeMillis() - lastTimePressed) % 1000 > 500)
            return;

        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final String subline = controller.getCurrentLine().sb.substring(0, controller.getCurColumn());
        final int lineHeight = lineHeight();

        final Geom cursorGeom = new Geom(getGeometry());
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
                if (curLine == 0 && controller.getSkip() > 0)
                        controller.updateSkip(-controller.getLimit());
                controller.moveCursorLine(-1);
                break;
            case Keyboard.KEY_DOWN:
                controller.moveCursorLine(1);
                if (curLine == controller.getLimit() - 1 && controller.getCurLineView() == 0)
                    controller.updateSkip(controller.getLimit());
                break;
            case Keyboard.KEY_DELETE:
                controller.remove(true);
                break;
            case Keyboard.KEY_BACK:
                controller.remove(false);
                if (curLine == 0 && controller.getCurLineView() > 0)
                    controller.updateSkip(-controller.getLimit());
                break;
            case Keyboard.KEY_RETURN:
                controller.breakLine();
                if (curLine == controller.getLimit() - 1 && controller.getCurLineView() == 0)
                    controller.updateSkip(controller.getLimit());
                break;
            default:
                controller.insert(c);
                if (curLine == controller.getLimit() - 1 && controller.getCurLineView() == 0)
                    controller.updateSkip(controller.getLimit());
                break;
        }

        lastTimePressed = System.currentTimeMillis();
    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final Geom geom = getGeometry();

        final int lineHeight = lineHeight();
        final int line = controller.getSkip() + (yMouse - geom.y - lineHeight / 2) / lineHeight;
        final int tune = -1;
        final int column = fr.trimStringToWidth(controller.getLine(line).toString(), xMouse - geom.x - tune).length();

        controller.moveCursor(line, column);
    }
}
