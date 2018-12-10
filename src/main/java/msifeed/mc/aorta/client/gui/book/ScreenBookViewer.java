package msifeed.mc.aorta.client.gui.book;

import msifeed.mc.aorta.books.RemoteBookManager;
import msifeed.mc.aorta.genesis.items.templates.BookTemplate;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

public class ScreenBookViewer extends MellowGuiScreen {
    private BookView bookView = new BookView();

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

            name = tc.getString("name");
            if (name.isEmpty()) {
                closeGui();
                return;
            }
        }

//        final RemoteBook.Style style;
//        try {
//            style = RemoteBook.Style.valueOf(tc.getString("style"));
//        } catch (Exception ignored) {
//            return;
//        }

        scene.addChild(bookView);

        RemoteBookManager.INSTANCE.fetchBook(name, book -> {
            if (book == null) {
                player.addChatMessage(new ChatComponentText("Unknown book."));
                closeGui();
                return;
            }
//            if (book.style == null)
//                book.style = style;
            bookView.setBook(book);
        });
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleKeyboardInput() {
        final int k = Keyboard.getEventKey();
        if (k == Keyboard.KEY_ESCAPE)
            closeGui();
    }

    private static void closeGui() {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }

}
