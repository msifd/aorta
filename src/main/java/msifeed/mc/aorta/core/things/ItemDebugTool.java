package msifeed.mc.aorta.core.things;

import javafx.util.Pair;
import msifeed.mc.aorta.core.props.CharacterProperty;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.aorta.props.SyncPropHandler;
import msifeed.mc.aorta.things.AortaCreativeTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDebugTool extends Item {
    public static String ITEM_NAME = "tool_debug";

    public ItemDebugTool() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName("aorta:tool_debug");
        setCreativeTab(AortaCreativeTab.TOOLS);
        setMaxStackSize(1);
    }
}
