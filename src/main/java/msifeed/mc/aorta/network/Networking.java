package msifeed.mc.aorta.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.props.MessageSyncProp;
import msifeed.mc.aorta.props.SyncProp;

public enum Networking {
    INSTANCE;

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID);

    public void init() {
        CHANNEL.registerMessage(SyncProp.class, MessageSyncProp.class, 0x00, Side.CLIENT);
        CHANNEL.registerMessage(SyncProp.class, MessageSyncProp.class, 0x01, Side.SERVER);
    }
}
