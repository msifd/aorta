package msifeed.mc.aorta.tweaks.nametag;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class Nametag {
    @SidedProxy(
            serverSide = "msifeed.mc.aorta.tweaks.nametag.Nametag",
            clientSide = "msifeed.mc.aorta.tweaks.nametag.NametagClient"
    )
    public static Nametag INSTANCE;

    static final String notifyTyping = "aorta:nametags.notify";
    static final String broadcastTyping = "aorta:nametags.broadcast";

    public void init() {
        Rpc.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @RpcMethod(Nametag.notifyTyping)
    public void onNotifyTyping(MessageContext ctx, int id) {
        Rpc.sendToAll(Nametag.broadcastTyping, id);
    }

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        event.displayname = getPreferredName(event.entityPlayer);
    }

    protected String getPreferredName(EntityPlayer player) {
        final CharStatus s = StatusAttribute.get(player).orElse(null);
        return s == null || s.name.isEmpty()
                ? player.getCommandSenderName()
                : s.name;
    }
}
