package msifeed.mc.aorta.core.character;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.chat.usage.LangAttribute;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.logs.Logs;
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
import net.minecraft.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public enum CharRpc {
    INSTANCE;

    private static final String setLang = "aorta:core.char.lang";
    private static final String updateChar = "aorta:core.char.char";
    private static final String updateStatus = "aorta:core.char.status";
    private static final String refreshName = "aorta:core.char.refreshName";
    private static final String clearEntity = "aorta:core.char.clear";
    private static final String requestHand = "aorta:core.char.hand.req";
    private static final String responseHand = "aorta:core.char.hand.res";

    public static void setLang(int entityId, Language lang) {
        Rpc.sendToServer(setLang, entityId, lang);
    }

    public static void updateChar(int entityId, Character character, CharStatus status) {
        try {
            byte[] charBytes = CompressedStreamTools.compress(character.toNBT());
            byte[] statusBytes = CompressedStreamTools.compress(status.toNBT());
            Rpc.sendToServer(updateChar, entityId, charBytes, statusBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateStatus(int entityId, CharStatus status) {
        try {
            byte[] statusBytes = CompressedStreamTools.compress(status.toNBT());
            Rpc.sendToServer(updateStatus, entityId, statusBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearEntity(int entityId) {
        Rpc.sendToServer(clearEntity, entityId);
    }

    public static void requestHand(int entityId) {
        Rpc.sendToServer(requestHand, entityId);
    }

    @RpcMethod(setLang)
    public void onSetLang(MessageContext ctx, int entityId, Language lang) {
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityPlayer))
            return;

        LangAttribute.INSTANCE.set(entity, lang);
    }

    @RpcMethod(updateChar)
    public void onUpdateChar(MessageContext ctx, int entityId, byte[] charBytes, byte[] statusBytes) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity entity = sender.worldObj.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        try {
            final NBTTagCompound charNbt = CompressedStreamTools.func_152457_a(charBytes, new NBTSizeTracker(2097152L));
            final NBTTagCompound statusNbt = CompressedStreamTools.func_152457_a(statusBytes, new NBTSizeTracker(2097152L));

            if (entity instanceof EntityPlayer) {
                final Optional<Character> optChar = CharacterAttribute.get(entity);
                if (!optChar.isPresent())
                    return;
                final Character before = new Character(optChar.get());

                CharacterAttribute.INSTANCE.update(entity, c -> c.fromNBT(charNbt));
                StatusAttribute.INSTANCE.update(entity, s -> s.fromNBT(statusNbt));

                if (sender == entity || !CharacterAttribute.has(sender, Trait.gm))
                    sendLogs(sender, (EntityLivingBase) entity, "update_char", Differ.diff(before, optChar.get()));
            } else {
                final Character c = new Character();
                final CharStatus s = new CharStatus();
                c.fromNBT(charNbt);
                s.fromNBT(statusNbt);
                CharacterAttribute.INSTANCE.set(entity, c);
                StatusAttribute.INSTANCE.set(entity, s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(updateStatus)
    public void onUpdateStatus(MessageContext ctx, int entityId, byte[] statusBytes) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity entity = sender.worldObj.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        try {
            final NBTTagCompound statusNbt = CompressedStreamTools.func_152457_a(statusBytes, new NBTSizeTracker(2097152L));
            if (entity instanceof EntityPlayer) {
                final Optional<CharStatus> optStatus = StatusAttribute.get(entity);
                if (!optStatus.isPresent())
                    return;

                final CharStatus before = new CharStatus(optStatus.get());
                StatusAttribute.INSTANCE.update(entity, s -> s.fromNBT(statusNbt));

                if (sender == entity || !CharacterAttribute.has(sender, Trait.gm))
                    sendLogs(sender, (EntityLivingBase) entity, "update_status", Differ.diff(before, optStatus.get()));

                CharacterAttribute.get(entity).ifPresent(c -> {
                    sendLogs(sender, (EntityLivingBase) entity, "status", Differ.finalStatus(c, before, optStatus.get()));
                });

                if (!before.name.equals(optStatus.get().name)) {
                    ((EntityPlayer) entity).refreshDisplayName();
                    Rpc.sendToAll(refreshName, entityId);
                }
            } else {
                StatusAttribute.INSTANCE.update(entity, s -> s.fromNBT(statusNbt));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(refreshName)
    public void onRefreshName(MessageContext ctx, int entityId) {
        final Entity entity = FMLClientHandler.instance().getWorldClient().getEntityByID(entityId);
        if (entity instanceof EntityPlayer)
            ((EntityPlayer) entity).refreshDisplayName();
    }

    private void sendLogs(EntityPlayerMP sender, EntityLivingBase who, String type, String message) {
        if (message.isEmpty())
            return;

        final String namePrefix = sender == who ? "" : who.getCommandSenderName() + ": ";
        Logs.log(sender, type, namePrefix + message);

        final ChatMessage m = Composer.makeMessage(SpeechType.LOG, sender, message);
        m.speaker = (who instanceof EntityPlayer) ? ((EntityPlayer) who).getDisplayName() : who.getCommandSenderName();
        ChatHandler.sendSystemChatMessage(sender, m);
    }

    @RpcMethod(clearEntity)
    public void onClearEntity(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity entity = sender.worldObj.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase) || entity instanceof EntityPlayer)
            return;

        CharacterAttribute.INSTANCE.set(entity, null);
        StatusAttribute.INSTANCE.set(entity, null);
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
        final NBTTagCompound root = new NBTTagCompound();
        int itemCount = 0;

        for (int i = 0; i < 9; i++) {
            final ItemStack is = inv[i];
            if (is == null)
                continue;
            final NBTTagCompound tag = new NBTTagCompound();
            is.writeToNBT(tag);
            root.setTag(String.valueOf(i), tag);
            itemCount++;
        }

        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final DataOutputStream dos = new DataOutputStream(bos);
            CompressedStreamTools.write(root, dos);

            Rpc.sendTo(sender, responseHand, entityId, itemCount, bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RpcMethod(responseHand)
    public void onResponseHand(MessageContext ctx, int entityId, int itemCount, byte[] bytes) {
        try {
            final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            final DataInputStream dis = new DataInputStream(bis);
            final NBTTagCompound root = CompressedStreamTools.read(dis);

            final ArrayList<ItemStack> items = new ArrayList<>(itemCount);
            for (int i = 0; i < 9; i++) {
                final NBTTagCompound tag = root.getCompoundTag(String.valueOf(i));
                items.add(ItemStack.loadItemStackFromNBT(tag));
            }

            CharHand.setHand(entityId, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
