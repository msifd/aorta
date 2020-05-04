package msifeed.mc.extensions.chat;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.rpc.Rpc;
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
        Rpc.sendToAllAround(speech, point, sender.getEntityId(), cc, range);
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(speech)
    public void receiveSpeech(MessageContext ctx, int senderId, IChatComponent cc, int range) {
        SpeechatClient.receiveSpeech(senderId, cc, range);
    }

    public static void sendGlobal(IChatComponent cc) {
        Rpc.sendToAll(global, cc);
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(global)
    public void receiveGlobal(MessageContext ctx, IChatComponent cc) {
        SpeechatClient.receiveGlobal(cc);
    }

    public static void sendGmGlobal(IChatComponent cc) {
        Rpc.sendToAll(gm_global, cc);
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(gm_global)
    public void receiveGmGlobal(MessageContext ctx, IChatComponent cc) {
        SpeechatClient.receiveGmGlobal(cc);
    }

    public static void sendRaw(EntityLivingBase sender, int range, IChatComponent cc) {
        final NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(sender.dimension, sender.posX, sender.posY, sender.posZ, range);
        Rpc.sendToAllAround(raw, point, cc);
    }

    public static void sendRaw(NetworkRegistry.TargetPoint point, IChatComponent cc) {
        Rpc.sendToAllAround(raw, point, cc);
    }

    public static void sendRawTo(EntityPlayerMP receiver, IChatComponent cc) {
        Rpc.sendTo(raw, receiver, cc);
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(raw)
    public void receiveRaw(MessageContext ctx, IChatComponent cc) {
        SpeechatClient.receiveRaw(cc);
    }
}
