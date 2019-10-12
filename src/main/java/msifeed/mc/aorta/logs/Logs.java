package msifeed.mc.aorta.logs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;

public enum Logs {
    INSTANCE;

    static final String requestLogCommand = "aorta:logs.cmd";
    private static final DBHandler dbHandler = new DBHandler();

    public static void init() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            MinecraftForge.EVENT_BUS.register(dbHandler);
            MinecraftForge.EVENT_BUS.register(INSTANCE);
            FMLCommonHandler.instance().bus().register(INSTANCE);
            Rpc.register(INSTANCE);
        }
    }

    public static void log(ICommandSender sender, String type, String message) {
        if (FMLCommonHandler.instance().getSide().isServer())
            dbHandler.logCommand(sender, type, message);
    }

    public static void logCommand(String cmd) {
        Rpc.sendToServer(Logs.requestLogCommand, cmd);
    }

    @RpcMethod(requestLogCommand)
    public void onLogCommand(MessageContext ctx, String cmd) {
        log(ctx.getServerHandler().playerEntity, "cmd", "/" + cmd);
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote)
            log(event.player, "log", "[login]");
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.player.worldObj.isRemote)
            log(event.player, "log", "[logout]");
    }
}
