package msifeed.mc.more.crabs.character;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.LangAttribute;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.Differ;
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
import net.minecraft.world.World;

import java.io.IOException;

public enum CharRpc {
    INSTANCE;

    private static final String setLang = Bootstrap.MODID + ":lang";
    private static final String updateChar = Bootstrap.MODID + ":char.update";
    private static final String refreshName = Bootstrap.MODID + ":char.refreshName";
    private static final String clearEntity = Bootstrap.MODID + ":char.clear";

    public static void setLang(int entityId, Language lang) {
        Rpc.sendToServer(setLang, entityId, lang);
    }

    public static void updateChar(int entityId, Character character) {
        try {
            byte[] charBytes = CompressedStreamTools.compress(character.toNBT());
            Rpc.sendToServer(updateChar, entityId, charBytes);
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
    public void onUpdateChar(MessageContext ctx, int entityId, byte[] charBytes) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity entity = sender.worldObj.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        final boolean senderIsGm = CharacterAttribute.has(sender, Trait.gm);
        final boolean senderIsTarget = sender.getEntityId() == entity.getEntityId();
        final boolean targetIsPlayer = entity instanceof EntityPlayer;

        if (!senderIsGm && !senderIsTarget && targetIsPlayer) {
            ExternalLogs.log(sender, "warning", String.format("%s tried to change character %s while not GM!", sender.getCommandSenderName(), entity.getCommandSenderName()));
            ctx.getServerHandler().kickPlayerFromServer("Forbidden!");
            return;
        }

        try {
            final NBTTagCompound charNbt = CompressedStreamTools.func_152457_a(charBytes, new NBTSizeTracker(2097152L));

            final Character after = CharacterAttribute.get(entity).orElse(null);
            if (after != null) {
                final Character before = new Character(after);
                CharacterAttribute.INSTANCE.update(entity, c -> c.fromNBT(charNbt));

                final String diffChanges = Differ.diff(before, after);
                final String diffResults = Differ.diffResults(before, after);

                final String speaker = before.name.isEmpty() ? entity.getCommandSenderName() : before.name;
                final String logPrefix = sender == entity ? "" : "(" + speaker + ") ";
                sendLogs(sender, speaker, logPrefix, diffChanges);
                sendLogs(sender, speaker, logPrefix, diffResults);

                if (entity instanceof EntityPlayer && !before.name.equals(after.name)) {
                    ((EntityPlayer) entity).refreshDisplayName();
                    Rpc.sendToAll(refreshName, entityId);
                }
            } else {
                final Character c = new Character();
                c.fromNBT(charNbt);
                CharacterAttribute.INSTANCE.set(entity, c);
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

    private void sendLogs(EntityPlayerMP sender, String speaker, String logPrefix, String message) {
        if (message.isEmpty())
            return;

        final ChatMessage m = Composer.makeMessage(SpeechType.LOG, sender, message);
        m.speaker = speaker;
        ChatHandler.sendSystemChatMessage(sender, m);

        ExternalLogs.log(sender, "log", logPrefix + message);
    }

    @RpcMethod(clearEntity)
    public void onClearEntity(MessageContext ctx, int entityId) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final Entity entity = sender.worldObj.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase) || entity instanceof EntityPlayer)
            return;

        if (CharacterAttribute.require(sender).has(Trait.gm)) {
            CharacterAttribute.INSTANCE.set(entity, null);
            MetaAttribute.INSTANCE.set(entity, null);
        }
    }
}
