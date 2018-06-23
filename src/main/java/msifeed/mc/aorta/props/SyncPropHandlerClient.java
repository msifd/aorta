package msifeed.mc.aorta.props;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class SyncPropHandlerClient extends SyncPropHandler {
    @Override
    public IMessage onMessage(SyncPropMessage message, MessageContext ctx) {
        final World clientWorld = FMLClientHandler.instance().getWorldClient();
        final World serverWorld = DimensionManager.getWorld(clientWorld.provider.dimensionId);
        setProp(clientWorld, message);
        if (serverWorld != null)
            setProp(serverWorld, message);
        return null;
    }
}
