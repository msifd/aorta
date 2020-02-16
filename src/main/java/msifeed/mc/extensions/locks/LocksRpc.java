package msifeed.mc.extensions.locks;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class LocksRpc {
    public static final String gmOverrideLock = Bootstrap.MODID + ":locks.gm_override";
    public static final String toggleDigital = Bootstrap.MODID + ":locks.toggle_digital";
    public static final String resetDigital = Bootstrap.MODID + ":locks.reset_digital";

    @RpcMethod(gmOverrideLock)
    public void gmOverrideLock(MessageContext ctx, int x, int y, int z, boolean locked, int diff) {
        final EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (!CharacterAttribute.has(player, Trait.gm))
            return;

        final World world = player.getEntityWorld();
        final LockObject lock = LockObject.find(world, x, y, z);
        if (lock == null)
            return;

        lock.setLocked(locked);
        lock.setDifficulty(diff);

        final String msg = String.format("Lock overridden; locked: %b, diff: %d", locked, diff);
        player.addChatMessage(new ChatComponentText(msg));
    }

    @RpcMethod(toggleDigital)
    public void toggleDigital(MessageContext ctx, int x, int y, int z, String secret) {
        final EntityPlayer player = ctx.getServerHandler().playerEntity;
        final World world = player.getEntityWorld();
        final LockObject lock = LockObject.find(world, x, y, z);
        if (lock == null || lock.getLockType() != LockType.DIGITAL)
            return;

        if (lock.canUnlockWith(secret)) {
            lock.toggleLocked();
            final String msg = lock.isLocked() ? "more.lock.locked" : "more.lock.unlocked";
            player.addChatMessage(new ChatComponentTranslation(msg));
        }
    }

    @RpcMethod(resetDigital)
    public void resetDigital(MessageContext ctx, int x, int y, int z, String secret) {
        final EntityPlayer player = ctx.getServerHandler().playerEntity;
        final World world = player.getEntityWorld();
        final LockObject lock = LockObject.find(world, x, y, z);
        if (lock == null || lock.getLockType() != LockType.DIGITAL)
            return;

        if (!lock.isLocked()) {
            lock.setSecret(secret);
            lock.makeToggleSound();
            player.addChatMessage(new ChatComponentTranslation("more.lock.reset"));
        }
    }
}
