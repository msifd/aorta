package msifeed.mc.aorta.books;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.rpc.Rpc;
import msifeed.mc.aorta.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;

public class RemoteBookRpc {
    static final String checkRequest = "aorta:books.check.request";
    static final String fetchRequest = "aorta:books.fetch.request";
    static final String signRequest = "aorta:books.sign";
    static final String checkResponse = "aorta:books.check.response";
    static final String fetchResponse = "aorta:books.fetch.response";

    @RpcMethod(checkRequest)
    public void checkRequest(MessageContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        Rpc.sendTo(player, checkResponse, RemoteBookManager.INSTANCE.checkBook(index));
    }

    @RpcMethod(fetchRequest)
    public void fetchRequest(MessageContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        Rpc.sendTo(player, fetchResponse, RemoteBookManager.INSTANCE.loadBook(index));
    }

    @RpcMethod(signRequest)
    public void signRequest(MessageContext ctx, String index) {
        RemoteBookManager.INSTANCE.signBook(ctx.getServerHandler().playerEntity, index);
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(checkResponse)
    public void checkResponse(MessageContext ctx, boolean exists) {
        RemoteBookManager.INSTANCE.receiveCheck(exists);
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(fetchResponse)
    public void fetchResponse(MessageContext ctx, String rawBook) {
        RemoteBookManager.INSTANCE.receiveResponse(rawBook);
    }
}
