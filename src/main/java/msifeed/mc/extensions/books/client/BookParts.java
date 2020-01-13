package msifeed.mc.extensions.books.client;

import msifeed.mc.extensions.books.RemoteBook;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.Point;

enum BookParts {
    REGULAR(RemoteBook.Style.REGULAR), RICH(RemoteBook.Style.RICH), PAD(RemoteBook.Style.PAD), NOTE(RemoteBook.Style.NOTE);

    RemoteBook.Style style;
    Part bookBg = new Part();
    Part leftBtn = new Part();
    Part leftBtnHover = new Part();
    Part rightBtn = new Part();
    Part rightBtnHover = new Part();

    BookParts(RemoteBook.Style style) {
        this.style = style;
        bookBg.sprite = style.sprite;
        bookBg.pos = new Point(0, 0);
        bookBg.size = new Point(192, 192);

        leftBtn.sprite = style.sprite;
        leftBtn.pos = new Point(0, 205);
        leftBtn.size = new Point(23, 13);
        leftBtnHover.sprite = style.sprite;
        leftBtnHover.pos = new Point(23, 205);
        leftBtnHover.size = new Point(23, 13);

        rightBtn.sprite = style.sprite;
        rightBtn.pos = new Point(0, 192);
        rightBtn.size = new Point(23, 13);
        rightBtnHover.sprite = style.sprite;
        rightBtnHover.pos = new Point(23, 192);
        rightBtnHover.size = new Point(23, 13);
    }
}
