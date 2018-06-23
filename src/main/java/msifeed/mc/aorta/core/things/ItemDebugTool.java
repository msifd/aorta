package msifeed.mc.aorta.core.things;

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

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer player) {
        CharacterProperty prop = CharacterProperty.get(player);
        prop.getCharacter().ifPresent(character -> {
            character.features.put(Feature.INTELLIGENCE, Grade.GREAT);
            prop.syncServer(player);
        });

        return super.onItemRightClick(p_77659_1_, p_77659_2_, player);
    }
}
