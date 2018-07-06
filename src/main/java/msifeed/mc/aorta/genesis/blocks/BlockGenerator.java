package msifeed.mc.aorta.genesis.blocks;

import com.google.gson.JsonObject;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.genesis.Generator;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.blocks.templates.BlockTemplate;
import msifeed.mc.aorta.genesis.blocks.templates.ContainerTemplate;
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

        final Block block = makeBaseBlock(unit);
        fillCommons(unit, block);

        if (unit.traits.contains(rotatable)) {
            ((BlockTraitCommons.Getter) block).getCommons().rotatable = true;
        }
        else if (unit.traits.contains(pillar)) {
            ((BlockTraitCommons.Getter) block).getCommons().pillar = true;
        }

        GameRegistry.registerBlock(block, unit.id);

        if (unit.traits.contains(add_stairs))
            generateStairs(unit, block);
        if (unit.traits.contains(add_slabs))
            generateSlabs(unit, block);
    }

    private Block makeBaseBlock(BlockGenesisUnit unit) {
        if (unit.traits.contains(container)) {
            final int rows;
            if (unit.traits.contains(large))
                rows = 6;
            else if (unit.traits.contains(small))
                rows = 1;
            else
                rows = 3;
            return new ContainerTemplate(getMaterial(unit), unit.id, rows);
        }
        else
            return new BlockTemplate(getMaterial(unit), unit.id);
    }

    private void generateStairs(BlockGenesisUnit unit, Block parent) {
        final String id = unit.id + "_stairs";
        final Block stairs = new StairsTemplate(parent, id, ((BlockTraitCommons.Getter) parent).getCommons());
        fillCommons(unit, stairs);
        GameRegistry.registerBlock(stairs, id);
    }

    private void generateSlabs(BlockGenesisUnit unit, Block parent) {
        final BlockTraitCommons commons = ((BlockTraitCommons.Getter) parent).getCommons();
        final String singleId = unit.id + "_slab";
        final String doubleId = unit.id + "_doubleslab";
        final SlabTemplate slabSingle = new SlabTemplate(parent, false, singleId, commons);
        final SlabTemplate slabDouble = new SlabTemplate(parent, true, doubleId, commons);

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
            ((BlockTraitCommons.Getter) block).getCommons().textureLayout = new BlockTextureLayout(unit.textureArray, unit.textureLayout);
            return;
        }

        block.setBlockTextureName("aorta:" + unit.id);
    }
}
