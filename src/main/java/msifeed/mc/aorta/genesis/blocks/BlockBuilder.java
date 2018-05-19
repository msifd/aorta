package msifeed.mc.aorta.genesis.blocks;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.GenesisBuilder;
import msifeed.mc.aorta.genesis.Trait;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.Collection;

import static msifeed.mc.aorta.genesis.Trait.*;
import static msifeed.mc.aorta.genesis.blocks.BlockBuilder.BlockTags.texture;
import static msifeed.mc.aorta.genesis.blocks.BlockBuilder.BlockTags.texture_layout;

public class BlockBuilder extends GenesisBuilder {
    private static final ImmutableSet<Trait> blockTraits = ImmutableSet.of(Trait.block);

    protected GenesisBlock block = null;

    public BlockBuilder(JsonObject json, Collection<Trait> traits) {
        super(json, traits);
    }

    @Override
    public Collection<Trait> specificTraits() {
        return blockTraits;
    }

    @Override
    public void produce() {
        block = new GenesisBlock(getMaterial());
        block.setBlockName(id);
        block.setCreativeTab(AortaCreativeTab.BLOCKS);

        setCommonTraits();

        if (side.isClient()) {
            setTexture();
        }
    }

    @Override
    public void register() {
        GameRegistry.registerBlock(block, id);
    }

    private Material getMaterial() {
        if (traits.contains(wooden))
            return Material.wood;
        else if (traits.contains(stone))
            return Material.rock;
        else if (traits.contains(metal))
            return Material.iron;
        else
            return Material.wood;
    }

    private void setCommonTraits() {
        if (traits.contains(unbreakable)) {
            block.setBlockUnbreakable();
            block.setResistance(6000000);
        }
    }

    @SideOnly(Side.CLIENT)
    private void setTexture() {
        // default - texture is id
        if (!has(texture)) {
            block.setBlockTextureName("aorta:" + id);
            return;
        }

        final JsonElement txEl = get(texture);

        // texture is string - simple layout
        if (isString(txEl)) {
            block.setBlockTextureName(txEl.getAsJsonPrimitive().getAsString());
            return;
        }

        // texture is array - use layout
        if (txEl.isJsonArray()) {
            ArrayList<String> rawTextures = new ArrayList<>();
            for (JsonElement el : txEl.getAsJsonArray())
                rawTextures.add(el.getAsString());

            if (rawTextures.isEmpty())
                return;

            if (has(texture_layout)) {
                // С разметкой
                final String layoutStr = get(texture_layout).getAsJsonPrimitive().getAsString();
                final int[] layout = layoutStr.codePoints()
                        .limit(6)
                        .map(Character::getNumericValue)
                        .map(i -> Math.min(i, 5)) // [0, 5]
                        .toArray();
                block.setTextures(rawTextures, layout);
            } else {
                // Без разметки берем только первую текстуру
                block.setBlockTextureName(rawTextures.get(0));
            }
        }
    }

    private boolean has(BlockTags tag) {
        return json.has(tag.toString());
    }

    private JsonElement get(BlockTags tag) {
        return json.get(tag.toString());
    }

    public enum BlockTags {
        texture, texture_layout
    }
}
