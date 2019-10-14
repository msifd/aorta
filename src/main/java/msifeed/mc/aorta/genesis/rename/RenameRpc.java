package msifeed.mc.aorta.genesis.rename;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public enum  RenameRpc {
    INSTANCE;

    private static final String rename = "aorta:genesis.rename";
    private static final String clear = "aorta:genesis.rename.clear";
    private static final String setValue = "aorta:genesis.rename.value.set";
    private static final String openRenameGui = "aorta:genesis.rename.gui";

    public static void rename(String title, List<String> desc) {
        final NBTTagCompound nbt = new NBTTagCompound();
        if (!title.isEmpty())
            nbt.setString("t", title);

        final NBTTagList descNbt = new NBTTagList();
        for (String l : desc)
            descNbt.appendTag(new NBTTagString(RenameProvider.fromAmpersandFormatting("\u00A7r" + l)));
        nbt.setTag("d", descNbt);

        try {
            Rpc.sendToServer(rename, (Serializable) CompressedStreamTools.compress(nbt));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(rename)
    public void onRename(MessageContext ctx, byte[] nbtBytes) {
        if (ctx.side.isClient())
            return;
        final EntityPlayer sender = ctx.getServerHandler().playerEntity;
        final ItemStack itemStack = sender.getHeldItem();
        if (itemStack == null)
            return;

        try {
            final NBTTagCompound nbt = CompressedStreamTools.func_152457_a(nbtBytes, new NBTSizeTracker(2097152L));
            if (nbt.hasKey("t"))
                RenameProvider.setTitle(itemStack, nbt.getString("t"));
            if (nbt.hasKey("d"))
                RenameProvider.setDescription(itemStack, nbt.getTagList("d", 8)); // 8 - string
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        Aorta.GUI_HANDLER.toggleRenamer();
    }
}
