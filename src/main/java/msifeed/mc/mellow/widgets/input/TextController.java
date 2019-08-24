package msifeed.mc.mellow.widgets.input;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

class TextController {
    private ArrayList<StringBuilder> lines = new ArrayList<>();
    private int curLine = 0;
    private int curColumn = 0;

    private int maxLines = Integer.MAX_VALUE;
    private int maxChars = 75;

    private BiConsumer<TextController, StringBuilder> onChange = (c, l) -> {};

    TextController() {
        clear();
    }

    boolean isEmpty() {
        return lines.size() == 1 && lines.get(0).length() == 0;
    }

    int getCurColumn() {
        return curColumn;
    }

    int getCurLine() {
        return curLine;
    }

    void clear() {
        lines.clear();
        lines.add(new StringBuilder());
    }

    void moveCursor(int line, int column) {
        line = Math.max(0, Math.min(line, lines.size() - 1));
        final StringBuilder sb = lines.get(line);
        column = Math.max(0, Math.min(column, sb.length()));

        curLine = line;
        curColumn = column;
    }

    void moveCursorLine(boolean down) {
        final int target = curLine + (down ? 1 : -1);
        if (target >= 0 && target < lines.size()) {
            curLine = target;
            curColumn = Math.min(curColumn, lines.get(curLine).length());
        }
    }

    void moveCursorColumn(boolean right) {
        final StringBuilder sb = lines.get(curLine);
        final int target = getColumnTarget(sb, right);
        if (target >= 0 && target <= sb.length())
            curColumn = target;
    }

    void remove(boolean right) {
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
            changed(sb);
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

    void input(char c) {
        switch (Character.getType(c)) {
            case Character.CONTROL:     // \p{Cc}
            case Character.FORMAT:      // \p{Cf}
            case Character.PRIVATE_USE: // \p{Co}
            case Character.SURROGATE:   // \p{Cs}
            case Character.UNASSIGNED:  // \p{Cn}
                return;
        }

        StringBuilder sb = getCurrentLine();
        if (sb.length() >= maxChars) {
            if (lines.size() >= maxLines)
                return;
            breakLine();
            sb = getCurrentLine();
        }
        sb.insert(curColumn, c);
        curColumn++;
        changed(sb);
    }

    void input(String str) {
        if (isMultiLine()) {
            final String[] lines = str.split("\n");
            for (String line : lines) {
                inputSolidLine(line);
                if (line != lines[lines.length - 1])
                    breakLine();
            }
        } else {
            inputSolidLine(str.replaceAll("\n", " "));
        }
    }

    private void inputSolidLine(String str) {
        if (str.isEmpty())
            return;

        int strOffset = 0;
        StringBuilder sb = lines.get(curLine);
        while (strOffset < str.length()) {
            if (sb.length() < maxChars) {
                final int charsFit = maxChars - sb.length();
                final int offsetEnd = strOffset + Math.min(charsFit, str.length() - strOffset);
                sb.append(str, strOffset, offsetEnd);
                curColumn += offsetEnd - strOffset;
                strOffset = offsetEnd;
            } else {
                changed(sb);
                breakLine();
                sb = lines.get(lines.size() - 1);
            }
        }

        changed(sb);
    }

    void breakLine() {
        // TODO: переносить часть строки на новую при разрыве в середине
        final StringBuilder sb = new StringBuilder();
        lines.add(curLine + 1, sb);
        curLine++;
        curColumn = 0;
        changed(sb);
    }

    private boolean isMultiLine() {
        return maxLines > 0;
    }

    StringBuilder getCurrentLine() {
        return lines.get(curLine);
    }

    Stream<String> toLinesStream() {
        return lines.stream().map(StringBuilder::toString);
    }

    String toJoinedString() {
        return String.join("\n", lines);
    }

    private void changed(StringBuilder sb) {
        onChange.accept(this, sb);
    }

    private static boolean moveByWord() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    private static boolean moveAndSelect() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
}
