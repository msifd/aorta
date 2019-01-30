package msifeed.mc.aorta.environment.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import msifeed.mc.aorta.environment.EnvHandler;
import msifeed.mc.aorta.environment.EnvironmentManager;
import msifeed.mc.aorta.environment.WorldEnv;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class EnvHandlerClient extends EnvHandler {
    @Override
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        super.onWorldTick(event);
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;
        final World w = Minecraft.getMinecraft().theWorld;
        if (w == null)
            return;
        final WorldEnv env = EnvironmentManager.getEnv(w.provider.dimensionId);
        handleTime(w, env);
    }
}
