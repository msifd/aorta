package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.KeyHandler;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;
import java.util.function.Function;

public class TextInput extends Widget implements KeyHandler {
    protected Part normalPart = Mellow.THEME.parts.get("sunken");
    protected Part focusedPart = Mellow.THEME.parts.get("sunken_focused");
    protected int darkColor = Mellow.THEME.colors.get("text_dark");

    private Function<String, Boolean> filter = s -> true;
    private Consumer<String> onChange = s -> {};

    private String text = "";
    private int cursor = text.length();

    public TextInput() {
        setSizeHint(10, 13);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
    }

    public void setText(String text) {
        this.text = text;
        this.cursor = text.length();
        notifyChange();
    }

    public String getText() {
        return text;
    }

    public void setFilter(Function<String, Boolean> filter) {
        this.filter = filter;
    }

    public void setCallback(Consumer<String> callback) {
        this.onChange = callback;
    }

    @Override
    protected void renderSelf() {
        try {
            renderBackground();
            renderText();
            if (isFocused())
                renderCursor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void renderBackground() {
        RenderParts.nineSlice(isFocused() ? focusedPart : normalPart, getGeometry());
    }

    protected void renderText() {
        final Geom geom = getGeometry();
        final Geom textGeom = new Geom(geom);
        textGeom.translate(3, 2);
        RenderWidgets.string(textGeom, text, darkColor);
    }

    protected void renderCursor() {
        if (System.currentTimeMillis() % 1000 < 500)
            return;

        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        final Geom geom = getGeometry();
        final Geom cursorGeom = new Geom(geom.x, geom.y, 2, fr.FONT_HEIGHT - 2);
        cursorGeom.translate(3 + fr.getStringWidth(text.substring(0, cursor)), 3);
        if (cursorGeom.x - geom.x > geom.w - 3)
            return;

        RenderShapes.rect(cursorGeom, darkColor, 0xdd);
    }

    @Override
    public void onKeyboard(char c, int key) {
        switch (key) {
            case Keyboard.KEY_LEFT:
                moveCursor(-1);
                break;
            case Keyboard.KEY_RIGHT:
                moveCursor(1);
                break;
            case Keyboard.KEY_DELETE:
                removeChar(0);
                break;
            case Keyboard.KEY_BACK:
                removeChar(-1);
                break;
            case Keyboard.KEY_RETURN:
                break;
            default:
                if (c != 0)
                    putChar(c);
        }
    }

    private void moveCursor(int delta) {
        final int target = cursor + delta;
        cursor = MathHelper.clamp_int(target, 0, text.length());
    }

    private void putChar(char c) {
        final boolean hasCharsOnRight = cursor < text.length();
        final String s = text.substring(0, cursor) + c + (hasCharsOnRight ? text.substring(cursor) : "");
        tryChange(s, 1);
    }

    private void removeChar(int delta) {
        final int target = cursor + delta;
        if (target < 0 || target >= text.length())
            return;

        final String s;
        if (target == 0)
            s = text.substring(1);
        else if (target == text.length() - 1)
            s = text.substring(0, text.length() - 1);
        else
            s = text.substring(0, target) + text.substring(target + 1);
        tryChange(s, delta);
    }

    private void tryChange(String s, int cursorDelta) {
        if (filter == null || filter.apply(s)) {
            text = s;
            moveCursor(cursorDelta);
            notifyChange();
        }
    }

    private void notifyChange() {
        if (onChange != null)
            onChange.accept(text);
    }

    public static boolean isDigitPart(String s) {
        if (s.isEmpty() || s.equals("-"))
            return true;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
