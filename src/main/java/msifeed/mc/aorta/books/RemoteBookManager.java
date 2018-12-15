package msifeed.mc.aorta.books;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public enum RemoteBookManager {
    INSTANCE;

    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID + ".books");
    private static Logger logger = LogManager.getLogger("Aorta.Books");
    private Consumer<RemoteBook> fetchConsumer = null;
    private Consumer<Boolean> checkConsumer = null;
    private File booksDir;

    public static void init() {
        final File mcRootDir = Loader.instance().getConfigDir().getParentFile();
        INSTANCE.booksDir = new File(mcRootDir, "books");
        INSTANCE.booksDir.mkdirs();

        CHANNEL.registerMessage(MessageRemoteBook.class, MessageRemoteBook.class, 0x00, Side.SERVER);
        CHANNEL.registerMessage(MessageRemoteBook.class, MessageRemoteBook.class, 0x01, Side.CLIENT);

        final ItemRemoteBook wikiBook = new ItemRemoteBook();
        GameRegistry.registerItem(wikiBook, "remote_book");
    }

    /**
     * @return Прошел ли запрос. Нельзя запрашивать следующую книжку до прихода предыдущей.
     */
    public boolean fetchBook(String name, Consumer<RemoteBook> consumer) {
        if (fetchConsumer != null) return false;

        fetchConsumer = consumer;
        CHANNEL.sendToServer(new MessageRemoteBook(MessageRemoteBook.Type.REQUEST_RESPONSE, name));
        return true;
    }

    public void sendCheck(String name, Consumer<Boolean> consumer) {
        if (checkConsumer != null) return;

        checkConsumer = consumer;
        CHANNEL.sendToServer(new MessageRemoteBook(MessageRemoteBook.Type.CHECK, name));
    }

    public void sendSign(String name) {
        CHANNEL.sendToServer(new MessageRemoteBook(MessageRemoteBook.Type.SIGN, name));
    }

    public void receiveResponse(String rawBook) {
        if (fetchConsumer == null) return;

        final Consumer<RemoteBook> consumer = fetchConsumer;
        fetchConsumer = null;

        try {
            consumer.accept(RemoteBookParser.parse(rawBook));
        } catch (Exception e) {
            logger.error(e);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Failed to parse book file."));

            consumer.accept(null);
        }
    }

    public void receiveCheck(boolean check) {
        if (checkConsumer == null) return;

        final Consumer<Boolean> consumer = checkConsumer;
        checkConsumer = null;
        consumer.accept(check);
    }

    public boolean checkBook(String name) {
        final File bookFile = new File(booksDir, name + ".txt");
        return bookFile.exists();
    }

    public String loadBook(String name) {
        final File bookFile = new File(booksDir, name + ".txt");
        if (!bookFile.exists()) return "";

        try {
            final String raw = new String(Files.readAllBytes(bookFile.toPath()), UTF_8);
            return raw.replaceAll("\r", "");
        } catch (IOException e) {
            logger.error(e);
            return "";
        }
    }

    public void signBook(EntityPlayerMP player, String name) {
        final ItemStack heldItem = player.getHeldItem();
        if (!(heldItem.getItem() instanceof ItemRemoteBook)) return;

        final String rawBook = loadBook(name);
        if (rawBook.isEmpty()) return;

        final RemoteBook book;
        try {
            book = RemoteBookParser.parse(rawBook);
        } catch (Exception e) {
            logger.error(e);
            player.addChatMessage(new ChatComponentText("Failed to parse book file."));
            return;
        }

        final NBTTagCompound tc = new NBTTagCompound();
        tc.setString("name", name);
        tc.setString("title", book.title);
        tc.setString("style", book.style.toString());
        heldItem.setTagCompound(tc);

        heldItem.setItemDamage(book.style.ordinal() + 1);

        player.updateHeldItem();
    }
}
