package msifeed.mc.extensions.books;

import msifeed.mc.extensions.chat.Language;
import net.minecraft.util.ResourceLocation;

public class RemoteBook {
    public Style style = Style.REGULAR;
    public String title = "";
    public String text = "";
    public Language lang = Language.COMMON;

    public enum Style {
        REGULAR("book_regular"), RICH("book_rich"), PAD("book_pad"), NOTE("book_note");

        public final ResourceLocation sprite;

        Style(String tex) {
            this.sprite = new ResourceLocation("aorta", "textures/gui/" + tex + ".png");
        }
    }
}
