package msifeed.mc.aorta.core.character;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
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
import msifeed.mc.aorta.rpc.Rpc;
import msifeed.mc.aorta.rpc.RpcMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.Optional;

public enum CharRpc {
    INSTANCE;

    private static final String setLang = "aorta:core.char.lang";
    private static final String updateChar = "aorta:core.char.char";
    private static final String updateStatus = "aorta:core.char.status";
    private static final String clearEntity = "aorta:core.char.clear";

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
                CharacterAttribute.INSTANCE.update(entity, c -> c.fromNBT(charNbt));
                StatusAttribute.INSTANCE.update(entity, s -> s.fromNBT(statusNbt));
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
                    sendLogs(sender, (EntityLivingBase) entity, "status", Differ.status(c, before, optStatus.get()));
                });

                if (!before.name.equals(optStatus.get().name))
                    ((EntityPlayer) entity).refreshDisplayName();
            } else {
                StatusAttribute.INSTANCE.update(entity, s -> s.fromNBT(statusNbt));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
