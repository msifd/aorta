package msifeed.mc.more.content;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.Differ;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.List;

class ItemForgivnessStone extends Item {
    static String ITEM_NAME = "forgivness_stone";

    ItemForgivnessStone() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(16);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean extra) {
        lines.add(L10n.tr("item.forgivness_stone.desc1"));
        lines.add(L10n.tr("item.forgivness_stone.desc2"));
        lines.add(L10n.tr("item.desc.consumable"));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer))
            return false;
        if (player.worldObj.isRemote)
            return true;

        final EntityPlayer target = (EntityPlayer) entity;
        final Character after = CharacterAttribute.require(target);
        if (after.sin == 0) {
            player.addChatMessage(new ChatComponentText(L10n.fmt("more.sin.not_sinner", target.getDisplayName())));
            return true;
        }
        final Character before = new Character(after);
        after.sin = Math.max(after.sin - More.DEFINES.get().items.forgivnessStoneSin, 0);

        player.addChatMessage(new ChatComponentText(L10n.fmt("more.sin.forgive", target.getDisplayName())));
        stack.stackSize--;

        CharacterAttribute.INSTANCE.set(player, after);
        Differ.printDiffs((EntityPlayerMP)player, target, before, after);

        return true;
    }
}
