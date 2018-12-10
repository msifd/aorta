package msifeed.mc.aorta.books;

import net.minecraft.util.ResourceLocation;

public class RemoteBook {
    public Style style = Style.BOOK;
    public String title = "";
    public String text = "";

    public enum Style {
        BOOK("book_regular"), RICH_BOOK("book_rich"), PAD("book_pad"), NOTE("book_note");

        public final ResourceLocation sprite;

        Style(String tex) {
            this.sprite = new ResourceLocation("aorta", "textures/gui/" + tex + ".png");
        }
    }
}
