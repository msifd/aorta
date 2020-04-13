package msifeed.mc.more.crabs.meta;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public enum MetaRpc {
    INSTANCE;

    private static final String updateMeta = Bootstrap.MODID + ":core.meta";

    public static void updateMeta(int entityId, MetaInfo meta) {
        Rpc.sendToServer(updateMeta, entityId, meta.toNBT());
    }

    @RpcMethod(updateMeta)
    public void onUpdateMeta(MessageContext ctx, int entityId, NBTTagCompound metaNbt) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity target = sender.worldObj.getEntityByID(entityId);
        if (!(target instanceof EntityLivingBase))
            return;

        if (target instanceof EntityPlayer)
            MetaAttribute.INSTANCE.update(target, meta -> meta.fromNBT(metaNbt));
        else {
            final MetaInfo m = new MetaInfo();
            m.fromNBT(metaNbt);
            MetaAttribute.INSTANCE.set(target, m);
        }
    }
}
