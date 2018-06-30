package msifeed.mc.aorta.genesis.blocks;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.GenesisBuilder;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Collection;

import static msifeed.mc.aorta.genesis.GenesisTrait.*;
import static msifeed.mc.aorta.genesis.blocks.BlockBuilder.BlockTags.texture;
import static msifeed.mc.aorta.genesis.blocks.BlockBuilder.BlockTags.texture_layout;

public class BlockBuilder extends GenesisBuilder {
    private static final ImmutableSet<GenesisTrait> blockTraits = ImmutableSet.of(GenesisTrait.block);

    public BlockBuilder(JsonObject json, Collection<GenesisTrait> traits) {
        super(json, traits);
    }

    @Override
    public Collection<GenesisTrait> specificTraits() {
        return blockTraits;
    }

    @Override
    public void process() {
        final BlockTemplate basic = new BlockTemplate(getMaterial());
        fillBlockCommons(basic, id);
        GameRegistry.registerBlock(basic, id);

        if (traits.contains(rotatable)) {
            basic.rotatable = true;
        }

        if (traits.contains(add_stairs)) {
            final String id = this.id + "_stairs";
            final Block stairs = new StairsTemplate(basic);
            fillBlockCommons(stairs, id);
            GameRegistry.registerBlock(stairs, id);
        }

        if (traits.contains(add_slabs)) {
            final SlabTemplate slabSingle = new SlabTemplate(basic, false);
            final SlabTemplate slabDouble = new SlabTemplate(basic, true);
            fillBlockCommons(slabSingle, slabSingle.getName());
            fillBlockCommons(slabDouble, slabDouble.getName());
            slabDouble.setCreativeTab(null);
            GameRegistry.registerBlock(slabSingle, SlabTemplate.SlabItem.class, slabSingle.getName(), slabSingle, slabDouble);
            slabDouble.item = Item.getItemFromBlock(basic); // Refer to basic block on middle click
            GameRegistry.registerBlock(slabDouble, slabDouble.getName());
        }
    }

    private void fillBlockCommons(Block block, String id) {
        block.setBlockName(id);
        block.setCreativeTab(AortaCreativeTab.BLOCKS);

        if (traits.contains(unbreakable)) {
            block.setBlockUnbreakable();
            block.setResistance(6000000);
        }

        if (side.isClient()) {
            setTexture(block);
        }
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

    @SideOnly(Side.CLIENT)
    private void setTexture(Block block) {
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
        if (block instanceof BlockTemplate && txEl.isJsonArray()) {
            final BlockTemplate blockTemplate = (BlockTemplate) block;
            final ArrayList<String> rawTextures = new ArrayList<>();
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
                blockTemplate.textureLayout = new BlockTextureLayout(rawTextures, layout);
            } else {
                // Без разметки берем только первую текстуру
                blockTemplate.setBlockTextureName(rawTextures.get(0));
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
