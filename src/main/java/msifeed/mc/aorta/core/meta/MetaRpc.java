package msifeed.mc.aorta.core.meta;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.IOException;
import java.util.ArrayList;

public enum MetaRpc {
    INSTANCE;

    private static final String updateMeta = "aorta:core.meta";
    private static final String requestHand = "aorta:core.hand.req";
    private static final String responseHand = "aorta:core.hand.res";

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
            MetaAttribute.INSTANCE.update(entity, meta -> meta.fromNBT(metaNbt));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestHand(int entityId) {
        Rpc.sendToServer(requestHand, entityId);
    }

    @RpcMethod(value = requestHand)
    public void onRequestHand(MessageContext ctx, int entityId) {
        if (ctx.side == Side.CLIENT)
            return;

        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity targetEntity = sender.worldObj.getEntityByID(entityId);
        if (!(targetEntity instanceof EntityPlayer))
            return;
        final EntityPlayer target = (EntityPlayer) targetEntity;

        final ItemStack[] inv = target.inventory.mainInventory;
        final NBTTagList itemsNbt = new NBTTagList();

        for (int i = 0; i < 9; i++) {
            final ItemStack is = inv[i];
            if (is == null)
                continue;
            final NBTTagCompound tag = new NBTTagCompound();
            is.writeToNBT(tag);
            itemsNbt.appendTag(tag);
        }

        final NBTTagCompound root = new NBTTagCompound();
        root.setTag("i", itemsNbt);

        try {
            Rpc.sendTo(sender, responseHand, entityId, CompressedStreamTools.compress(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(responseHand)
    public void onResponseHand(MessageContext ctx, int entityId, byte[] nbtBytes) {
        try {
            final NBTTagCompound root = CompressedStreamTools.func_152457_a(nbtBytes, new NBTSizeTracker(2097152L));
            final NBTTagList itemsNbt = root.getTagList("i", 10); // 10 - NBTTagCompound

            final ArrayList<ItemStack> items = new ArrayList<>(itemsNbt.tagCount());
            for (int i = 0; i < itemsNbt.tagCount(); i++)
                items.add(ItemStack.loadItemStackFromNBT(itemsNbt.getCompoundTagAt(i)));

            CharHand.setHand(entityId, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
