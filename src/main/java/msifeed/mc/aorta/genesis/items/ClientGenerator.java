package msifeed.mc.aorta.genesis.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.items.client.RenderLargeItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
class ClientGenerator {
    private static RenderLargeItem largeRenderer = new RenderLargeItem();

    static void fillTexture(ItemGenesisUnit unit, Item item) {
        if (unit.texture != null) {
            item.setTextureName(unit.texture);
        }
    }

    static void setDoubleSized(Item item) {
        MinecraftForgeClient.registerItemRenderer(item, largeRenderer);
    }
}
