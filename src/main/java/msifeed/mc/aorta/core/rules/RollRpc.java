package msifeed.mc.aorta.core.rules;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.RollComposer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.aorta.rpc.RpcMethod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Optional;

public class RollRpc {
    public static final String rollFeature = "aorta:core.roll.feature";
    public static final String rollAction = "aorta:core.roll.action";

    @RpcMethod(rollFeature)
    public void rollFeature(MessageContext ctx, int entityId, Feature feature, int mod) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<CharStatus> statusOpt = StatusAttribute.get(entity);
        if (charOpt.isPresent() && statusOpt.isPresent()) {
            final FeatureRoll result = new FeatureRoll(charOpt.get(), statusOpt.get(), feature, mod);
            final String text = RollComposer.makeText((EntityLivingBase) entity, result);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage(player, m);
        }
    }

    @RpcMethod(rollAction)
    public void rollAction(MessageContext ctx, int entityId, FightAction action, int mod) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<CharStatus> statusOpt = StatusAttribute.get(entity);
        if (charOpt.isPresent() && statusOpt.isPresent()) {
            final FightRoll result = new FightRoll(charOpt.get(), statusOpt.get(), action, mod);
            final String text = RollComposer.makeText((EntityLivingBase) entity, result);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage(player, m);
        }
    }
}
