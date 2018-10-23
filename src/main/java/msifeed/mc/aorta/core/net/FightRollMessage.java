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
import msifeed.mc.aorta.core.rules.FightAction;
import msifeed.mc.aorta.core.rules.FightRoll;
import msifeed.mc.aorta.core.status.CharStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Optional;

public class FightRollMessage implements IMessage, IMessageHandler<FightRollMessage, IMessage> {
    public int entityId;
    public FightAction action;
    public int mod;

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        action = FightAction.values()[buf.readByte()];
        mod = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(action.ordinal());
        buf.writeInt(mod);
    }

    @Override
    public IMessage onMessage(FightRollMessage message, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        final World world = ctx.getServerHandler().playerEntity.worldObj;
        final Entity entity = world.getEntityByID(message.entityId);
        if (!(entity instanceof EntityLivingBase))
            return null;

        final Optional<Character> charOpt = CharacterAttribute.get(entity);
        final Optional<CharStatus> statusOpt = StatusAttribute.get(entity);
        if (charOpt.isPresent() && statusOpt.isPresent()) {
            final FightRoll result = new FightRoll(charOpt.get(), statusOpt.get(), message.action, message.mod);
            final String text = RollComposer.makeText((EntityLivingBase) entity, result);
            final ChatMessage m = Composer.makeMessage(SpeechType.ROLL, player, text);
            ChatHandler.sendChatMessage(player, m);
        }

        return null;
    }
}
