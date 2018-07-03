package msifeed.mc.aorta.genesis.blocks;

import com.google.gson.JsonObject;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.Generator;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.blocks.templates.BlockTemplate;
import msifeed.mc.aorta.genesis.blocks.templates.SlabTemplate;
import msifeed.mc.aorta.genesis.blocks.templates.StairsTemplate;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.HashSet;

import static msifeed.mc.aorta.genesis.GenesisTrait.*;

public class BlockGenerator implements Generator {
    @Override
    public void generate(JsonObject json, HashSet<GenesisTrait> traits) {
        final BlockGenesisUnit unit = new BlockGenesisUnit(json, traits);
        switch (unit.type) {
            case BASIC:
                generateBasic(unit);
                break;
        }
    }

    private void generateBasic(BlockGenesisUnit unit) {
        final BlockTemplate basic = new BlockTemplate(getMaterial(unit), unit.id);
        fillCommons(unit, basic);

        if (unit.traits.contains(rotatable)) {
            basic.rotatable = true;
        }

        GameRegistry.registerBlock(basic, unit.id);

        if (unit.traits.contains(add_stairs))
            generateStairs(unit, basic);
        if (unit.traits.contains(add_slabs))
            generateSlabs(unit, basic);
    }

    private void generateStairs(BlockGenesisUnit unit, Block parent) {
        final String id = unit.id + "_stairs";
        final Block stairs = new StairsTemplate(parent, id);
        fillCommons(unit, stairs);
        GameRegistry.registerBlock(stairs, id);
    }

    private void generateSlabs(BlockGenesisUnit unit, Block parent) {
        final String singleId = unit.id + "_slab";
        final String doubleId = unit.id + "_doubleslab";
        final SlabTemplate slabSingle = new SlabTemplate(parent, false, singleId);
        final SlabTemplate slabDouble = new SlabTemplate(parent, true, doubleId);

        fillCommons(unit, slabSingle);
        fillCommons(unit, slabDouble);
        slabDouble.setCreativeTab(null);

        GameRegistry.registerBlock(slabSingle, SlabTemplate.SlabItem.class, singleId, slabSingle, slabDouble);
        GameRegistry.registerBlock(slabDouble, doubleId);
    }

    private Material getMaterial(BlockGenesisUnit unit) {
        if (unit.traits.contains(wooden))
            return Material.wood;
        else if (unit.traits.contains(stone))
            return Material.rock;
        else if (unit.traits.contains(metal))
            return Material.iron;
        else
            return Material.wood;
    }

    private void fillCommons(BlockGenesisUnit unit, Block block) {
        block.setCreativeTab(AortaCreativeTab.BLOCKS);

        if (unit.traits.contains(unbreakable)) {
            block.setBlockUnbreakable();
            block.setResistance(6000000);
        }

        if (FMLCommonHandler.instance().getSide().isClient()) {
            fillTexture(unit, block);
        }
    }

    @SideOnly(Side.CLIENT)
    private void fillTexture(BlockGenesisUnit unit, Block block) {
        if (unit.textureString != null) {
            block.setBlockTextureName(unit.textureString);
            return;
        }

        if (unit.textureArray != null && unit.textureLayout != null) {
            if (block instanceof BlockTemplate)
                ((BlockTemplate) block).textureLayout = new BlockTextureLayout(unit.textureArray, unit.textureLayout);
            return;
        }

        block.setBlockTextureName("aorta:" + unit.id);
    }
}
