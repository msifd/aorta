package msifeed.mc.extensions.items;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.Differ;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemConvictionIndict extends Item {
    public static String ITEM_NAME = "conviction_indict";

    public ItemConvictionIndict() {
        setUnlocalizedName(ITEM_NAME);
        setTextureName(Bootstrap.MODID + ":" + ITEM_NAME);
        setCreativeTab(GenesisCreativeTab.ITEMS);
        setMaxStackSize(16);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 0;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemConvictionIndict))
            return;
        if (event.target instanceof EntityPlayer && !player.worldObj.isRemote) {
            final EntityPlayer target = (EntityPlayer) event.target;
            final Character after = CharacterAttribute.require(target);
            final Character before = new Character(after);
            after.sinfulness = (byte)Math.min(after.sinfulness + 1, 100);

            player.addChatMessage(new ChatComponentText(L10n.fmt("aorta.sin.indict", target.getDisplayName())));
            heldItem.stackSize--;

            CharacterAttribute.INSTANCE.set(player, after);
            Differ.printDiffs((EntityPlayerMP)player, target, before, after);
        }
    }
}
