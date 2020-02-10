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
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public enum MetaRpc {
    INSTANCE;

    private static final String updateMeta = Bootstrap.MODID + ":core.meta";

    public static void updateMeta(int entityId, MetaInfo meta) {
        try {
            Rpc.sendToServer(updateMeta, entityId, CompressedStreamTools.compress(meta.toNBT()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(updateMeta)
    public void onUpdateMeta(MessageContext ctx, int entityId, byte[] metaBytes) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity entity = sender.worldObj.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        try {
            final NBTTagCompound metaNbt = CompressedStreamTools.func_152457_a(metaBytes, new NBTSizeTracker(2097152L));
            if (entity instanceof EntityPlayer)
                MetaAttribute.INSTANCE.update(entity, meta -> meta.fromNBT(metaNbt));
            else {
                final MetaInfo m = new MetaInfo();
                m.fromNBT(metaNbt);
                MetaAttribute.INSTANCE.set(entity, m);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
