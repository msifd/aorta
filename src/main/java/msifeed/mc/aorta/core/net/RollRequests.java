package msifeed.mc.aorta.core.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Feature;

public enum RollRequests {
    INSTANCE;

    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID + ".core.roll");

    public static void init() {
        CHANNEL.registerMessage(FeatureRollMessage.class, FeatureRollMessage.class, 0x00, Side.SERVER);
    }

    @SideOnly(Side.CLIENT)
    public static void rollFeature(Feature feat, int mod) {
        final FeatureRollMessage msg = new FeatureRollMessage();
        msg.feature = feat;
        msg.mod = mod;
        CHANNEL.sendToServer(msg);
    }
}
