package msifeed.mc.more.crabs.meta;

import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.utils.GetUtils;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.rpc.RpcContext;
import msifeed.mc.sys.rpc.RpcException;
import msifeed.mc.sys.rpc.RpcMethodHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public enum MetaRpc {
    INSTANCE;

    private static final String updateMeta = Bootstrap.MODID + ":core.meta";

    public static void updateMeta(int entityId, MetaInfo meta) {
        More.RPC.sendToServer(updateMeta, entityId, meta.toNBT());
    }

    @RpcMethodHandler(updateMeta)
    public void onUpdateMeta(RpcContext ctx, int entityId, NBTTagCompound metaNbt) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcException(sender, "invalid target entity"));

        if (target instanceof EntityPlayer)
            MetaAttribute.INSTANCE.update(target, meta -> meta.fromNBT(metaNbt));
        else {
            final MetaInfo m = new MetaInfo();
            m.fromNBT(metaNbt);
            MetaAttribute.INSTANCE.set(target, m);
        }
    }
}
