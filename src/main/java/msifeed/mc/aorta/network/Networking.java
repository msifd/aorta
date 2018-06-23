package msifeed.mc.aorta.network;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.props.SyncPropHandler;
import msifeed.mc.aorta.props.SyncPropMessage;

public enum Networking {
    INSTANCE;

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID);

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.props.SyncPropHandler",
            clientSide = "msifeed.mc.aorta.props.SyncPropHandlerClient"
    )
    public static SyncPropHandler syncPropHandler;

    public void init() {
        CHANNEL.registerMessage(syncPropHandler, SyncPropMessage.class, 0x00, Side.SERVER);
        CHANNEL.registerMessage(syncPropHandler, SyncPropMessage.class, 0x01, Side.CLIENT);
    }
}
