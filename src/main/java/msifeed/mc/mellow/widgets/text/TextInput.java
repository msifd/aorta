package msifeed.mc.mellow.widgets.text;

import msifeed.mc.mellow.render.RenderWidgets;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

public class TextInput extends TextInputArea {
    private Function<String, Boolean> filter = s -> true;
    private Consumer<String> onChange = s -> {};

    private String placeholderText = "";

    public TextInput() {
        super(NavMode.LINES);

        setSizeHint(10, 12);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
        getMargin().set(2, 2, 0, 3);

        controller.setMaxLines(1);
    }

    public boolean isEmpty() {
        return controller.isEmpty();
    }

    public String getText() {
        return controller.getCurrentLine().sb.toString();
    }

    public void setText(String text) {
        controller.setLines(Collections.singletonList(text));
        notifyChange();
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public void setPlaceholderText(String text) {
        this.placeholderText = text;
    }

    public void setFilter(Function<String, Boolean> filter) {
        this.filter = filter;
    }

    public void setCallback(Consumer<String> callback) {
        this.onChange = callback;
    }

    @Override
    protected void renderText() {
        if (isEmpty()) {
            final Geom geom = this.getGeomWithMargin();
            geom.z += 1;
            RenderWidgets.string(geom, getPlaceholderText(), getColor() * 2);
        } else
            super.renderText();
    }

    @Override
    public void onKeyboard(char c, int key) {
        final String before = getText();
        final int cc = controller.getCurColumn();

        super.onKeyboard(c, key);
        if (filter == null || filter.apply(getText()))
            notifyChange();
        else {
            setText(before);
            controller.setCursor(0, cc);
        }
    }

    private void notifyChange() {
        if (onChange != null)
            onChange.accept(getText());
    }

    public int getInt() {
        if (isEmpty())
            return 0;
        try {
            return Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public float getFloat() {
        if (isEmpty())
            return 0;
        try {
            return Float.parseFloat(getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public static boolean isUnsignedInt(String s) {
        if (s.isEmpty())
            return true;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isUnsignedIntBetween(String s, int min, int max) {
        if (s.isEmpty())
            return true;
        try {
            final int i = Integer.parseInt(s);
            return i >= min && i <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isSignedInt(String s) {
        if (s.isEmpty() || s.equals("-"))
            return true;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isSignedIntBetween(String s, int min, int max) {
        if (s.isEmpty() || s.equals("-"))
            return true;
        try {
            final int i = Integer.parseInt(s);
            return i >= min && i <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isUnsignedFloat(String s) {
        if (s.isEmpty())
            return true;
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isSignedFloat(String s) {
        if (s.isEmpty() || s.equals("-"))
            return true;
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isJavaName(String s) {
        return s.codePoints().allMatch(Character::isJavaIdentifierStart);
    }

    public static boolean isValidName(String s) {
        return s.codePoints().allMatch(c -> Character.isLetter(c) || c == ' ');
    }
}
