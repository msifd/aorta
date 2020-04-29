package msifeed.mc.extensions.books.client.inner;

import msifeed.mc.mellow.layout.FillLayout;
import msifeed.mc.mellow.widgets.text.TextInputArea;

import java.util.List;
import java.util.stream.Stream;

public class WriterTextWrap extends TextWrap {
    private final TextInputArea area;

    public WriterTextWrap(TextInputArea area) {
        this.area = area;

        setLayout(FillLayout.INSTANCE);

        area.getMargin().set(0);
        addChild(area);
    }

    @Override
    public int getColor() {
        return area.getColor();
    }

    @Override
    public void setColor(int color) {
        area.setColor(color);
    }

    @Override
    public int getLineSkip() {
        return area.getLineSkip();
    }

    @Override
    public void updateLineSkip(int delta) {
        area.updateLineSkip(delta);
    }

    @Override
    public void setLineLimit(int limit) {
        area.setLineLimit(limit);
    }

    @Override
    public void setMaxLineWidth(int width) {
        area.setMaxLineWidth(width);
        area.getController().setViewWidth(width);
        area.getController().setViewWidthReserve(0);
    }

    @Override
    public int getLineCount() {
        return area.getLineCount();
    }

    @Override
    public void setLines(List<String> lines) {
        area.setLines(lines);
    }
}
