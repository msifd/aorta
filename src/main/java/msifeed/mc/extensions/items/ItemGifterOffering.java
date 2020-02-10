package msifeed.mc.extensions.items;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.Differ;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemGifterOffering extends Item {
    public static String ITEM_NAME = "gifter_offering";

    public ItemGifterOffering() {
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
        if (heldItem == null || !(heldItem.getItem() instanceof ItemGifterOffering))
            return;
        if (event.target instanceof EntityPlayer && !player.worldObj.isRemote) {
            final EntityPlayer target = (EntityPlayer) event.target;
            final Character playerAfter = CharacterAttribute.require(player);
            final Character playerBefore = new Character(playerAfter);

            if (playerAfter.estitence <= 10) {
                player.addChatMessage(new ChatComponentText(L10n.fmt("aorta.est.gift_not_enough")));
                return;
            }

            final Character targetAfter = CharacterAttribute.require(target);
            final Character targetBefore = new Character(targetAfter);

            if (targetAfter.estitence >= 90) {
                player.addChatMessage(new ChatComponentText(L10n.fmt("aorta.est.gift_too_much", target.getDisplayName())));
                return;
            }

            playerAfter.estitence--;
            targetAfter.estitence++;
            targetAfter.illness.illness = 0;
            targetAfter.illness.limit = 0;
            targetAfter.illness.treatment = 0;
            target.heal(target.getMaxHealth());

            player.addChatMessage(new ChatComponentText(L10n.fmt("aorta.est.gift_to", target.getDisplayName())));
            target.addChatMessage(new ChatComponentText(L10n.fmt("aorta.est.gift_from", player.getDisplayName())));
            heldItem.stackSize--;

            CharacterAttribute.INSTANCE.set(player, playerAfter);
            Differ.printDiffs((EntityPlayerMP)player, player, playerBefore, playerAfter);
            if (playerBefore.countMaxHP() != playerAfter.countMaxHP())
                player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(playerAfter.countMaxHP());

            CharacterAttribute.INSTANCE.set(target, targetAfter);
            Differ.printDiffs((EntityPlayerMP)player, target, targetBefore, targetAfter);
            if (targetBefore.countMaxHP() != targetAfter.countMaxHP())
                target.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(targetAfter.countMaxHP());
        }
    }
}
