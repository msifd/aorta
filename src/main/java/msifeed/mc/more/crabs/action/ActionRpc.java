package msifeed.mc.more.crabs.action;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collection;

public enum ActionRpc {
    INSTANCE;

    private static final String actionsUpdate = Bootstrap.MODID + ":actions.upd";
    public static void broadcastToAll(Collection<Action> actions) {
        Rpc.sendToAll(actionsUpdate, wrapHeaders(actions));
    }

    public static void broadcastTo(EntityPlayerMP player, Collection<Action> actions) {
        Rpc.sendTo(player, actionsUpdate, wrapHeaders(actions));
    }

    @RpcMethod(actionsUpdate)
    public void onActionsUpdate(MessageContext ctx, NBTTagCompound nbt) {
        ActionRegistry.setActionHeaders(unwrapHeaders(nbt));
    }

    private static NBTTagCompound wrapHeaders(Collection<Action> actions) {
        final NBTTagCompound tag = new NBTTagCompound();
        final NBTTagList list = new NBTTagList();
        for (Action a : actions)
            list.appendTag(a.toHeaderNBT());
        tag.setTag("l", list);

        return tag;
    }

    private static ArrayList<ActionHeader> unwrapHeaders(NBTTagCompound tag) {
        final NBTTagList list = tag.getTagList("l", 10); // 10 - NBTTagCompound

        final ArrayList<ActionHeader> actions = new ArrayList<>(list.tagCount());
        for (int i = 0; i < list.tagCount(); i++)
            actions.add(new ActionHeader(list.getCompoundTagAt(i)));

        return actions;
    }
}
