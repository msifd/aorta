package msifeed.mc.more.content;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemOldEye extends Item {
    public static String ITEM_NAME = "old_eye";

    public ItemOldEye() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(1);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemOldEye || heldItem.getItem() instanceof ItemFragileOldEye))
            return;
        if (event.target instanceof EntityPlayer && !player.worldObj.isRemote) {
            final EntityPlayer target = (EntityPlayer) event.target;
            final Character c = CharacterAttribute.require(target);
            final int sinLevel = c.sinLevel();
            final String sinStr = sinLevel > 0 ? Integer.toString(c.sin) : L10n.fmt("more.sin.none");
            final String sinLevelStr = L10n.fmt("more.status.sin." + sinLevel);
            player.addChatMessage(new ChatComponentText((sinLevel > 0 ? "§4" : sinLevel < 0 ? "§b" : "§f") +
                    L10n.fmt("more.sin.scan", target.getDisplayName(), sinLevelStr, sinStr)));

            if (heldItem.getItem() instanceof ItemFragileOldEye)
                heldItem.stackSize--;
        }
    }
}
