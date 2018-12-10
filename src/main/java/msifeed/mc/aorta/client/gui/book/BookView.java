package msifeed.mc.aorta.client.gui.book;

import msifeed.mc.aorta.books.RemoteBook;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.TextWall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookView extends Widget {
    private static final int BOOK_TEXT_WIDTH = 115;
    private static final int BOOK_MAX_LINES = 17;

    private TextWall textWall = new TextWall();

    BookView() {
        setSizeHint(192, 192);
        setSizePolicy(SizePolicy.FIXED);
        getMargin().set(36, 10);

        textWall.setLines(Collections.singletonList("Looking for book..."));
        addChild(textWall);
    }

    public void setBook(RemoteBook book) {
        textWall.setLines(breakLines(book.text));
}

    @Override
    protected void renderSelf() {
        RenderParts.slice(BookParts.regular.bookBg, getGeometry());
    }

    private static List<String> breakLines(String text) {
        if (text.trim().isEmpty())
            return Collections.emptyList();

        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

        final ArrayList<String> lines = new ArrayList<>();
        int begin = 0;
        while (begin < text.length()) {
            String line = fr.trimStringToWidth(text.substring(begin), BOOK_TEXT_WIDTH);

            final int breakIndex = line.indexOf('\n');
            if (breakIndex > 0) {
                line = line.substring(0, breakIndex);
                begin += 1; // skip linebreak
            }

            line = line.replaceAll("[\n\r]", "");

            lines.add(line);
            begin += line.length();
        }

//        final StringBuilder sb = new StringBuilder(cleaned);
//        while (sb.length() > 0) {
//            final int textPartLength = Math.min(bookWidth, sb.length());
//            final String textPart = sb.substring(0, textPartLength);
//            final int lineWidth = font.sizeStringToWidth(textPart, bookWidth) + 1;
//
//            final int lineEnd = Math.min(lineWidth, sb.length());
//            final String line = sb.substring(0, lineEnd).trim();
//
//            lines.add(line);
//            sb.delete(0, lineEnd);
//        }

        return lines;
    }
}
