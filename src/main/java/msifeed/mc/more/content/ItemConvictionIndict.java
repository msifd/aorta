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

class ItemConvictionIndict extends Item {
    public static String ITEM_NAME = "conviction_indict";

    ItemConvictionIndict() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(16);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean extra) {
        lines.add(L10n.tr("item.conviction_indict.desc"));
        lines.add(L10n.tr("item.desc.consumable"));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
        if (player.worldObj.isRemote || !(entity instanceof EntityPlayer))
            return false;

        final EntityPlayer target = (EntityPlayer) entity;
        final Character after = CharacterAttribute.require(target);
        final Character before = new Character(after);
        after.sin = Math.min(after.sin + More.DEFINES.get().items.convictionIndictSin, 100);

        player.addChatMessage(new ChatComponentText(L10n.fmt("more.sin.indict", target.getDisplayName())));
        stack.stackSize--;

        CharacterAttribute.INSTANCE.set(target, after);
        Differ.printDiffs((EntityPlayerMP)player, target, before, after);

        return true;
    }
}
