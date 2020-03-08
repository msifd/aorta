package msifeed.mc.extensions.rename;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.List;

public enum RenameRpc {
    INSTANCE;

    private static final String rename = Bootstrap.MODID + ":rename";
    private static final String clear = Bootstrap.MODID + ":rename.clear";
    private static final String setValue = Bootstrap.MODID + ":rename.value.set";
    private static final String openRenameGui = Bootstrap.MODID + ":rename.gui";

    public static void init() {
        Rpc.register(INSTANCE);
    }

    public static void rename(String title, List<String> desc) {
        final NBTTagCompound nbt = new NBTTagCompound();
        if (!title.isEmpty())
            nbt.setString("t", title);

        final NBTTagList descNbt = new NBTTagList();
        for (String l : desc)
            descNbt.appendTag(new NBTTagString(RenameProvider.fromAmpersandFormatting("\u00A7r" + l)));
        nbt.setTag("d", descNbt);

        Rpc.sendToServer(rename, nbt);
    }

    @RpcMethod(rename)
    public void onRename(MessageContext ctx, NBTTagCompound nbt) {
        if (ctx.side.isClient())
            return;
        final EntityPlayer sender = ctx.getServerHandler().playerEntity;
        final ItemStack itemStack = sender.getHeldItem();
        if (itemStack == null)
            return;

        if (nbt.hasKey("t"))
            RenameProvider.setTitle(itemStack, nbt.getString("t"));
        if (nbt.hasKey("d"))
            RenameProvider.setDescription(itemStack, nbt.getTagList("d", 8)); // 8 - string
    }

    public static void clear() {
        Rpc.sendToServer(clear);
    }

    @RpcMethod(clear)
    public void onClear(MessageContext ctx) {
        if (ctx.side.isClient())
            return;
        final EntityPlayer sender = ctx.getServerHandler().playerEntity;
        final ItemStack itemStack = sender.getHeldItem();
        if (itemStack == null)
            return;

        RenameProvider.clearAll(itemStack);
    }

    public static void setValue(String key, String value) {
        if (!key.isEmpty())
            Rpc.sendToServer(setValue, key, value == null ? "[del]" : value);
    }

    @RpcMethod(setValue)
    public void onSetValue(MessageContext ctx, String key, String value) {
        if (ctx.side.isClient())
            return;
        final EntityPlayer sender = ctx.getServerHandler().playerEntity;
        final ItemStack itemStack = sender.getHeldItem();
        if (itemStack == null)
            return;

        RenameProvider.setValue(itemStack, key, value.equals("[del]") ? null : value);
    }

    public static void openRenameGui(EntityPlayerMP player) {
        Rpc.sendTo(player, openRenameGui);
    }

    @RpcMethod(openRenameGui)
    public void onOpenRenameGui(MessageContext ctx) {
        More.GUI_HANDLER.toggleRenamer();
    }
}
