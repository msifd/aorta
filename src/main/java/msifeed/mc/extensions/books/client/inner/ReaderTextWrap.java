package msifeed.mc.extensions.books.client.inner;

import msifeed.mc.mellow.layout.FillLayout;
import msifeed.mc.mellow.widgets.text.MultilineLabel;

import java.util.List;
import java.util.stream.Stream;

public class ReaderTextWrap extends TextWrap {
    private final MultilineLabel label;

    public ReaderTextWrap(MultilineLabel label) {
        this.label = label;

        setLayout(FillLayout.INSTANCE);
        addChild(label);
    }

    @Override
    public int getColor() {
        return label.getColor();
    }

    @Override
    public void setColor(int color) {
        label.setColor(color);
    }

    @Override
    public int getLineSkip() {
        return label.getLineSkip();
    }

    @Override
    public void updateLineSkip(int delta) {
        label.updateLineSkip(delta);
    }

    @Override
    public void setLineLimit(int limit) {
        label.setLineLimit(limit);
    }

    @Override
    public void setMaxLineWidth(int width) {
    }

    @Override
    public int getLineCount() {
        return label.getLines().size();
    }

    @Override
    public void setLines(List<String> lines) {
        label.setLines(lines);
    }
}
