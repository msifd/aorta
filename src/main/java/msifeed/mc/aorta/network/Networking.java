package msifeed.mc.aorta.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;

public enum Networking {
    INSTANCE;

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID);

    public void init() {
        CHANNEL.registerMessage(EntityPropertySync.Handler.class, EntityPropertySync.class, 0x00, Side.CLIENT);
    }
}
