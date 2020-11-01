package msifeed.mc.extensions.chat;

import cpw.mods.fml.common.network.NetworkRegistry;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.sys.rpc.RpcContext;
import msifeed.mc.sys.rpc.RpcMethodHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;

public final class SpeechatRpc {
    private static final String speech = Bootstrap.MODID + ":chat.speech";
    private static final String global = Bootstrap.MODID + ":chat.global";
    private static final String gm_global = Bootstrap.MODID + ":chat.gm.global";
    private static final String raw = Bootstrap.MODID + ":chat.raw";

    public static void sendSpeech(EntityPlayerMP sender, int range, IChatComponent cc) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, range);
        More.RPC.sendToAllAround(point, speech, sender.getEntityId(), cc, range);
    }

    @RpcMethodHandler(speech)
    public void receiveSpeech(RpcContext ctx, int senderId, IChatComponent cc, int range) {
        SpeechatClient.receiveSpeech(senderId, cc, range);
    }

    public static void sendGlobal(IChatComponent cc) {
        More.RPC.sendToAll(global, cc);
    }

    @RpcMethodHandler(global)
    public void receiveGlobal(RpcContext ctx, IChatComponent cc) {
        SpeechatClient.receiveGlobal(cc);
    }

    public static void sendGmGlobal(IChatComponent cc) {
        More.RPC.sendToAll(gm_global, cc);
    }

    @RpcMethodHandler(gm_global)
    public void receiveGmGlobal(RpcContext ctx, IChatComponent cc) {
        SpeechatClient.receiveGmGlobal(cc);
    }

    public static void sendRaw(EntityLivingBase sender, int range, IChatComponent cc) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, range);
        More.RPC.sendToAllAround(point, raw, cc);
    }

    public static void sendRaw(NetworkRegistry.TargetPoint point, IChatComponent cc) {
        More.RPC.sendToAllAround(point, raw, cc);
    }

    public static void sendRawTo(EntityPlayerMP receiver, IChatComponent cc) {
        More.RPC.sendTo(receiver, raw, cc);
    }

    @RpcMethodHandler(raw)
    public void receiveRaw(RpcContext ctx, IChatComponent cc) {
        SpeechatClient.receiveRaw(cc);
    }
}
