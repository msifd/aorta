package msifeed.mc.aorta.genesis;

import cpw.mods.fml.client.registry.RenderingRegistry;
import msifeed.mc.aorta.genesis.blocks.GenesisBlockRenderer;

public class GenesisClient extends Genesis {
    public GenesisClient() {
        RenderingRegistry.registerBlockHandler(new GenesisBlockRenderer());
    }
}
