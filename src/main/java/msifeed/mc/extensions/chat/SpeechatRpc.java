package msifeed.mc.extensions.chat;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.sys.rpc.RpcMethod;
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

    @RpcMethod(speech)
    public void receiveSpeech(MessageContext ctx, int senderId, IChatComponent cc, int range) {
        SpeechatClient.receiveSpeech(senderId, cc, range);
    }

    public static void sendGlobal(IChatComponent cc) {
        More.RPC.sendToAll(global, cc);
    }

    @RpcMethod(global)
    public void receiveGlobal(MessageContext ctx, IChatComponent cc) {
        SpeechatClient.receiveGlobal(cc);
    }

    public static void sendGmGlobal(IChatComponent cc) {
        More.RPC.sendToAll(gm_global, cc);
    }

    @RpcMethod(gm_global)
    public void receiveGmGlobal(MessageContext ctx, IChatComponent cc) {
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

    @RpcMethod(raw)
    public void receiveRaw(MessageContext ctx, IChatComponent cc) {
        SpeechatClient.receiveRaw(cc);
    }
}
