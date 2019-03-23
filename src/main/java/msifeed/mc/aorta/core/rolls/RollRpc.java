package msifeed.mc.aorta.core.rolls;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.RollComposer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.sys.rpc.RpcMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Optional;

public enum RollRpc {
    INSTANCE;

    private static final String updateMods = "aorta:core.roll.modifiers";
    private static final String rollFeature = "aorta:core.roll.feature";
    private static final String rollAction = "aorta:core.roll.action";

    public static void updateMods(int entityId, Modifiers modifiers) {
        Rpc.sendToServer(updateMods, entityId, modifiers);
    }

    public static void rollFeature(int entityId, Feature[] features) {
        Rpc.sendToServer(rollFeature, entityId, features);
    }

    public static void rollAction(int entityId, FightAction action) {
        Rpc.sendToServer(rollAction, entityId, action);
    }

    @RpcMethod(updateMods)
    public void onUpdateMods(MessageContext ctx, int entityId, Modifiers modifiers) {
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        StatusAttribute.get(entity).ifPresent(status -> {
            status.modifiers = modifiers;
            StatusAttribute.INSTANCE.broadcast(world, entity);
        });
    }

    @RpcMethod(rollFeature)
    public void onRollFeature(MessageContext ctx, int entityId, Feature[] features) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<CharStatus> statusOpt = StatusAttribute.get(entity);
        if (charOpt.isPresent() && statusOpt.isPresent()) {
            final FeatureRoll roll = new FeatureRoll(charOpt.get(), statusOpt.get(), features);
            final String text = RollComposer.makeText((EntityLivingBase) entity, roll);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage(player, m);
        }
    }

    @RpcMethod(rollAction)
    public void onRollAction(MessageContext ctx, int entityId, FightAction action) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<CharStatus> statusOpt = StatusAttribute.get(entity);
        if (charOpt.isPresent() && statusOpt.isPresent()) {
            final FightRoll roll = new FightRoll(charOpt.get(), statusOpt.get(), action);
            final String text = RollComposer.makeText((EntityLivingBase) entity, roll);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage(player, m);
        }
    }
}
