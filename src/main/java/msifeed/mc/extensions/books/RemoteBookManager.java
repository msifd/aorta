package msifeed.mc.extensions.books;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.rpc.Rpc;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

public enum RemoteBookManager {
    INSTANCE;

    private static RemoteBookRpc rpcHandler = new RemoteBookRpc();
    private static Random random = new Random();

    private File booksDir;

    // Client-side consumers
    private Consumer<Boolean> checkConsumer = null;
    private Consumer<RemoteBook> fetchConsumer = null;

    public static void preInit() {
        Rpc.register(rpcHandler);

        final File mcRootDir = Loader.instance().getConfigDir().getParentFile();
        INSTANCE.booksDir = new File(mcRootDir, "books");
        INSTANCE.booksDir.mkdirs();
    }

    public static void init() {
        GameRegistry.registerItem(new ItemRemoteBook(), ItemRemoteBook.ID);
        GameRegistry.registerItem(new ItemRemoteBookEditor(), "remote_book_editor");
        GameRegistry.registerItem(new ItemRemoteBookLoader(), "remote_book_loader");
    }

    public void fetchBook(String index, Consumer<RemoteBook> consumer) {
        if (fetchConsumer != null) return;

        fetchConsumer = consumer;
        RemoteBookRpc.fetch(index);
    }

    public void requestCheck(String index, Consumer<Boolean> consumer) {
        if (checkConsumer != null) return;

        checkConsumer = consumer;
        RemoteBookRpc.check(index);
    }

    public void requestLoad(String index) {
        RemoteBookRpc.load(index);
    }

    public void receiveResponse(String rawBook) {
        if (fetchConsumer == null) return;

        final Consumer<RemoteBook> consumer = fetchConsumer;
        fetchConsumer = null;

        final RemoteBook book = RemoteBookParser.parse(rawBook);
        if (book == null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Failed to open book."));
            consumer.accept(null);
            return;
        }

        consumer.accept(book);
    }

    public void receiveCheck(boolean exists) {
        if (checkConsumer == null) return;

        final Consumer<Boolean> consumer = checkConsumer;
        checkConsumer = null;
        consumer.accept(exists);
    }

    public boolean checkBook(String name) {
        final File bookFile = new File(booksDir, name + ".txt");
        return bookFile.exists();
    }

    public String readBook(String name) {
        final File bookFile = new File(booksDir, name + ".txt");
        if (!bookFile.exists()) return "";

        try {
            return new String(Files.readAllBytes(bookFile.toPath()), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void readBook(EntityPlayerMP player, String index) {
        final ItemStack loaderItem = player.getHeldItem();
        if (!(loaderItem.getItem() instanceof ItemRemoteBookLoader)) return;

        final String rawBook = readBook(index);
        if (rawBook.isEmpty()) return;

        final RemoteBook book = RemoteBookParser.parse(rawBook);
        if (book == null) {
            player.addChatMessage(new ChatComponentText("Failed to parse book."));
            return;
        }

        loaderItem.stackSize--;
        player.inventory.addItemStackToInventory(makeBook(index, book.title, book.style));
//        player.updateHeldItem();
        player.inventory.markDirty();
    }

    public void publishBook(EntityPlayerMP player, String text, String title, RemoteBook.Style style) {
        final ItemStack heldItem = player.getHeldItem();
        if (!(heldItem.getItem() instanceof ItemRemoteBookEditor)) return;

        final String compatibilityLang = "VANILLA";
//        final String index = player.getCommandSenderName() + "-" + UUID.randomUUID();
        String safeTitle = title.replaceAll("\\s", "-");
        safeTitle = safeTitle.substring(0, Math.min(safeTitle.length(), 16));

        final String index = player.getCommandSenderName() + "-" + safeTitle + "-" + random.nextInt();
        final String header = String.format("#! %s %s\n%s\n", style.name(), compatibilityLang, title);
        final String finalText = header + text;

        try {
            final Path path = Paths.get(booksDir.getPath(), index + ".txt");
            Files.write(path, finalText.getBytes(UTF_8), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        heldItem.stackSize--;
        player.inventory.addItemStackToInventory(makeBook(index, title, style));
        player.updateHeldItem();
    }

    private static ItemStack makeBook(String index, String title, RemoteBook.Style style) {
        final ItemStack book = GameRegistry.findItemStack(Bootstrap.MODID, ItemRemoteBook.ID, 1);
        book.setItemDamage(style.ordinal());

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("value", index);
        tag.setString("title", title);
        tag.setString("style", style.name());
        book.setTagCompound(tag);

        return book;
    }
}
