package msifeed.mc.aorta.locks.items;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.RollComposer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.rolls.FeatureRoll;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.aorta.genesis.AortaCreativeTab;
import msifeed.mc.aorta.locks.LockObject;
import msifeed.mc.aorta.locks.LockType;
import msifeed.mc.aorta.logs.Logs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class LockpickItem extends Item {
    public static final String ID = "lock_lockpick";

    public LockpickItem() {
        setCreativeTab(AortaCreativeTab.LOCKS);
        setUnlocalizedName(ID);
        setTextureName("aorta:" + ID);
    }

    protected boolean canPick(LockObject lock) {
        return lock.getLockType() == LockType.BUILD_IN || lock.getLockType() == LockType.PADLOCK;
    }

    protected boolean rollPick(LockObject lock, ItemStack pick, EntityPlayer player) {
        if (lock.getDifficulty() >= 100)
            return false;

        final Character character = CharacterAttribute.require(player);
        final MetaInfo meta = MetaAttribute.require(player);

        final FeatureRoll roll = new FeatureRoll(character, meta, Feature.DEX, Feature.INT);
        consumePick(lock, pick, player, roll);

        if (player instanceof EntityPlayerMP) {
            final String text = RollComposer.makeText(player, roll);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage((EntityPlayerMP) player, m);
        }

        return roll.check(lock.getDifficulty());
    }

    protected void consumePick(LockObject lock, ItemStack pick, EntityPlayer player, FeatureRoll roll) {
        if (lock.getTileEntity().getWorldObj().isRemote)
            return;
        if (roll.result <= lock.getDifficulty() - Aorta.DEFINES.get().locks.pickBreakOffset) {
            pick.stackSize--;
            makeBreakSound(lock);
            player.addChatMessage(new ChatComponentTranslation("aorta.lock.pick_break"));
        }
    }

    protected void makeBreakSound(LockObject lock) {
        final TileEntity te = lock.getTileEntity();
        te.getWorldObj().playSoundEffect(te.xCoord, te.yCoord, te.zCoord, "random.break", 0.3f, 3);
    }

    protected void doPick(LockObject lock) {
        lock.setLocked(!lock.isLocked());
    }

    protected void successMessage(LockObject lock, EntityPlayer player) {
        if (lock.isLocked())
            player.addChatMessage(new ChatComponentTranslation("aorta.lock.locked"));
        else
            player.addChatMessage(new ChatComponentTranslation("aorta.lock.unlocked"));
    }

    private boolean tryToPick(LockObject lock, ItemStack pick, EntityPlayer player) {
        if (rollPick(lock, pick, player)) {
            doPick(lock);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final LockObject lock = LockObject.find(world, x, y, z);
        if (lock == null || !lock.hasLock())
            return false;

        if (canPick(lock) && tryToPick(lock, itemStack, player)) {
            if (!world.isRemote) {
                successMessage(lock, player);
                Logs.log(player, "lockpick", lock.isLocked() ? "[locked]" : "[unlocked]");
            }
        }

        return true;
    }
}
