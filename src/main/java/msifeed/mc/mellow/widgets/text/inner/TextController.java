package msifeed.mc.mellow.widgets.text.inner;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TextController {
    private List<StringBuilder> lines = new ArrayList<>();
    private int curLine = 0;
    private int curColumn = 0;
    private int maxColumns = 50;
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

    public int getMaxColumns() {
        return maxColumns;
    }

    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
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

    public StringBuilder getCurrentLine() {
        return lines.get(Math.max(0, Math.min(curLine, lines.size() - 1)));
    }

    public StringBuilder getLine(int n) {
        return lines.get(Math.max(0, Math.min(n, lines.size() - 1)));
    }

    public int getLineCount() {
        return lines.size();
    }

    public String toJoinedString() {
        final StringBuilder sb = new StringBuilder();
        for (StringBuilder l : lines) {
            sb.append(l);
            sb.append('\n');
        }
        return sb.toString();
//        return String.join("\n", lines);
    }

    public Stream<String> toLineStream() {
        return lines.stream()
                .skip(skip)
                .limit(limit)
                .map(StringBuilder::toString);
    }

    public void setLines(List<String> lines) {
        this.lines.clear();
        for (String s : lines)
            this.lines.add(new StringBuilder(s));
        if (this.lines.isEmpty())
            this.lines.add(new StringBuilder());
    }

    public void clear() {
        lines.clear();
        lines.add(new StringBuilder());
    }

    public void moveCursor(int line, int column) {
        line = Math.max(0, Math.min(line, lines.size() - 1));
        final StringBuilder sb = lines.get(line);
        column = Math.max(0, Math.min(column, sb.length()));

        curLine = line;
        curColumn = column;
    }

    public void moveCursorLine(int delta) {
        final int target = Math.max(0, Math.min(curLine + delta, lines.size() - 1));
        if (target != curLine) {
            curLine = target;
            curColumn = Math.min(curColumn, lines.get(curLine).length());
        }
    }

    public void moveCursorColumn(boolean right) {
        final StringBuilder sb = lines.get(curLine);
        final int target = getColumnTarget(sb, right);
        if (target >= 0 && target <= sb.length())
            curColumn = target;
    }

    public void remove(boolean right) {
        final StringBuilder sb = lines.get(curLine);
        final int target = getColumnTarget(sb, right);

        if (curColumn == target)
            return;

        final int start = curColumn < target ? curColumn : target;
        final int end = curColumn > target ? curColumn : target;

        // TODO: удаление строк c переносом
        if (start < 0) { // backspace line
            if (curLine == 0)
                return;
            if (sb.length() == 0) {
                lines.remove(curLine);
                curLine--;
                curColumn = getCurrentLine().length();
            }
        } else if (end > sb.length()) { // delete line
            if (curLine == lines.size() - 1)
                return;
            if (sb.length() == 0) {
                lines.remove(curLine);
            }
        } else {
            sb.delete(start, end);
            if (!right)
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

        StringBuilder sb = getCurrentLine();
        if (sb.length() >= maxColumns) {
            breakLine();
            sb = getCurrentLine();
        }
        sb.insert(curColumn, c);
        curColumn++;
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

        int strOffset = 0;
        StringBuilder sb = lines.get(curLine);
        while (strOffset < str.length()) {
            if (sb.length() < maxColumns) {
                final int charsFit = maxColumns - sb.length();
                final int offsetEnd = strOffset + Math.min(charsFit, str.length() - strOffset);
                sb.append(str, strOffset, offsetEnd);
                curColumn += offsetEnd - strOffset;
                strOffset = offsetEnd;
            } else {
                breakLine();
                sb = lines.get(lines.size() - 1);
            }
        }
    }

    public void breakLine() {
        // TODO: переносить часть строки на новую при разрыве в середине
        final StringBuilder sb = new StringBuilder();
        lines.add(curLine + 1, sb);
        curLine++;
        curColumn = 0;
    }

    private static boolean moveByWord() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }
}
