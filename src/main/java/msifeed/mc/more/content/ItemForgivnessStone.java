package msifeed.mc.more.content;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.Differ;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemForgivnessStone extends Item {
    public static String ITEM_NAME = "forgivness_stone";

    public ItemForgivnessStone() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(16);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemForgivnessStone))
            return;
        if (event.target instanceof EntityPlayer && !player.worldObj.isRemote) {
            final EntityPlayer target = (EntityPlayer) event.target;
            final Character after = CharacterAttribute.require(target);
            if (after.sin == 0) {
                player.addChatMessage(new ChatComponentText(L10n.fmt("more.sin.not_sinner", target.getDisplayName())));
                return;
            }
            final Character before = new Character(after);
            after.sin = Math.max(after.sin - More.DEFINES.get().items.forgivnessStoneSin, 0);

            player.addChatMessage(new ChatComponentText(L10n.fmt("more.sin.forgive", target.getDisplayName())));
            heldItem.stackSize--;

            CharacterAttribute.INSTANCE.set(player, after);
            Differ.printDiffs((EntityPlayerMP)player, target, before, after);
        }
    }
}
