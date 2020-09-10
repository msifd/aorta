package msifeed.mc.sys.config;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.nio.charset.StandardCharsets;

public enum ConfigRpc {
    INSTANCE;

    private static final String sync = Bootstrap.MODID + ":config.sync";

    public static void sendTo(EntityPlayerMP player, JsonConfig cfg) {
        More.RPC.sendTo(player, sync, cfg.getFilename(), cfg.toJson().getBytes(StandardCharsets.UTF_8));
    }

    public static void broadcast(JsonConfig cfg) {
        if (!serverStarted()) return;
        More.RPC.sendToAll(sync, cfg.getFilename(), cfg.toJson().getBytes(StandardCharsets.UTF_8));
    }

    @RpcMethod(sync)
    public void onSync(MessageContext ctx, String filename, byte[] compressed) {
        if (ctx.side.isServer()) return;
        ConfigManager.INSTANCE.applySyncConfig(filename, new String(compressed, StandardCharsets.UTF_8));
    }

    private static boolean serverStarted() {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        return server != null && server.getConfigurationManager() != null;
    }
}
