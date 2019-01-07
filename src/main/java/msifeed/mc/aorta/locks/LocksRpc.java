package msifeed.mc.aorta.locks;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.rpc.RpcMethod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class LocksRpc {
    public static final String toggleDigital = "aorta:locks.toggle_digital";
    public static final String resetDigital = "aorta:locks.reset_digital";

    @RpcMethod(toggleDigital)
    public void toggleDigital(MessageContext ctx, int x, int y, int z, String secret) {
        final EntityPlayer player = ctx.getServerHandler().playerEntity;
        final World world = player.getEntityWorld();
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null || lock.getLockType() != LockType.DIGITAL)
            return;

        if (lock.canUnlockWith(secret)) {
            lock.toggleLocked();
            if (!world.isRemote) {
                final String msg = lock.isLocked() ? "aorta.lock.locked" : "aorta.lock.unlocked";
                player.addChatMessage(new ChatComponentTranslation(msg));
            }
        }
    }

    @RpcMethod(resetDigital)
    public void resetDigital(MessageContext ctx, int x, int y, int z, String secret) {
        final EntityPlayer player = ctx.getServerHandler().playerEntity;
        final World world = player.getEntityWorld();
        final LockTileEntity lock = LockTileEntity.find(world, x, y, z);
        if (lock == null || lock.getLockType() != LockType.DIGITAL)
            return;

        if (!lock.isLocked()) {
            lock.setSecret(secret);
            lock.makeToggleSound();
            player.addChatMessage(new ChatComponentTranslation("aorta.lock.reset"));
        }
    }
}
