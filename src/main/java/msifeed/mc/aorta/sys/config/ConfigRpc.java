package msifeed.mc.aorta.sys.config;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;

public enum ConfigRpc {
    INSTANCE;

    private static final String sync = "aorta:config.sync";

    public static void sendTo(EntityPlayerMP player, HashMap<String, String> configs) {
        Rpc.sendTo(player, sync, configs);
    }

    public static void broadcast(HashMap<String, String> configs) {
        Rpc.sendToAll(sync, configs);
    }

    @RpcMethod(sync)
    public void onSync(MessageContext ctx, HashMap<String, String> configs) {
        if (ctx.side.isServer())
            return;
        ConfigManager.INSTANCE.onConfigSync(configs);
    }
}
