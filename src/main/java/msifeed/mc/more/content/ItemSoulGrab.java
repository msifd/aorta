package msifeed.mc.more.content;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.extensions.tweaks.EsitenceHealthModifier;
import msifeed.mc.genesis.GenesisCreativeTab;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.Differ;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemSoulGrab extends Item {
    public static String ITEM_NAME = "soul_grab";

    public ItemSoulGrab() {
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
        if (heldItem == null || !(heldItem.getItem() instanceof ItemSoulGrab))
            return;
        
        if (!(event.target instanceof EntityPlayer) || player.worldObj.isRemote)
            return;

        final EntityPlayer target = (EntityPlayer) event.target;
        final Character playerAfter = CharacterAttribute.require(player);
        final Character playerBefore = new Character(playerAfter);
        final ItemDefines config = More.DEFINES.get().items;

        if (playerAfter.estitence >= 100) {
            player.addChatMessage(new ChatComponentText(L10n.fmt("more.est.grab_not_enough", target.getDisplayName())));
            return;
        }

        final Character targetAfter = CharacterAttribute.require(target);
        final Character targetBefore = new Character(targetAfter);

        if (targetAfter.estitence <= 20) {
            player.addChatMessage(new ChatComponentText(L10n.fmt("more.est.grab_too_much")));
            return;
        }

        playerAfter.estitence = Math.min(playerAfter.estitence + config.soulGrabEstitence, 90);
        playerAfter.sin = Math.min(playerAfter.sin + config.soulGrabSin, 100);
        targetAfter.estitence = Math.max(targetAfter.estitence - config.soulGrabEstitence, 10);

        player.addChatMessage(new ChatComponentText(L10n.fmt("more.est.grab_from", target.getDisplayName())));
        target.addChatMessage(new ChatComponentText(L10n.fmt("more.est.grab_to", player.getDisplayName())));
        heldItem.stackSize--;

        CharacterAttribute.INSTANCE.set(player, playerAfter);
        Differ.printDiffs((EntityPlayerMP)player, player, playerBefore, playerAfter);
        if (playerBefore.countMaxHealth() != playerAfter.countMaxHealth())
            EsitenceHealthModifier.applyModifier(player, playerAfter);

        CharacterAttribute.INSTANCE.set(target, targetAfter);
        Differ.printDiffs((EntityPlayerMP)player, target, targetBefore, targetAfter);
        if (targetBefore.countMaxHealth() != targetAfter.countMaxHealth())
            EsitenceHealthModifier.applyModifier(target, targetAfter);
    }
}
