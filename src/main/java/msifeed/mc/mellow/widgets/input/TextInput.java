package msifeed.mc.mellow.widgets.input;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.handlers.MouseHandler;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

public class TextInput extends Widget implements MouseHandler.All {
    private static final int CURSOR_BLINK_MS = 500;

    private Part normalPart = Mellow.THEME.parts.get("sunken");
    private Part focusedPart = Mellow.THEME.parts.get("sunken_focused");
    private int darkColor = Mellow.THEME.colors.get("text_dark");

    private String text = "";

//    public Function<String, Boolean> validateText = s -> true;
//    public Consumer<String> onUnfocus = s -> {};
//    public boolean centerByWidth = false;

    private int cursorPos = 0;
//    private int scrollOffset = 0;
//    private int frameCounter = 0; // For cursor blinking
//
//    private int pressedKey = -1;
//    private long pressedTime = 0; // For key repeating
//
//    private boolean wasInFocus; // For onUnfocus event

    public TextInput() {
        getSizeHint().set(30, 10);
        setSizePolicy(SizePolicy.FIXED);
    }

    @Override
    public void renderSelf() {
        renderBackground();
        renderText();
    }

    protected void renderBackground() {
        if (isFocused())
            RenderParts.nineSlice(focusedPart, getGeometry());
        else
            RenderParts.nineSlice(normalPart, getGeometry());
    }

    protected void renderText() {
        RenderWidgets.string(getGeometry(), text, darkColor);
    }

    protected void renderCursor() {
        RenderWidgets.string(getGeometry(), text, darkColor);
    }

    @Override
    public void onPress(int xMouse, int yMouse, int button) {

    }

    @Override
    public void onMove(int xMouse, int yMouse, int button) {

    }

    @Override
    public void onRelease(int xMouse, int yMouse, int button) {

    }

    @Override
    public void onClick(int xMouse, int yMouse, int button) {

    }
}
