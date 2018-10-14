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
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rules.FeatureRoll;
import net.minecraft.entity.player.EntityPlayerMP;

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
        CharacterAttribute.get(player).ifPresent(c -> {
            final int result = FeatureRoll.roll(c, message.feature, message.mod);
            final String text = RollComposer.makeText(player, message.feature, message.mod, result);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage(player, m);
        });
        return null;
    }
}
