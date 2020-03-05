package msifeed.mc.more.crabs.character;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.LangAttribute;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.Differ;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public enum CharRpc {
    INSTANCE;

    private static final String setLang = Bootstrap.MODID + ":lang";
    private static final String updateChar = Bootstrap.MODID + ":char.update";
    private static final String refreshName = Bootstrap.MODID + ":char.refreshName";
    private static final String clearEntity = Bootstrap.MODID + ":char.clear";

    public static void setLang(int entityId, Language lang) {
        Rpc.sendToServer(setLang, entityId, lang.ordinal());
    }

    public static void updateChar(int entityId, Character character) {
        Rpc.sendToServer(updateChar, entityId, character.toNBT());
    }

    public static void clearEntity(int entityId) {
        Rpc.sendToServer(clearEntity, entityId);
    }

    @RpcMethod(setLang)
    public void onSetLang(MessageContext ctx, int entityId, int langIdx) {
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityPlayer))
            return;

        LangAttribute.INSTANCE.set(entity, Language.values()[langIdx]);
    }

    @RpcMethod(updateChar)
    public void onUpdateChar(MessageContext ctx, int entityId, NBTTagCompound charNbt) {
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

        final Character after = CharacterAttribute.get(entity).orElse(null);
        if (after != null) {
            final Character before = new Character(after);
            CharacterAttribute.INSTANCE.update(entity, c -> c.fromNBT(charNbt));

            Differ.printDiffs(sender, entity, before, after);

            if (entity instanceof EntityPlayer) {
                if (!before.name.equals(after.name)) {
                    ((EntityPlayer) entity).refreshDisplayName();
                    Rpc.sendToAll(refreshName, entityId);
                }

                if (before.estitence != after.estitence) {
                    ((EntityPlayer)entity).getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(after.countMaxHealth());
                }
            }
        } else {
            final Character c = new Character();
            c.fromNBT(charNbt);
            CharacterAttribute.INSTANCE.set(entity, c);
        }
    }

    @RpcMethod(refreshName)
    public void onRefreshName(MessageContext ctx, int entityId) {
        final Entity entity = FMLClientHandler.instance().getWorldClient().getEntityByID(entityId);
        if (entity instanceof EntityPlayer)
            ((EntityPlayer) entity).refreshDisplayName();
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
