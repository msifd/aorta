package msifeed.mc.mellow.widgets.text;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class MultilineLabel extends TextWall {
    public int brightColor = Mellow.getColor("text_bright");
    public int darkColor = Mellow.getColor("text_dark");

    protected List<String> lines = Collections.emptyList();
    protected int lineSkip = 0;
    protected int lineLimit = 10;

    protected int color = darkColor;

    public MultilineLabel() {
        setZLevel(1);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
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
        return lineSkip;
    }

    @Override
    public void setLineSkip(int skip) {
        this.lineSkip = skip;
    }

    @Override
    public void setLineLimit(int lineLimit) {
        this.lineLimit = lineLimit;
    }

    @Override
    public int getLineCount() {
        return lines.size();
    }

    @Override
    public Stream<String> getLines() {
        return lines.stream().skip(lineSkip).limit(lineLimit);
    }

    @Override
    public void setLines(List<String> lines) {
        super.setLines(lines);
        this.lines = lines;
    }
}
