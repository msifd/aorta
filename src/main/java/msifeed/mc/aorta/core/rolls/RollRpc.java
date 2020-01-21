package msifeed.mc.aorta.core.rolls;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.RollComposer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import msifeed.mc.sys.utils.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Optional;

public enum RollRpc {
    INSTANCE;

    private static final String rollFeature = Bootstrap.MODID + ":core.roll.feature";
    private static final String rollAction = Bootstrap.MODID + ":core.roll.action";

    public static void rollFeature(int entityId, Feature feature, String target) {
        Rpc.sendToServer(rollFeature, entityId, feature, target);
    }

    public static void rollAction(int entityId, FightAction action, String target) {
        Rpc.sendToServer(rollAction, entityId, action, target);
    }

    @RpcMethod(rollFeature)
    public void onRollFeature(MessageContext ctx, int entityId, Feature feature, String target) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<MetaInfo> metaOpt = MetaAttribute.get(entity);
        if (charOpt.isPresent() && metaOpt.isPresent()) {
            final FeatureRoll roll = new FeatureRoll(charOpt.get(), metaOpt.get(), target, feature);
            final String text = RollComposer.makeText((EntityLivingBase) entity, charOpt.get(), roll);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendSystemChatMessage(player, m);

            final String prefix = player != entity ? ">> " : "";
            ExternalLogs.log(player, "feature", prefix + ChatUtils.stripFormatting(text));
        }
    }

    @RpcMethod(rollAction)
    public void onRollAction(MessageContext ctx, int entityId, FightAction action, String target) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(entityId);
        if (!(entity instanceof EntityLivingBase))
            return;

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<MetaInfo> metaOpt = MetaAttribute.get(entity);
        if (charOpt.isPresent() && metaOpt.isPresent()) {
            final FightRoll roll = new FightRoll(charOpt.get(), metaOpt.get(), target, action);
            final String text = RollComposer.makeText((EntityLivingBase) entity, charOpt.get(), roll);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendSystemChatMessage(player, m);

            final String prefix = player != entity ? ">> " : "";
            ExternalLogs.log(player, "action", prefix + ChatUtils.stripFormatting(text));
        }
    }
}
