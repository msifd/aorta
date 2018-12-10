package msifeed.mc.aorta.client.gui.book;

import msifeed.mc.aorta.books.RemoteBook;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;

public class BookParts {
    static BookParts regular = new BookParts(RemoteBook.Style.BOOK);

    Part bookBg = new Part();

    BookParts(RemoteBook.Style style) {
        bookBg.sprite = style.sprite;
        bookBg.pos = new Point(0, 0);
        bookBg.size = new Point(192, 192);
    }
}
