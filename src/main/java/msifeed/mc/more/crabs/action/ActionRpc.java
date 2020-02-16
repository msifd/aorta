package msifeed.mc.more.crabs.action;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public enum ActionRpc {
    INSTANCE;

    private static final String actionsUpdate = Bootstrap.MODID + ":actions.upd";
    private static final String actionsRequest = Bootstrap.MODID + ":actions.req";

    public static void broadcastToAll(Collection<Action> actions) {
        try {
            final byte[] bytes = wrapHeaders(actions);
            Rpc.sendToAll(actionsUpdate, (Serializable) bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastTo(EntityPlayerMP player, Collection<Action> actions) {
        try {
            final byte[] bytes = wrapHeaders(actions);
            Rpc.sendTo(player, actionsUpdate, (Serializable) bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestActions() {
        Rpc.sendToServer(actionsRequest);
    }

    @RpcMethod(actionsRequest)
    public void onActionsRequest(MessageContext ctx) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        try {
            final byte[] bytes = wrapHeaders(ActionRegistry.getActions());
            Rpc.sendTo(sender, actionsUpdate, (Serializable) bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(actionsUpdate)
    public void onActionsUpdate(MessageContext ctx, byte[] nbtBytes) {
        try {
            final ArrayList<ActionHeader> headers = unwrapHeaders(nbtBytes);
            ActionRegistry.setActionHeaders(headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] wrapHeaders(Collection<Action> actions) throws IOException {
        final NBTTagCompound tag = new NBTTagCompound();
        final NBTTagList list = new NBTTagList();
        for (Action a : actions)
            list.appendTag(a.toHeaderNBT());
        tag.setTag("l", list);

        return CompressedStreamTools.compress(tag);
    }

    private static ArrayList<ActionHeader> unwrapHeaders(byte[] bytes) throws IOException {
        final NBTTagCompound tag = CompressedStreamTools.func_152457_a(bytes, new NBTSizeTracker(2097152L));
        final NBTTagList list = tag.getTagList("l", 10); // 10 - NBTTagCompound

        final ArrayList<ActionHeader> actions = new ArrayList<>(list.tagCount());
        for (int i = 0; i < list.tagCount(); i++)
            actions.add(new ActionHeader(list.getCompoundTagAt(i)));

        return actions;
    }
}
