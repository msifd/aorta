package msifeed.mc.mellow.widgets.text.inner;

import msifeed.mc.mellow.render.RenderWidgets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextController {
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private List<Line> lines = new ArrayList<>();

    private List<String> stringCache;
    private boolean cacheInvalid = true;
    private int cacheCursorX = 0;

    private int curLine = 0;
    private int curColumn = 0;

    private int offsetLine = 0;
    private int offsetColumn = 0;

    private int viewHeight = 50;
    private int viewWidth = 50;
    private int viewWidthReserve = 4;

    private int maxLines = Integer.MAX_VALUE;
    private int maxWidth = Integer.MAX_VALUE;

    public TextController() {
        clear();
    }

    public boolean isEmpty() {
        return lines.size() == 1 && lines.get(0).sb.length() == 0;
    }

    public int getCurLine() {
        return curLine;
    }

    public int getCurColumn() {
        return curColumn;
    }

    public int getCursorXOffset() {
        return cacheCursorX;
    }

    public int getOffsetColumn() {
        return offsetColumn;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public void setViewWidthReserve(int viewWidthReserve) {
        this.viewWidthReserve = viewWidthReserve;
    }

    public void setLinesPerView(int lines) {
        viewHeight = lines * RenderWidgets.lineHeight();
    }

    public int getLinesPerView() {
        return viewHeight / RenderWidgets.lineHeight();
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int n) {
        this.maxLines = n;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void updateOffsetLine(int lineDelta) {
        offsetLine = Math.max(0, offsetLine + lineDelta);
        cacheInvalid = true;
        refreshCursorPos();
    }

    public int getOffsetLine() {
        return offsetLine;
    }

    public Line getCurrentLine() {
        return lines.get(Math.max(0, Math.min(curLine, lines.size() - 1)));
    }

    public Line getLine(int n) {
        return lines.get(Math.max(0, Math.min(n, lines.size() - 1)));
    }

    public int getLineCount() {
        return lines.size();
    }

    public String toJoinedString() {
        final StringBuilder sb = new StringBuilder();
        for (Line l : lines) {
            sb.append(l.sb);
            sb.append('\n');
        }
        return sb.toString();
    }

    public Stream<String> toLineStream() {
        if (cacheInvalid) {
            stringCache = lines.stream()
                    .skip(offsetLine)
                    .limit(getLinesPerView())
                    .map(ln -> {
                        if (ln.columns > offsetColumn)
                            return fontRenderer.trimStringToWidth(ln.sb.substring(offsetColumn), viewWidth);
                        else
                            return "";
                    })
                    .collect(Collectors.toList());
            cacheInvalid = false;
        }

        return stringCache.stream();
    }

    public void setLines(List<String> lines) {
        this.lines.clear();
        for (String s : lines)
            this.lines.add(new Line(s));
        if (this.lines.isEmpty())
            this.lines.add(new Line());
    }

    public void clear() {
        lines.clear();
        lines.add(new Line());
    }

    public void setCursor(int line, int column) {
        line = Math.max(0, Math.min(line, lines.size() - 1));
        final Line ln = lines.get(line);
        column = Math.max(0, Math.min(column, ln.sb.length()));

        curLine = line;
        curColumn = column;
        refreshOffsetColumn();
    }

    public void moveCursorLine(int delta) {
        final int target = Math.max(0, Math.min(curLine + delta, lines.size() - 1));
        if (target != curLine) {
            curLine = target;
            final int targetCol = fontRenderer.trimStringToWidth(lines.get(curLine).sb.toString(), cacheCursorX).length();
            curColumn = Math.min(targetCol, lines.get(curLine).sb.length());

            refreshOffsetColumn();
        }
    }

    public void moveCursorColumn(boolean right) {
        final StringBuilder sb = lines.get(curLine).sb;
        final int target = getColumnTarget(sb, right);
        if (target >= 0 && target <= sb.length()) {
            curColumn = target;
            refreshOffsetColumn();
        } else if (target < 0) {
            if (curLine > 0)
                setCursor(curLine - 1, lines.get(curLine - 1).columns);
        } else {
            if (curLine + 1 < lines.size())
                setCursor(curLine + 1, 0);
        }
    }

    private void refreshCursorPos() {
        cacheCursorX = fontRenderer.getStringWidth(getCurrentLine().sb.substring(offsetColumn, curColumn));
    }

    private void refreshOffsetColumn() {
        if (curColumn < offsetColumn) {
            offsetColumn = curColumn;
            cacheInvalid = true;
            refreshCursorPos();
        } else {
            refreshCursorPos();
            if (viewWidth - cacheCursorX < viewWidthReserve) {
                final int overlap = cacheCursorX - viewWidth + viewWidthReserve;
                offsetColumn += fontRenderer.trimStringToWidth(getCurrentLine().sb.toString(), overlap).length();
                cacheInvalid = true;
                refreshCursorPos();
            }
        }
    }

    public void remove(boolean right) {
        final Line line = lines.get(curLine);
        final int target = getColumnTarget(line.sb, right);

        if (curColumn == target)
            return;

        final int start = curColumn < target ? curColumn : target;
        final int end = curColumn > target ? curColumn : target;

        if (start < 0) { // backspace line
            if (curLine == 0)
                return;
            if (line.sb.length() == 0) {
                lines.remove(curLine);
                setCursor(curLine - 1, getLine(curLine - 1).sb.length());
                cacheInvalid = true;
            } else {
                final Line prevLn = lines.get(curLine - 1);
                final int targetColumn = prevLn.columns;
                curColumn = targetColumn;
                final String leftover = prevLn.insert(line.sb.toString());
                if (leftover.isEmpty())
                    lines.remove(curLine);
                else
                    line.remove(0, line.columns - leftover.length());
                setCursor(curLine, targetColumn);
                cacheInvalid = true;
            }
        } else if (end > line.sb.length()) { // delete line
            if (curLine == lines.size() - 1)
                return;
            if (line.sb.length() == 0) {
                lines.remove(curLine);
                cacheInvalid = true;
            }
        } else {
            if (line.remove(start, end) && !right)
                setCursor(curLine, target);
        }
    }

    private int getColumnTarget(StringBuilder sb, boolean right) {
        if (moveByWord()) {
            final int t = right ? sb.indexOf(" ", curColumn + 1) : sb.lastIndexOf(" ", curColumn - 1);
            return t != -1 ? t : (right ? sb.length() : 0);
        } else {
            return curColumn + (right ? 1 : -1);
        }
    }

    public boolean insert(char c) {
        switch (Character.getType(c)) {
            case Character.CONTROL:     // \p{Cc}
            case Character.FORMAT:      // \p{Cf}
            case Character.PRIVATE_USE: // \p{Co}
            case Character.SURROGATE:   // \p{Cs}
            case Character.UNASSIGNED:  // \p{Cn}
                return false;
        }

        final int lc = getLineCount();
        final int lw = getCurrentLine().width;

        if (!getCurrentLine().insert(c))
            if (breakLine())
                getCurrentLine().insert(c);
        final boolean inserted = lc != getLineCount() || lw != getCurrentLine().width;

        if (inserted)
            refreshOffsetColumn();

        return inserted;
    }

    public void insert(String str) {
        final String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            inputSolidLine(lines[i]);
            if (i < lines.length - 1)
                if (!breakLine())
                    break;
        }
        cacheInvalid = true;
    }

    private void inputSolidLine(String str) {
        if (str.isEmpty())
            return;

        String overflow = getCurrentLine().insert(str);
        while (!overflow.isEmpty()) {
            if (!breakLine())
                break;
            overflow = getCurrentLine().insert(overflow);
        }

        refreshOffsetColumn();
    }

    public boolean breakLine() {
        if (getLineCount() >= maxLines)
            return false;

        final String ending = getCurrentLine().sb.substring(curColumn);
        if (ending.isEmpty()) {
            lines.add(curLine + 1, new Line());
        } else {
            getCurrentLine().remove(curColumn, curColumn + ending.length());
            lines.add(curLine + 1, new Line(ending));
        }

        curLine++;
        curColumn = 0;
        offsetColumn = 0;
        cacheInvalid = true;
        refreshCursorPos();

        return true;
    }

    private static boolean moveByWord() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public class Line {
        public StringBuilder sb = new StringBuilder();
        public int columns = 0;
        public int width = 0;

        public Line() {

        }
        public Line(String s) {
            sb.append(s);
            columns = s.length();
            width = fontRenderer.getStringWidth(s);
        }

        public boolean insert(char c) {
            final int cw = fontRenderer.getCharWidth(c);
            if (width + cw > maxWidth)
                return false;

            sb.insert(curColumn, c);
            columns++;
            width += cw;
            curColumn++;
            cacheInvalid = true;

            return true;
        }

        public String insert(String s) {
            final String ts = fontRenderer.trimStringToWidth(s, maxWidth - width);
            if (ts.isEmpty())
                return s;

            sb.insert(curColumn, ts);
            columns += ts.length();
            width += fontRenderer.getStringWidth(ts);
            curColumn += ts.length();
            cacheInvalid = true;

            return s.substring(ts.length());
        }

        public boolean remove(int start, int end) {
            if (sb.length() == 0)
                return false;

            final int w = fontRenderer.getStringWidth(sb.substring(start, end));
            sb.delete(start, end);
            columns -= end - start;
            width -= w;
            cacheInvalid = true;

            return true;
        }
    }
}
