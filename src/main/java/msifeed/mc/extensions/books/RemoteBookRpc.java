package msifeed.mc.extensions.books;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.sys.rpc.RpcContext;
import msifeed.mc.sys.rpc.RpcMethodHandler;
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

    @RpcMethodHandler(checkRequest)
    public void checkRequest(RpcContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        More.RPC.sendTo(player, checkResponse, RemoteBookManager.INSTANCE.checkBook(index));
    }

    @SideOnly(Side.CLIENT)
    @RpcMethodHandler(checkResponse)
    public void checkResponse(RpcContext ctx, boolean exists) {
        RemoteBookManager.INSTANCE.receiveCheck(exists);
    }

    public static void fetch(String index) {
        More.RPC.sendToServer(fetchRequest, index);
    }

    @RpcMethodHandler(fetchRequest)
    public void fetchRequest(RpcContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        More.RPC.sendTo(player, fetchResponse, RemoteBookManager.INSTANCE.readBook(index));
    }

    @SideOnly(Side.CLIENT)
    @RpcMethodHandler(fetchResponse)
    public void fetchResponse(RpcContext ctx, String rawBook) {
        RemoteBookManager.INSTANCE.receiveResponse(rawBook);
    }

    public static void load(String index) {
        More.RPC.sendToServer(loadRequest, index);
    }

    @RpcMethodHandler(loadRequest)
    public void loadRequest(RpcContext ctx, String index) {
        RemoteBookManager.INSTANCE.readBook(ctx.getServerHandler().playerEntity, index);
    }

    public static void publish(String text, String title, RemoteBook.Style style) {
        More.RPC.sendToServer(publishRequest, text, title, style.ordinal());
    }

    @RpcMethodHandler(publishRequest)
    public void publishRequest(RpcContext ctx, String text, String title, int style) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        RemoteBookManager.INSTANCE.publishBook(player, text, title, RemoteBook.Style.values()[style]);
    }
}
