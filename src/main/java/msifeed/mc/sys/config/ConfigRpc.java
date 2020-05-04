package msifeed.mc.sys.config;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ConfigRpc {
    INSTANCE;

    private static final String sync = Bootstrap.MODID + ":config.sync";

    public static void sendTo(EntityPlayerMP player, HashMap<String, String> configs) {
        Rpc.sendTo(sync, player, pack(configs));
    }

    public static void broadcast(HashMap<String, String> configs) {
        if (serverStarted())
            Rpc.sendToAll(sync, pack(configs));
    }

    @RpcMethod(sync)
    public void onSync(MessageContext ctx, NBTTagCompound compound) {
        if (ctx.side.isServer())
            return;
        ConfigManager.INSTANCE.onConfigSync(unpack(compound));
    }

    private static NBTTagCompound pack(Map<String, String> configs) {
        final NBTTagCompound compound = new NBTTagCompound();
        for (Map.Entry<String, String> e : configs.entrySet())
            compound.setString(e.getKey(), e.getValue());
        return compound;
    }

    private static Map<String, String> unpack(NBTTagCompound compound) {
        final HashMap<String, String> configs = new HashMap<>();
        final Set<String> keys = compound.func_150296_c();
        for (String k : keys)
            configs.put(k, compound.getString(k));
        return configs;
    }

    private static boolean serverStarted() {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        return server != null && server.getConfigurationManager() != null;
    }
}
