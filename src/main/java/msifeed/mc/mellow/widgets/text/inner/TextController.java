package msifeed.mc.mellow.widgets.text.inner;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TextController {
    private FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private List<Line> lines = new ArrayList<>();
    private int curLine = 0;
    private int curColumn = 0;
    private int maxWidth = 50;
    private int skip = 0;
    private int limit = 10;

    public TextController() {
        clear();
    }

    public int getCurLine() {
        return curLine;
    }

    public int getCurLineView() {
        return curLine % limit;
    }

    public int getCurColumn() {
        return curColumn;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = Math.max(0, Math.min(skip, getLineCount() - 1));
    }

    public void updateSkip(int delta) {
        setSkip(skip + delta);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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
        return lines.stream()
                .skip(skip)
                .limit(limit)
                .map(l -> l.sb.toString());
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

    public void moveCursor(int line, int column) {
        line = Math.max(0, Math.min(line, lines.size() - 1));
        final StringBuilder sb = lines.get(line).sb;
        column = Math.max(0, Math.min(column, sb.length()));

        curLine = line;
        curColumn = column;
    }

    public void moveCursorLine(int delta) {
        final int target = Math.max(0, Math.min(curLine + delta, lines.size() - 1));
        if (target != curLine) {
            curLine = target;
            curColumn = Math.min(curColumn, lines.get(curLine).sb.length());
        }
    }

    public void moveCursorColumn(boolean right) {
        final StringBuilder sb = lines.get(curLine).sb;
        final int target = getColumnTarget(sb, right);
        if (target >= 0 && target <= sb.length())
            curColumn = target;
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
                curLine--;
                curColumn = getCurrentLine().sb.length();
            }
        } else if (end > line.sb.length()) { // delete line
            if (curLine == lines.size() - 1)
                return;
            if (line.sb.length() == 0)
                lines.remove(curLine);
        } else {
            if (line.remove(start, end) && !right)
                curColumn = target;
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

    public void insert(char c) {
        switch (Character.getType(c)) {
            case Character.CONTROL:     // \p{Cc}
            case Character.FORMAT:      // \p{Cf}
            case Character.PRIVATE_USE: // \p{Co}
            case Character.SURROGATE:   // \p{Cs}
            case Character.UNASSIGNED:  // \p{Cn}
                return;
        }

        if (!getCurrentLine().insert(c)) {
            breakLine();
            getCurrentLine().insert(c);
        }
    }

    public void insert(String str) {
        final String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            inputSolidLine(lines[i]);
            if (i < lines.length - 1)
                breakLine();
        }
    }

    private void inputSolidLine(String str) {
        if (str.isEmpty())
            return;

        String overflow = getCurrentLine().insert(str);
        while (!overflow.isEmpty()) {
            breakLine();
            overflow = getCurrentLine().insert(overflow);
        }
    }

    public void breakLine() {
        final String ending = getCurrentLine().sb.substring(curColumn);
        if (ending.isEmpty()) {
            lines.add(curLine + 1, new Line());
        } else {
            getCurrentLine().remove(curColumn, curColumn + ending.length());
            lines.add(curLine + 1, new Line(ending));
        }
        curLine++;
        curColumn = 0;
    }

    private static boolean moveByWord() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public class Line {
        public StringBuilder sb = new StringBuilder();
        public int width = 0;

        public Line() {

        }
        public Line(String s) {
            sb.append(s);
            width = fontRenderer.getStringWidth(s);
        }

        public boolean insert(char c) {
            final int cw = fontRenderer.getCharWidth(c);
            if (width + cw > maxWidth)
                return false;
            sb.insert(curColumn, c);
            width += cw;
            curColumn++;
            return true;
        }

        public String insert(String s) {
            final String ts = fontRenderer.trimStringToWidth(s, maxWidth - width);
            if (ts.isEmpty())
                return s;
            sb.insert(curColumn, ts);
            width += fontRenderer.getStringWidth(ts);
            curColumn += ts.length();
            return s.substring(ts.length());
        }

        public boolean remove(int start, int end) {
            if (sb.length() == 0)
                return false;
            final int w = fontRenderer.getStringWidth(sb.substring(start, end));
            sb.delete(start, end);
            width -= w;
            return true;
        }
    }
}
