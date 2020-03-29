package msifeed.mc.more.content;

import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.List;

class ItemOldEye extends Item {
    static String NORMAL_ITEM_NAME = "old_eye";
    static String FRAGILE_ITEM_NAME = "fragile_old_eye";

    private final boolean fragile;

    private ItemOldEye(boolean fragile) {
        this.fragile = fragile;

        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(1);
    }

    static ItemOldEye makeNormal() {
        final ItemOldEye item = new ItemOldEye(false);
        item.setUnlocalizedName(NORMAL_ITEM_NAME);
        item.setTextureName(Bootstrap.MODID + ":" + NORMAL_ITEM_NAME);
        return item;
    }

    static ItemOldEye makeFragile() {
        final ItemOldEye item = new ItemOldEye(true);
        item.setUnlocalizedName(FRAGILE_ITEM_NAME);
        item.setTextureName(Bootstrap.MODID + ":" + FRAGILE_ITEM_NAME);
        return item;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean extra) {
        lines.add(L10n.tr("item.old_eye.desc1"));
        lines.add(L10n.tr("item.old_eye.desc2"));
        if (fragile)
            lines.add(L10n.tr("item.desc.consumable"));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer))
            return false;
        if (player.worldObj.isRemote)
            return true;

        final EntityPlayer target = (EntityPlayer) entity;
        final Character c = CharacterAttribute.require(target);

        final int sinLevel = c.sinLevel();
        final String sinStr = sinLevel > 0 ? Integer.toString(c.sin) : L10n.fmt("more.sin.none");
        final String sinLevelStr = L10n.fmt("more.status.sin." + sinLevel);
        player.addChatMessage(new ChatComponentText((sinLevel > 0 ? "§4" : sinLevel < 0 ? "§b" : "§f") +
                L10n.fmt("more.sin.scan", target.getDisplayName(), sinLevelStr, sinStr)));

        if (fragile)
            stack.stackSize--;

        return true;
    }
}
