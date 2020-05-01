package msifeed.mc.more.crabs.character;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.commons.traits.Trait;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.LangAttribute;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.extensions.tweaks.EsitenceHealthModifier;
import msifeed.mc.more.crabs.rolls.Modifiers;
import msifeed.mc.more.crabs.rolls.Rolls;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.Differ;
import msifeed.mc.more.crabs.utils.GetUtils;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.attributes.MissingRequiredAttributeException;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import msifeed.mc.sys.rpc.RpcMethodException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
    private static final String rollAbility = Bootstrap.MODID + ":char.roll";

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
        GetUtils.entityLiving(ctx.getServerHandler().playerEntity, entityId)
                .ifPresent(e -> LangAttribute.INSTANCE.set(e, Language.values()[langIdx]));
    }

    @RpcMethod(updateChar)
    public void onUpdateChar(MessageContext ctx, int entityId, NBTTagCompound charNbt) {
        final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId).orElse(null);
        if (target == null)
            return;

        final boolean senderIsGm = CharacterAttribute.has(sender, Trait.gm);
        final boolean senderIsTarget = sender.getEntityId() == target.getEntityId();
        final boolean targetIsPlayer = target instanceof EntityPlayer;

        if (!senderIsGm && !senderIsTarget && targetIsPlayer) {
            ExternalLogs.log(sender, "warning", String.format("%s tried to change character %s while not GM!", sender.getCommandSenderName(), target.getCommandSenderName()));
            ctx.getServerHandler().kickPlayerFromServer("Forbidden!");
            return;
        }

        final Character after = CharacterAttribute.get(target).orElse(null);
        if (after != null) {
            final Character before = new Character(after);
            CharacterAttribute.INSTANCE.update(target, c -> c.fromNBT(charNbt));

            Differ.printDiffs(sender, target, before, after);

            if (target instanceof EntityPlayer) {
                if (!before.name.equals(after.name)) {
                    ((EntityPlayer) target).refreshDisplayName();
                    Rpc.sendToAll(refreshName, entityId);
                }

                if (before.estitence != after.estitence) {
                    EsitenceHealthModifier.applyModifier((EntityPlayer) target, after);
                }
            }
        } else {
            final Character c = new Character();
            c.fromNBT(charNbt);
            CharacterAttribute.INSTANCE.set(target, c);
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
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .filter(e -> !(e instanceof EntityPlayer))
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        if (CharacterAttribute.require(sender).has(Trait.gm)) {
            CharacterAttribute.INSTANCE.set(target, null);
            MetaAttribute.INSTANCE.set(target, null);
        }
    }

    public static void rollAbility(int entityId, Ability ability) {
        Rpc.sendToServer(rollAbility, entityId, ability.ordinal());
    }

    @RpcMethod(rollAbility)
    public void onRollAbility(MessageContext ctx, int entityId, int abilityOrd) {
        final EntityPlayer sender = ctx.getServerHandler().playerEntity;
        final EntityLivingBase target = GetUtils.entityLiving(sender, entityId)
                .orElseThrow(() -> new RpcMethodException(sender, "invalid target entity"));

        final Ability[] abilityValues = Ability.values();
        if (abilityOrd >= abilityValues.length)
            return;

        try {
            final Character c = CharacterAttribute.require(target);
            final Modifiers m = MetaAttribute.require(target).modifiers;
            final Ability a = abilityValues[abilityOrd];

            final Rolls.Result roll = Rolls.rollAbility(c, m, a);
            final String fmtRoll = roll.format(m.roll, m.toAbility(a), a);
            final String name = target instanceof EntityPlayer ? ((EntityPlayer) target).getDisplayName() : target.getCommandSenderName();
            final String text = String.format("%s: %s %s", name, a.trShort(), fmtRoll);

            ChatHandler.sendSystemChatMessage(target, Composer.makeMessage(SpeechType.ROLL, sender, text));
            ExternalLogs.log(sender, "roll", text);
        } catch (MissingRequiredAttributeException e) {
        }
    }
}
