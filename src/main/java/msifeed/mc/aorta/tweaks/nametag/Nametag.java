package msifeed.mc.aorta.tweaks.nametag;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class Nametag {
    @SidedProxy(
            serverSide = "msifeed.mc.aorta.tweaks.nametag.Nametag",
            clientSide = "msifeed.mc.aorta.tweaks.nametag.NametagClient"
    )
    public static Nametag INSTANCE;

    static final String broadcastTyping = "aorta:nametags.broadcast";
    private static final String notifyTyping = "aorta:nametags.notify";

    public void init() {
        Rpc.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void notifyTyping() {
        Rpc.sendToServer(Nametag.notifyTyping, Minecraft.getMinecraft().thePlayer.getEntityId());
    }

    @RpcMethod(Nametag.notifyTyping)
    public void onNotifyTyping(MessageContext ctx, int id) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final ChunkCoordinates coord = player.getPlayerCoordinates();
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
                player.dimension, coord.posX, coord.posY, coord.posZ, getSpeechRadius()
        );
        Rpc.sendToAllAround(Nametag.broadcastTyping, point, id);
    }

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        event.displayname = getPreferredName(event.entityPlayer);
    }

    protected String getPreferredName(EntityPlayer player) {
        final Character c = CharacterAttribute.get(player).orElse(null);
        return c == null || c.name.isEmpty()
                ? player.getCommandSenderName()
                : c.name;
    }

    static int getSpeechRadius() {
        final int[] speechRadius = Aorta.DEFINES.get().chat.speechRadius;
        final int mid = (speechRadius.length - 1) / 2;
        return speechRadius[mid];
    }
}
