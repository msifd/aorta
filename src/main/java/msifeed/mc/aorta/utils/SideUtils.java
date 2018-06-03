package msifeed.mc.aorta.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;

public class SideUtils {
    public static boolean isServer() {
        final MinecraftServer ms = FMLCommonHandler.instance().getMinecraftServerInstance();
        return ms != null && ms.isServerRunning();
    }

    public static boolean isClient() {
        return !isServer();
    }
}
