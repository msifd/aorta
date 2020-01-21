package msifeed.mc.genesis;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import msifeed.mc.genesis.blocks.client.GenesisBlockRenderer;
import msifeed.mc.genesis.blocks.client.GenesisChestEntityRenderer;
import msifeed.mc.genesis.blocks.client.GenesisChestRenderer;
import msifeed.mc.genesis.blocks.templates.ChestTemplate;
import msifeed.mc.genesis.content.EmptySignBlock;
import msifeed.mc.genesis.content.EmptySignEntityRenderer;

public class GenesisClient extends Genesis {
    @Override
    public void init() {
        super.init();

        RenderingRegistry.registerBlockHandler(new GenesisBlockRenderer());
        RenderingRegistry.registerBlockHandler(new GenesisChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ChestTemplate.ChestEntity.class, new GenesisChestEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EmptySignBlock.Tile.class, new EmptySignEntityRenderer());
    }
}
