package msifeed.mc.aorta.core.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rules.FightAction;
import net.minecraft.entity.EntityLivingBase;

public enum RollRequests {
    INSTANCE;

    private static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID + ".core.roll");

    public static void init() {
        CHANNEL.registerMessage(FeatureRollMessage.class, FeatureRollMessage.class, 0x00, Side.SERVER);
        CHANNEL.registerMessage(FightRollMessage.class, FightRollMessage.class, 0x01, Side.SERVER);
    }

    @SideOnly(Side.CLIENT)
    public static void rollFeature(EntityLivingBase entity, Feature feat, int mod) {
        final FeatureRollMessage msg = new FeatureRollMessage();
        msg.entityId = entity.getEntityId();
        msg.feature = feat;
        msg.mod = mod;
        CHANNEL.sendToServer(msg);
    }

    @SideOnly(Side.CLIENT)
    public static void rollAction(EntityLivingBase entity, FightAction action, int mod) {
        final FightRollMessage msg = new FightRollMessage();
        msg.entityId = entity.getEntityId();
        msg.action = action;
        msg.mod = mod;
        CHANNEL.sendToServer(msg);
    }
}
