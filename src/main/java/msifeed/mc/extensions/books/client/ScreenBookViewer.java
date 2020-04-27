package msifeed.mc.extensions.books.client;

import msifeed.mc.extensions.books.RemoteBook;
import msifeed.mc.extensions.books.RemoteBookManager;
import msifeed.mc.extensions.books.client.inner.ReaderTextWrap;
import msifeed.mc.genesis.items.templates.BookTemplate;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.text.MultilineLabel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.Collections;

public class ScreenBookViewer extends MellowGuiScreen {
    private BookView bookView = new BookView(new ReaderTextWrap(new MultilineLabel()));

    public ScreenBookViewer(EntityPlayer player) {
        ItemStack itemStack = player.getHeldItem();

        final String name;
        if (itemStack.getItem() instanceof BookTemplate) {
            final BookTemplate bt = (BookTemplate) itemStack.getItem();
            name = bt.getData().index;
        } else {
            if (!itemStack.hasTagCompound())
                return;
            final NBTTagCompound tc = itemStack.getTagCompound();

            name = tc.getString("value");
            if (name.isEmpty()) {
                closeGui();
                return;
            }
        }

        final RemoteBook.Style[] styles = RemoteBook.Style.values();
        final RemoteBook.Style currentStyle = styles[MathHelper.clamp_int(itemStack.getItemDamage(), 0, styles.length)];
        bookView.setStyle(currentStyle);

        bookView.setLines(Collections.singletonList("Looking for book..."));
        scene.addChild(bookView);

        RemoteBookManager.INSTANCE.fetchBook(name, book -> {
            if (book == null) {
//                player.addChatMessage(new ChatComponentText("Unknown book."));
                closeGui();
                return;
            }
            bookView.setBook(book);
        });
    }

    @Override
    public void handleKeyboardInput() {
        final int k = Keyboard.getEventKey();
        if (k == Keyboard.KEY_ESCAPE)
            closeGui();
    }
}
