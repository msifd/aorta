package msifeed.mc.aorta.books;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;

public class RemoteBookRpc {
    private static final String checkRequest = "aorta:books.check.request";
    private static final String checkResponse = "aorta:books.check.response";
    private static final String fetchRequest = "aorta:books.fetch.request";
    private static final String fetchResponse = "aorta:books.fetch.response";
    private static final String loadRequest = "aorta:books.load";
    private static final String publishRequest = "aorta:books.publish";

    public static void check(String index) {
        Rpc.sendToServer(checkRequest, index);
    }

    @RpcMethod(checkRequest)
    public void checkRequest(MessageContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        Rpc.sendTo(player, checkResponse, RemoteBookManager.INSTANCE.checkBook(index));
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(checkResponse)
    public void checkResponse(MessageContext ctx, boolean exists) {
        RemoteBookManager.INSTANCE.receiveCheck(exists);
    }

    public static void fetch(String index) {
        Rpc.sendToServer(fetchRequest, index);
    }

    @RpcMethod(fetchRequest)
    public void fetchRequest(MessageContext ctx, String index) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        Rpc.sendTo(player, fetchResponse, RemoteBookManager.INSTANCE.readBook(index));
    }

    @SideOnly(Side.CLIENT)
    @RpcMethod(fetchResponse)
    public void fetchResponse(MessageContext ctx, String rawBook) {
        RemoteBookManager.INSTANCE.receiveResponse(rawBook);
    }

    public static void load(String index) {
        Rpc.sendToServer(loadRequest, index);
    }

    @RpcMethod(loadRequest)
    public void loadRequest(MessageContext ctx, String index) {
        RemoteBookManager.INSTANCE.readBook(ctx.getServerHandler().playerEntity, index);
    }

    public static void publish(String text, String title, RemoteBook.Style style, Language lang) {
        Rpc.sendToServer(publishRequest, text, title, style, lang);
    }

    @RpcMethod(publishRequest)
    public void publishRequest(MessageContext ctx, String text, String title, RemoteBook.Style style, Language lang) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        RemoteBookManager.INSTANCE.publishBook(player, text, title, style, lang);
    }
}
