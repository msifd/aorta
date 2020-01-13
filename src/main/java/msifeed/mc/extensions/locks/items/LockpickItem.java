package msifeed.mc.extensions.locks.items;

import msifeed.mc.Bootstrap;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.rolls.FeatureRoll;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.RollComposer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.extensions.locks.LockObject;
import msifeed.mc.extensions.locks.LockType;
import msifeed.mc.extensions.locks.Locks;
import msifeed.mc.sys.utils.ChatUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class LockpickItem extends Item {
    public static final String ID = "lock_lockpick";

    public LockpickItem() {
        setCreativeTab(Locks.LOCKS);
        setUnlocalizedName(ID);
        setTextureName(Bootstrap.MODID + ":" + ID);
    }

    protected boolean canPick(LockObject lock) {
        return lock.getLockType() == LockType.BUILD_IN || lock.getLockType() == LockType.PADLOCK;
    }

    protected boolean rollPick(LockObject lock, ItemStack pick, EntityPlayer player) {
        if (lock.getDifficulty() >= 100)
            return false;

        final Character character = CharacterAttribute.require(player);
        final MetaInfo meta = MetaAttribute.require(player);

        final FeatureRoll roll = new FeatureRoll(character, meta, "", Feature.HND);

        final String text = RollComposer.makeText(player, character, roll);
        final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
        ChatHandler.sendSystemChatMessage(player, m);
        ExternalLogs.log(player, "feature", ChatUtils.stripFormatting(text));

        consumePick(lock, pick, player, roll);

        return roll.check(lock.getDifficulty());
    }

    protected void consumePick(LockObject lock, ItemStack pick, EntityPlayer player, FeatureRoll roll) {
        if (roll != null && roll.result <= lock.getDifficulty() - Aorta.DEFINES.get().locks.pickBreakOffset) {
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

        if (world.isRemote)
            return false;

        if (canPick(lock) && tryToPick(lock, itemStack, player)) {
            successMessage(lock, player);
            ExternalLogs.log(player, "log", lock.isLocked() ? "[locked]" : "[unlocked]");
        }

        return true;
    }
}
