package msifeed.mc.aorta.core.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.RollComposer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rules.FeatureRoll;
import msifeed.mc.aorta.core.rules.FeatureRollResult;
import msifeed.mc.aorta.core.status.CharStatus;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Optional;

public class FeatureRollMessage implements IMessage, IMessageHandler<FeatureRollMessage, IMessage> {
    public Feature feature;
    public int mod;

    @Override
    public void fromBytes(ByteBuf buf) {
        feature = Feature.values()[buf.readByte()];
        mod = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(feature.ordinal());
        buf.writeInt(mod);
    }

    @Override
    public IMessage onMessage(FeatureRollMessage message, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        final Optional<Character> charOpt = CharacterAttribute.get(player);
        final Optional<CharStatus> statusOpt = StatusAttribute.get(player);
        if (charOpt.isPresent() && statusOpt.isPresent()) {
            final FeatureRollResult result = FeatureRoll.roll(charOpt.get(), statusOpt.get(), message.feature, message.mod);
            final String text = RollComposer.makeText(player, result);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage(player, m);
        }

        return null;
    }
}
