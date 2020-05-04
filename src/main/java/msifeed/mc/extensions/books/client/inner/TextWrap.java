package msifeed.mc.extensions.books.client.inner;

import msifeed.mc.mellow.widgets.Widget;

import java.util.List;

public abstract class TextWrap extends Widget {
    public abstract int getColor();
    public abstract void setColor(int color);

    public abstract int getLineSkip();
    public abstract void updateLineSkip(int delta);
    public abstract void setLineLimit(int limit);
    public abstract void setMaxLineWidth(int width);

    public abstract int getLineCount();
    public abstract void setLines(List<String> lines);
}
