package msifeed.mc.aorta.core.things;

import msifeed.mc.aorta.core.character.CharacterProperty;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.aorta.props.SyncProp;
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
        prop.getCharacter().get().features.put(Feature.INTELLIGENCE, Grade.GREAT);
        SyncProp.syncServer(player, prop);

        return super.onItemRightClick(p_77659_1_, p_77659_2_, player);
    }
}
