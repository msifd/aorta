package msifeed.mc.extensions.books;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;

public class RemoteBookRpc {
    private static final String checkRequest = Bootstrap.MODID + ":books.check.request";
    private static final String checkResponse = Bootstrap.MODID + ":books.check.response";
    private static final String fetchRequest = Bootstrap.MODID + ":books.fetch.request";
    private static final String fetchResponse = Bootstrap.MODID + ":books.fetch.response";
    private static final String loadRequest = Bootstrap.MODID + ":books.load";
    private static final String publishRequest = Bootstrap.MODID + ":books.publish";

    public static void check(String index) {
        More.RPC.sendToServer(checkRequest, index);
    }

    @RpcMethod(checkRequest)
    public void checkRequest(MessageContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        More.RPC.sendTo(player, checkResponse, RemoteBookManager.INSTANCE.checkBook(index));
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(checkResponse)
    public void checkResponse(MessageContext ctx, boolean exists) {
        RemoteBookManager.INSTANCE.receiveCheck(exists);
    }

    public static void fetch(String index) {
        More.RPC.sendToServer(fetchRequest, index);
    }

    @RpcMethod(fetchRequest)
    public void fetchRequest(MessageContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        More.RPC.sendTo(player, fetchResponse, RemoteBookManager.INSTANCE.readBook(index));
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(fetchResponse)
    public void fetchResponse(MessageContext ctx, String rawBook) {
        RemoteBookManager.INSTANCE.receiveResponse(rawBook);
    }

    public static void load(String index) {
        More.RPC.sendToServer(loadRequest, index);
    }

    @RpcMethod(loadRequest)
    public void loadRequest(MessageContext ctx, String index) {
        RemoteBookManager.INSTANCE.readBook(ctx.getServerHandler().playerEntity, index);
    }

    public static void publish(String text, String title, RemoteBook.Style style) {
        More.RPC.sendToServer(publishRequest, text, title, style.ordinal());
    }

    @RpcMethod(publishRequest)
    public void publishRequest(MessageContext ctx, String text, String title, int style) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        RemoteBookManager.INSTANCE.publishBook(player, text, title, RemoteBook.Style.values()[style]);
    }
}
