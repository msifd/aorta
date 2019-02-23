package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.RollComposer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.FeatureRoll;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.LockType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.Optional;

public class LockpickItem extends Item {
    public static final String ID = "lock_lockpick";

    public LockpickItem() {
        setCreativeTab(AortaCreativeTab.LOCKS);
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    protected boolean canPick(LockTileEntity lock) {
        return lock.getLockType() == LockType.BUILD_IN || lock.getLockType() == LockType.PADLOCK;
    }

    protected boolean rollPick(LockTileEntity lock, ItemStack pick, EntityPlayer player) {
        if (lock.getDifficulty() >= 100)
            return false;

        final Optional<Character> charOpt = CharacterAttribute.get(player);
        final Optional<CharStatus> statusOpt = StatusAttribute.get(player);
        if (!charOpt.isPresent() || !statusOpt.isPresent())
            return false;

        final FeatureRoll roll = new FeatureRoll(charOpt.get(), statusOpt.get(), Feature.DEX, Feature.INT);
        consumePick(lock, pick, player, roll);

        if (player instanceof EntityPlayerMP) {
            final String text = RollComposer.makeText(player, roll);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage((EntityPlayerMP) player, m);
        }

        return roll.check(lock.getDifficulty());
    }

    protected void consumePick(LockTileEntity lock, ItemStack pick, EntityPlayer player, FeatureRoll roll) {
        if (lock.getWorldObj().isRemote)
            return;
        if (roll.result <= lock.getDifficulty() - Aorta.DEFINES.get().locks.pickBreakOffset) {
            pick.stackSize--;
            makeBreakSound(lock);
            player.addChatMessage(new ChatComponentTranslation("aorta.lock.pick_break"));
        }
    }

    protected void makeBreakSound(LockTileEntity lock) {
        lock.getWorldObj().playSoundEffect(lock.xCoord, lock.yCoord, lock.zCoord, "random.break", 0.3f, 3);
    }

    protected void doPick(LockTileEntity lock) {
        lock.setLocked(!lock.isLocked());
    }

    protected void successMessage(LockTileEntity lock, EntityPlayer player) {
        if (lock.isLocked())
            player.addChatMessage(new ChatComponentTranslation("aorta.lock.locked"));
        else
            player.addChatMessage(new ChatComponentTranslation("aorta.lock.unlocked"));
    }

    private boolean tryToPick(LockTileEntity lock, ItemStack pick, EntityPlayer player) {
        if (rollPick(lock, pick, player)) {
            doPick(lock);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null || !lock.hasLock())
            return false;

        if (canPick(lock) && tryToPick(lock, itemStack, player)) {
            if (!world.isRemote)
                successMessage(lock, player);
        }

        return true;
    }
}
