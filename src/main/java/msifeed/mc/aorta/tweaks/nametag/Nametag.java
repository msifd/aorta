package msifeed.mc.aorta.tweaks.nametag;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.rpc.Rpc;
import msifeed.mc.aorta.rpc.RpcMethod;
import net.minecraftforge.common.MinecraftForge;

public class Nametag {
    @SidedProxy(
            serverSide = "msifeed.mc.aorta.tweaks.nametag.Nametag",
            clientSide = "msifeed.mc.aorta.tweaks.nametag.NametagClient"
    )
    public static Nametag INSTANCE;

    static final String notifyTyping = "aorta:nametags.notify";
    static final String broadcastTyping = "aorta:nametags.broadcast";

    public void init() {
        Rpc.register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    public void notifyTyping() {
    }

    @SideOnly(Side.SERVER)
    @RpcMethod(notifyTyping)
    public void onNotifyTyping(MessageContext ctx, int id) {
        Rpc.sendToAll(broadcastTyping, id);
    }
}
